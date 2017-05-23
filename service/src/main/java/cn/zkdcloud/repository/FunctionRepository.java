package cn.zkdcloud.repository;

import cn.zkdcloud.entity.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author zk
 * @Date 2017/5/19.
 * @Email 2117251154@qq.com
 */
public interface FunctionRepository extends JpaRepository<Function,String>{

    List<Function> findByMenuId(String menuId);

    List<Function> findByFunctionUrl(String url);

    @Query("select f from Function f where f.functionId in (select rp.functionId from RolePower rp where rp.roleId = :roleId)")
    Page<Function> findByRoleRoleId(@Param("roleId") String roleId, Pageable pageable);

    @Query("select f from Function f where f.functionId in (select rp.functionId from RolePower rp where rp.roleId = :roleId)")
    List<Function> findByRoleRoleId(@Param("roleId") String roleId,Sort sort);
}
