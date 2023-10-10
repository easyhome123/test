package cn.xueden.hotel.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.exception.BadRequestException;
import cn.xueden.hotel.domain.HotelFloor;
import cn.xueden.hotel.domain.HotelRoom;
import cn.xueden.hotel.domain.HotelRoomType;
import cn.xueden.hotel.repository.HotelFloorRepository;
import cn.xueden.hotel.repository.HotelRoomRepository;
import cn.xueden.hotel.repository.HotelRoomTypeRepository;
import cn.xueden.hotel.service.IHotelRoomService;
import cn.xueden.hotel.service.dto.RoomQueryCriteria;
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
public class HotelRoomServiceImpl implements IHotelRoomService {

    private final HotelRoomRepository hotelRoomRepository;

    private final HotelRoomTypeRepository hotelRoomTypeRepository;

    private  final HotelFloorRepository hotelFloorRepository;

    public HotelRoomServiceImpl(HotelRoomRepository hotelRoomRepository, HotelRoomTypeRepository hotelRoomTypeRepository, HotelFloorRepository hotelFloorRepository) {
        this.hotelRoomRepository = hotelRoomRepository;
        this.hotelRoomTypeRepository = hotelRoomTypeRepository;
        this.hotelFloorRepository = hotelFloorRepository;
    }

    //获取房间列表数据

    @Override
    public Object getList(RoomQueryCriteria queryCriteria, Pageable pageable) {
        Page<HotelRoom> page =  hotelRoomRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        setFloorAndTypeToRoom(page.stream().toList());
        return PageUtil.toPage(page);
    }

    public List<HotelRoom> setFloorAndTypeToRoom(List<HotelRoom> hotelRooms) {
        for( HotelRoom r : hotelRooms){
            if( r.getFloorId()!=null && r.getFloorId()!= 0 ){
                HotelFloor hotelFloor = hotelFloorRepository.findById(r.getFloorId()).orElseGet(HotelFloor::new);
                if(StringUtils.isNoneBlank(hotelFloor.getFloorName())){
                    r.setHotelFloor(hotelFloor);
                }
            }
            if( r.getRoomTypeId()!= null && r.getRoomTypeId()!=0){
                HotelRoomType hotelRoomType = hotelRoomTypeRepository.findById(r.getRoomTypeId()).orElseGet(HotelRoomType::new);
                if(StringUtils.isNoneBlank(hotelRoomType.getTypeName())){
                    r.setHotelRoomType(hotelRoomType);
                }
            }
        }
        return hotelRooms;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRoom(HotelRoom hotelRoom) {
        HotelRoom hotelRoom1 = hotelRoomRepository.findByRoomNumber(hotelRoom.getRoomNumber());
        if(hotelRoom1 != null){
            throw new BadRequestException("添加失败，房间号已存在");
        }else {
            hotelRoom.setRoomStatus(0);
            hotelRoomRepository.save(hotelRoom);
        }

    }

    @Override
    public HotelRoom getById(Long id) {
        return hotelRoomRepository.findById(id).orElseGet(HotelRoom::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editRoom(HotelRoom hotelRoom) {
        HotelRoom hotelRoom1 = hotelRoomRepository.findByRoomNumber(hotelRoom.getRoomNumber());
        if( hotelRoom1 != null && !hotelRoom1.getId().equals(hotelRoom.getId())){
            throw new BadRequestException("更新失败，房间号已存在");
        } else {
            hotelRoom1 = hotelRoomRepository.getReferenceById(hotelRoom.getId());
            BeanUtil.copyProperties(hotelRoom,hotelRoom1, CopyOptions.create().setIgnoreError(true).setIgnoreNullValue(true));
            hotelRoomRepository.save(hotelRoom1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        hotelRoomRepository.deleteById(id);
    }

    //根据楼层ID，获取房间列表
    @Override
    public List<HotelRoom> getRoomListByFloorId(Long id, int num) {
        return hotelRoomRepository.getRoomListByFloorId(id,num);
    }
}
