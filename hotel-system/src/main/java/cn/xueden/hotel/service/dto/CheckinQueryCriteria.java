package cn.xueden.hotel.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

@Data
public class CheckinQueryCriteria {

    //根据姓名，订单号，手机号，身份证进行模糊查询
    @EnableXuedenQuery(blurry = "orderNumber,memberName,phone,idCard")
    private String searchValue;

    @EnableXuedenQuery
    private Integer status;
}
