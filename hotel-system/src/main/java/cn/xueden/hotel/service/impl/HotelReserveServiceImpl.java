package cn.xueden.hotel.service.impl;

import cn.xueden.exception.BadRequestException;
import cn.xueden.hotel.domain.*;
import cn.xueden.hotel.repository.*;
import cn.xueden.hotel.service.IHotelReserveService;
import cn.xueden.hotel.service.dto.ReserveQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import cn.xueden.utils.XuedenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class HotelReserveServiceImpl implements IHotelReserveService {
    private final HotelReserveRepository hotelReserveRepository;
    private final HotelRoomRepository hotelRoomRepository;
    private final HotelMemberRepository hotelMemberRepository;

    private final HotelCheckinRepository hotelCheckinRepository;

    private final HotelRoomTypeRepository hotelRoomTypeRepository;

    public HotelReserveServiceImpl(HotelReserveRepository hotelReserveRepository, HotelRoomRepository hotelRoomRepository, HotelMemberRepository hotelMemberRepository, HotelCheckinRepository hotelCheckinRepository, HotelRoomTypeRepository hotelRoomTypeRepository) {
        this.hotelReserveRepository = hotelReserveRepository;
        this.hotelRoomRepository = hotelRoomRepository;
        this.hotelMemberRepository = hotelMemberRepository;
        this.hotelCheckinRepository = hotelCheckinRepository;
        this.hotelRoomTypeRepository = hotelRoomTypeRepository;
    }

    @Override
    public Object getList(ReserveQueryCriteria queryCriteria, Pageable pageable) {
        Page<HotelReserve> page = hotelReserveRepository.findAll(
                (root, query, criteriaBuilder)-> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        setRoomToReserve(page.stream().toList());
        return PageUtil.toPage(page);
    }
    //获取房间信息
    private List<HotelReserve> setRoomToReserve(List<HotelReserve> hotelReserves){
        for(HotelReserve r:hotelReserves){
            if(r.getRoomId()!=null && r.getRoomId()!=0 ){
                HotelRoom hotelRoom = hotelRoomRepository.findById(r.getRoomId()).orElseGet(HotelRoom::new);
                if(StringUtils.isNoneBlank(hotelRoom.getRoomName())){
                    r.setHotelRoom(hotelRoom);
                }
            }
            if(r.getCreateBy()!= null && r.getCreateBy()!=0){
                HotelMember hotelMember = hotelMemberRepository.findById(r.getCreateBy()).orElseGet(HotelMember::new);
                if(StringUtils.isNoneBlank(hotelMember.getMemberName())){
                    r.setHotelMember(hotelMember);
                }
            }
        }
        return hotelReserves;
    }

    @Override
    public HotelReserve getById(Long id) {
        return hotelReserveRepository.findById(id).orElseGet(HotelReserve::new);
    }


    //退订房间
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unsubscribe(Long id) {
        HotelReserve hotelReserve = hotelReserveRepository.findById(id).orElseGet(HotelReserve::new);
        HotelRoom hotelRoom = hotelRoomRepository.findById(hotelReserve.getRoomId()).orElseGet(HotelRoom::new);
        if(hotelReserve.getStatus()!=0&&hotelReserve.getStatus()!=1){
            throw new BadRequestException("该订单已完成，请勿重复操作");
        }else {
            hotelReserve.setStatus(3);
            hotelRoom.setRoomStatus(0);//退订房间状态为 0
            hotelReserveRepository.save(hotelReserve);
            hotelRoomRepository.save(hotelRoom);
        }
    }

    //付款
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payment(Long id) {
        //根据id获取订单信息
        HotelReserve hotelReserve = hotelReserveRepository.getReferenceById(id);
        //根据创建者ID获取会员信息
        HotelMember hotelMember = hotelMemberRepository.getReferenceById(hotelReserve.getCreateBy());
        if(hotelMember.getBalance()>=hotelReserve.getAmountMoney()){
            hotelMember.setBalance(hotelMember.getBalance()-hotelReserve.getAmountMoney());
            hotelReserve.setStatus(1);
            //保存会员信息
            hotelMemberRepository.save(hotelMember);
            //保存订单信息
            hotelReserveRepository.save(hotelReserve);
        } else {
            throw new BadRequestException("付款失败，余额不足");
        }

    }

    //登记入住
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkin(HotelCheckin hotelCheckin) {
        //根据订单号查找订单信息
        HotelReserve hotelReserve = hotelReserveRepository.findByOrderNumber(hotelCheckin.getOrderNumber());
        HotelRoom hotelRoom = hotelRoomRepository.findById(hotelReserve.getRoomId()).orElseGet(HotelRoom::new);
        if(null == hotelReserve){
            throw new BadRequestException("订单为空或不存在");
        }else {
            hotelReserve.setStatus(2);//2表示已入住待退房
            hotelReserveRepository.save(hotelReserve);
            //保存到入住信息表
            hotelCheckin.setRoomId(hotelReserve.getRoomId());
            hotelCheckin.setStatus(0);
            hotelCheckin.setAmountMoney(hotelReserve.getAmountMoney());
            hotelCheckin.setCheckinDate(hotelReserve.getCheckinDate());
            hotelRoom.setRoomStatus(2);//表示已入住 2
            hotelRoomRepository.save(hotelRoom);
            hotelCheckinRepository.save(hotelCheckin);
        }

    }

    @Override
    public Set<LocalDate> getDatesByRoomId(Long id) {
        Set<LocalDate> localDateSet = new HashSet<>();
        List<HotelReserve> list = hotelReserveRepository.findAllByRoomId(id);
        list.stream().forEach(item->{
            XuedenUtil.getMiddleDate(item.getCheckinDate(),item.getCheckoutDate()).stream().forEach(localDate -> {
                localDateSet.add(localDate);
            });
        });
        return localDateSet;
    }

    //预订房间
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReserve(HotelReserve hotelReserve) {
        Long reserveDays = 1L;
        reserveDays = XuedenUtil.LocalDateBetween(hotelReserve.getCheckinDate(),hotelReserve.getCheckoutDate());
        if(reserveDays.intValue()==0){
            throw new BadRequestException("预定失败，入住时间和退房时间不能为同一天");
        } else {
            hotelReserve.setStatus(0);
            //预定天数
            hotelReserve.setReserveDays(reserveDays);
            HotelRoom hotelRoom = hotelRoomRepository.getReferenceById(hotelReserve.getRoomId());
            HotelRoomType hotelRoomType = hotelRoomTypeRepository.getById(hotelRoom.getRoomTypeId());
            hotelRoomType.setSum(hotelRoomType.getSum()+1);
            hotelRoom.setRoomStatus(1);//房间状态变为1，为已预定。
            hotelRoomRepository.save(hotelRoom);
            hotelRoomTypeRepository.save(hotelRoomType);
            int money = 0;
            money = reserveDays.intValue()*hotelRoom.getMemberPrice();
            hotelReserve.setAmountMoney(money);
            hotelReserve.setOrderNumber(XuedenUtil.createOrderNumber());
            hotelReserveRepository.save(hotelReserve);
        }
    }


    @Override
    public Object getMyReserveList(ReserveQueryCriteria reserveQueryCriteria, Pageable pageable) {
        Page<HotelReserve> page = hotelReserveRepository.findAll((root,query,criteriaBuilder)->
                QueryHelp.getPredicate(root,reserveQueryCriteria,criteriaBuilder),pageable);
        setRoomToReserve(page.stream().toList());
        return PageUtil.toPage(page);
    }
}
