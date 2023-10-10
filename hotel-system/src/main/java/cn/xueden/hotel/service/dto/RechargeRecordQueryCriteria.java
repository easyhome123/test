package cn.xueden.hotel.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

//查询充值记录条件
@Data
public class RechargeRecordQueryCriteria {

    @EnableXuedenQuery
    private Long memberId;
}
