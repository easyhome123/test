package cn.xueden.hotel.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

@Data
public class ReserveQueryCriteria {
    @EnableXuedenQuery(blurry = "orderNumber")
    private String searchValue;

    @EnableXuedenQuery
    private Integer status;

    //创建者查询
    @EnableXuedenQuery
    private  Long createBy;
}
