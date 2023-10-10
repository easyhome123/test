package cn.xueden.hotel.service;

import cn.xueden.hotel.service.dto.CheckinQueryCriteria;
import org.springframework.data.domain.Pageable;

public interface IHotelCheckinService {
    Object getList(CheckinQueryCriteria checkinQueryCriteria, Pageable pageable);

    void checkout(Long id);
}
