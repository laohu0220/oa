package cn.zkdcloud.processing;

import cn.zkdcloud.annotation.Before;
import cn.zkdcloud.entity.Menu;
import cn.zkdcloud.entity.MenuTree;
import cn.zkdcloud.exception.ErrorPageException;
import cn.zkdcloud.interceptors.InputMenuInterceptor;
import cn.zkdcloud.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author zk
 * @Date 2017/5/20.
 * @Email 2117251154@qq.com
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends ContentController{

    @Autowired
    MenuService menuService;

    /** 测试专用，仅列出所有的目录
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/listAll")
    public String listAll(ModelMap modelMap){
        MenuTree menuTree= menuService.menuTreeAllList();
        modelMap.put("menuTree",menuTree);
        return "index";
    }

    /** 添加目录页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String addMenu(ModelMap modelMap){
        modelMap.put("menuTree",menuService.menuTreeAllList());
        modelMap.put("menuList",menuService.getMenuList());
        return "menu/add";
    }

    /** 添加目录操作
     *
     * @return
     */
    @Before(InputMenuInterceptor.class)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addMenu(){
        menuService.addMenu(getReqString("menuName"),getReqString("menuDescribe"),
                getReqString("parentId"), Integer.valueOf(getReqString("menuSort")),"haha");
        return OPERSTOR_SUCCESS;
    }

    /** 目录列表
     *
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String listMenu(ModelMap modelMap){
        Integer curPage = getReqString("p") == null ? 1 : Integer.valueOf(getReqString("p"));
        Integer pageSize = getReqString("pageSize") == null ? 10 : Integer.valueOf(getReqString("pageSize"));

        Page<Menu> menuPage = menuService.getMenuList(curPage,pageSize);
        Integer sumPage = menuPage.getTotalPages();
        modelMap.put("sumPage",sumPage);
        modelMap.put("curPage",curPage);
        modelMap.put("menuTree",menuService.menuTreeAllList());
        modelMap.put("menuPage",menuPage);
        return "menu/list";
    }

    /**修改目录页面
     *
     * @return
     */
    @RequestMapping(value = "/modify",method = RequestMethod.GET)
    public String modifyMenu(ModelMap modelMap){
        Menu menu = menuService.getMenu(getReqString("menuId"));
        if(menu == null)
            throw new ErrorPageException("{\"code\":403,\"tip\":\"错误请求\"}");

        modelMap.put("modifyMenu",menu);
        modelMap.put("menuList",menuService.getMenuList());
        modelMap.put("menuTree",menuService.menuTreeAllList());
        return "menu/modify";
    }

    /***修改目录操作
     *
     * @return
     */
    @Before(InputMenuInterceptor.class)
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    @ResponseBody
    public String modifyMenu(){
        menuService.updateMenu(getReqString("menuId"),getReqString("menuDescribe"),getReqString("parentId"),
                getReqString("menuName"), Integer.valueOf(getReqString("menuSort")),"haha");
        return OPERSTOR_SUCCESS;
    }

    /** 删除目录操作
     *
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public String removeMenu(){
        menuService.removeMenu(getReqString("menuId"),"haha");
        return OPERSTOR_SUCCESS;
    }
}