package cn.xueden.hotel.repository;

import cn.xueden.hotel.domain.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRoomRepository extends JpaRepository<HotelRoom,Long>, JpaSpecificationExecutor<HotelRoom> {
    HotelRoom findByRoomNumber(String roomNumber);

    @Query(value = "select * from hotel_room where floor_id=?1 LiMIT ?2",nativeQuery = true)
    List<HotelRoom> getRoomListByFloorId(Long id, int num);
}
