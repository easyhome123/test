package cn.xueden.hotel.repository;

import cn.xueden.hotel.domain.HotelReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HotelReserveRepository extends JpaRepository<HotelReserve,Long> ,
        JpaSpecificationExecutor<HotelReserve> {

    //根据订单查询信息
    HotelReserve findByOrderNumber(String orderNumber);

    List<HotelReserve> findAllByRoomId(Long id);
}
