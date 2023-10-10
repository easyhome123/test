package cn.xueden.hotel.service;

import cn.xueden.hotel.service.dto.RechargeRecordQueryCriteria;
import org.springframework.data.domain.Pageable;

public interface IHotelRechargeRecordService {
    Object getMyRechargeRecordList(RechargeRecordQueryCriteria queryCriteria, Pageable pageable);
}
