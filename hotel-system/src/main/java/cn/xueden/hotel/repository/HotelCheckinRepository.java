package cn.xueden.hotel.repository;

import cn.xueden.hotel.domain.HotelCheckin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

//入住登记持久层
public interface HotelCheckinRepository extends JpaRepository<HotelCheckin,Long>, JpaSpecificationExecutor<HotelCheckin> {

    @Query(value="update HotelCheckin set status = 1 where id=?1")
    @Modifying
    void updateStatusById(Long id);

}
