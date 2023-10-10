package cn.xueden.hotel.repository;

import cn.xueden.hotel.domain.HotelRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HotelRoomTypeRepository extends JpaRepository<HotelRoomType , Long> , JpaSpecificationExecutor<HotelRoomType> {
}
