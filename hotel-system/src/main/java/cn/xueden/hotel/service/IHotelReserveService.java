package cn.xueden.hotel.service;

import cn.xueden.hotel.domain.HotelCheckin;
import cn.xueden.hotel.domain.HotelReserve;
import cn.xueden.hotel.service.dto.ReserveQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Set;

public interface IHotelReserveService {
    Object getList(ReserveQueryCriteria queryCriteria, Pageable pageable);

    HotelReserve getById(Long id);

    void unsubscribe(Long id);

    void payment(Long id);


    void checkin(HotelCheckin hotelCheckin);

    Set<LocalDate> getDatesByRoomId(Long id);

    void addReserve(HotelReserve hotelReserve);


    Object getMyReserveList(ReserveQueryCriteria reserveQueryCriteria, Pageable pageable);
}
