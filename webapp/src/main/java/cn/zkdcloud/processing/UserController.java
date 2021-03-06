package cn.zkdcloud.processing;

import cn.zkdcloud.annotation.Before;
import cn.zkdcloud.entity.Function;
import cn.zkdcloud.entity.Role;
import cn.zkdcloud.entity.User;
import cn.zkdcloud.exception.TipException;
import cn.zkdcloud.interceptors.*;
import cn.zkdcloud.service.MenuService;
import cn.zkdcloud.service.RoleService;
import cn.zkdcloud.service.UserService;
import cn.zkdcloud.util.Const;
import cn.zkdcloud.util.Md5Util;
import cn.zkdcloud.util.StrUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zk
 * @Date 2017/5/20.
 * @Email 2117251154@qq.com
 */
@Controller
@RequestMapping("/user")
public class UserController extends ContentController{
    private static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    /** 添加用户页面
     *
     * @return
     */
    @Before({LoginCheckInterceptor.class,PowerCheckInterceptor.class})
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String addUserPage(ModelMap modelMap){
        Role role = getLoginUser().getRole();
        modelMap.put("roleList",roleService.listRoleLess(role.getRolePowerSize())); //获取权限比自己小的角色列表
        return "user/add";
    }

    /** 添加用户请求
     *
     * @return
     */
    @Before({InputBaseUserInterceptor.class, PowerCheckInterceptor.class})
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addUser(){
        userService.addUser(getReqString("username"),getReqString("password"),getReqString("roleId"),getLoginUserName());
        return OPERSTOR_SUCCESS;
    }

    /** 删除用户请求
     *
     * @return
     */
    @Before({LoginCheckInterceptor.class,PowerCheckInterceptor.class})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public String removeUser(){
        userService.removeUser(getReqString("uid"));
        return OPERSTOR_SUCCESS;
    }

    /** 登录请求
     *
     * @return
     */
    @Before(InputBaseUserInterceptor.class)
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public String login(){
        User user = userService.loginUser(getReqString("username"),getReqString("password"));
        session.setAttribute(Const.USER_LOGIN,user.getUid());
        return "登录成功";
    }

    /**注销用户
     *
     * @return
     */
    @RequestMapping(value = "/off",method = RequestMethod.GET)
    public String off(){
        session.removeAttribute(Const.USER_LOGIN);
        return "login";
    }

    /** 用户列表页面
     *
     * @param modelMap
     * @return
     */
    @Before({LoginCheckInterceptor.class,PowerCheckInterceptor.class})
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String userList(ModelMap modelMap){
        Integer rolePowerSize = getLoginUser().getRole().getRolePowerSize();
        Integer curPage = getReqString("p") == null ? 1 : Integer.parseInt(getReqString("p"));
        Integer pageSize = getReqString("pageSize") == null ? 10 : Integer.parseInt(getReqString("pageSize"));

        Page<User> userPage = userService.getUserListByRoleLess(curPage,pageSize,rolePowerSize);
        Integer sumPage = userPage.getTotalPages();

        modelMap.put("sumPage",sumPage);
        modelMap.put("curPage",curPage);
        modelMap.put("userPage",userPage);

        return "user/list";
    }

    /** 修改用户请求页面
     *
     * @return
     */
    @Before({LoginCheckInterceptor.class,PowerCheckInterceptor.class})
    @RequestMapping(value = "/modify",method = RequestMethod.GET)
    public String modifyUser(ModelMap modelMap){
        String uid = getReqString("uid");
        modelMap.put("modifyUser",userService.getUserByUid(uid));
        modelMap.put("roleList",roleService.listRoleLess(getLoginUser().getRole().getRolePowerSize()));
        return "user/modify";
    }

    /**修改用户请求
     *
     * @return
     */
    @Before({LoginCheckInterceptor.class,InputBaseUserInterceptor.class,PowerCheckInterceptor.class})
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    @ResponseBody
    public String modifyUser(){
        userService.modifyUser(getReqString("uid"),getReqString("username"),getReqString("password"),
                getReqString("roleId"),getLoginUserName());
        return OPERSTOR_SUCCESS;
    }

    /** 个人设置页面
     *
     * @return
     */
    @Before({LoginCheckInterceptor.class})
    @RequestMapping(value = "/personal",method = RequestMethod.GET)
    public String personal(){
        return "user/personal";
    }

    /**修改个人信息操作,有些特殊原因，必须在这里校验
     *
     * @return
     */
    @Before({LoginCheckInterceptor.class})
    @RequestMapping(value = "/personal",method = RequestMethod.POST)
    @ResponseBody
    public String personalModify(){
        User user = getLoginUser();

        String username = getReqString("username");
        String password = getReqString("password");
        String new_password= getReqString("new_password");

        if(StrUtil.isBlank(username) || StrUtil.notDigitAndLetterAndChinese(username))
            throw new TipException("用户名仅限定于（字母，数字，中文）");

        if(StrUtil.isBlank(password))
            password = user.getPassword();
        else if(!Md5Util.toMD5(password).equals(user.getPassword()))
            throw new TipException("旧密码输入错误");
        else if(StrUtil.isBlank(new_password) || StrUtil.notDigitAndLetterAndChinese(new_password))
            throw new TipException("新密码仅限定于（字母，数字，中文）");
        else
            password = new_password;

        userService.modifyPersonal(username,password,getReqString("email"),getReqString("url"),
                user.getUid());
        return OPERSTOR_SUCCESS;
    }

}
