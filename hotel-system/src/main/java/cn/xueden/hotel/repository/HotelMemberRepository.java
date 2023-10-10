package cn.xueden.hotel.repository;

import cn.xueden.hotel.domain.HotelFloor;
import cn.xueden.hotel.domain.HotelMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HotelMemberRepository extends JpaRepository<HotelMember,Long>, JpaSpecificationExecutor<HotelMember> {
    HotelMember findByPhone(String phone);
}
