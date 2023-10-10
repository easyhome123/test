package cn.xueden.hotel.service;

import cn.xueden.hotel.domain.HotelMember;
import cn.xueden.hotel.service.dto.MemberQueryCriteria;
import cn.xueden.hotel.vo.MemberPwdModel;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.Pageable;

public interface IHotelMemberService {


    Object getList(MemberQueryCriteria queryCriteria, Pageable pageable);

    HotelMember getById(Long id);

    void recharge(HotelMember hotelMember);

    void deleteById(Long id);

    void register(HotelMember hotelMember);

    HotelMember login(HotelMember hotelMember);

    HotelMember getLoginMemberInfo(Long memberId);

    void updateMember(HotelMember hotelMember);

    void updateMemberPwd(MemberPwdModel memberPwdModel);
}
