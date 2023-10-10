package cn.xueden.system.repository;

import cn.xueden.system.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：系统用户持久层
 */
public interface SysUserRepository extends JpaRepository<SysUser,Long>, JpaSpecificationExecutor<SysUser> {

    /**
     * 根据登录名查找用户信息
     * @param username
     * @return
     */
    SysUser findByUsername(String username);

}
