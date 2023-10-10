package cn.xueden.hotel.repository;

import cn.xueden.hotel.domain.HotelRechargeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HotelRechargeRecordRepository extends JpaRepository<HotelRechargeRecord,Long> , JpaSpecificationExecutor<HotelRechargeRecord> {

}
