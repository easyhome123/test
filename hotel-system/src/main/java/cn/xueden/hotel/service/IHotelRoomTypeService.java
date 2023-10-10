package cn.xueden.hotel.service;


import cn.xueden.hotel.domain.HotelRoomType;
import cn.xueden.hotel.service.dto.RoomTypeQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

//房间类型的业务接口
public interface IHotelRoomTypeService {

    Object getList(RoomTypeQueryCriteria queryCriteria, Pageable pageable);

    /**新增房间类型方法**/
    void addRoomType(HotelRoomType hotelRoomType);

    HotelRoomType getById(Long id);

    void editRoomType(HotelRoomType hotelRoomType);

    void deleteById(Long id);

    List<HotelRoomType> getAll();
}
