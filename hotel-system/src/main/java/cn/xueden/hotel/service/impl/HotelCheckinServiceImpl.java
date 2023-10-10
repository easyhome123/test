package cn.xueden.hotel.service.impl;

import cn.xueden.hotel.domain.HotelCheckin;
import cn.xueden.hotel.domain.HotelRoom;
import cn.xueden.hotel.repository.HotelCheckinRepository;
import cn.xueden.hotel.repository.HotelRoomRepository;
import cn.xueden.hotel.service.IHotelCheckinService;
import cn.xueden.hotel.service.dto.CheckinQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HotelCheckinServiceImpl implements IHotelCheckinService {

    private final HotelCheckinRepository hotelCheckinRepository;
    private final HotelRoomRepository hotelRoomRepository;

    public HotelCheckinServiceImpl(HotelCheckinRepository hotelCheckinRepository, HotelRoomRepository hotelRoomRepository) {
        this.hotelCheckinRepository = hotelCheckinRepository;
        this.hotelRoomRepository = hotelRoomRepository;
    }

    @Override
    public Object getList(CheckinQueryCriteria checkinQueryCriteria, Pageable pageable) {
        Page<HotelCheckin> page = hotelCheckinRepository.findAll(
                (root, query, criteriaBuilder)-> QueryHelp.getPredicate(root,checkinQueryCriteria,criteriaBuilder),pageable);
        setRoomToCheckin(page.stream().toList());
        return PageUtil.toPage(page);
    }

    //添加房间信息到入住列表中
    private List<HotelCheckin> setRoomToCheckin(List<HotelCheckin> hotelCheckins){
        for (HotelCheckin r: hotelCheckins){
            if(r.getRoomId()!=null&&r.getRoomId()!=0){
                HotelRoom hotelRoom = hotelRoomRepository.findById(r.getRoomId()).orElseGet(HotelRoom::new);
                if(StringUtils.isNoneBlank(hotelRoom.getRoomName())){
                    r.setHotelRoom(hotelRoom);
                }
            }
        }
        return hotelCheckins;
    }

    //退房
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkout(Long id) {
        hotelCheckinRepository.updateStatusById(id);
    }
}
