package cn.xueden.hotel.service;

import cn.xueden.hotel.domain.HotelFloor;
import cn.xueden.hotel.service.dto.FloorQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHotelFloorService {

    Object getList(FloorQueryCriteria queryCriteria, Pageable pageable);

    void addFloor(HotelFloor hotelFloor);

    HotelFloor getById(Long id);

    void editFloor(HotelFloor hotelFloor);

    void deleteById(Long id);

    List<HotelFloor> getAll();
}
