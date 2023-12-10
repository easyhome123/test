package cn.xueden.hotel.service;


import cn.xueden.hotel.domain.HotelRoom;
import cn.xueden.hotel.service.dto.RoomQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHotelRoomService {
    //获取房间列表数据
    Object getList(RoomQueryCriteria queryCriteria, Pageable pageable);

    void addRoom(HotelRoom hotelRoom);

    HotelRoom getById(Long id);

    void editRoom(HotelRoom hotelRoom);

    void deleteById(Long id);

    //根据楼层id，获取房间列表
    List<HotelRoom> getRoomListByFloorId(Long id, int num);

    void cleanroom(Long id);
}
