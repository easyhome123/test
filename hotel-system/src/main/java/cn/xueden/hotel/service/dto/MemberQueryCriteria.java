package cn.xueden.hotel.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

@Data
public class MemberQueryCriteria {

    //根据邮箱，手机，姓名，名称查询
    @EnableXuedenQuery(blurry = "email,phone,realName,memberName")
    private String searchValue;


}
