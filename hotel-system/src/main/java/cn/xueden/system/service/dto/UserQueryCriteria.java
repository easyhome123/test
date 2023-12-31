package cn.xueden.system.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：系统用户查询条件参数
 */
@Data
public class UserQueryCriteria {

    /**
     * 根据用户名、真实姓名、性别、邮箱模糊查询
     */
   @EnableXuedenQuery(blurry = "username,realname,sex,email")
   private String searchValue;

    /**
     * 根据状态查询
     */
    @EnableXuedenQuery
    private String status;

}
