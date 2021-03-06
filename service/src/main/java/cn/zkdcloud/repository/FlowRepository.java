package cn.zkdcloud.repository;

import cn.zkdcloud.entity.Flow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author zk
 * @Date 2017/5/28.
 * @Email 2117251154@qq.com
 */
public interface FlowRepository extends JpaRepository<Flow,String>{
    Page<Flow> findByUid(String uid, Pageable pageable);
}
