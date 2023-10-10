package cn.xueden.hotel.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.exception.BadRequestException;
import cn.xueden.hotel.domain.HotelRoomType;
import cn.xueden.hotel.repository.HotelRoomTypeRepository;
import cn.xueden.hotel.service.IHotelRoomTypeService;
import cn.xueden.hotel.service.dto.RoomTypeQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//房间类型业务接口实现类
@Service
@Transactional(readOnly = true)
public class HotelRoomTypeServiceImpl implements IHotelRoomTypeService {

    private  final HotelRoomTypeRepository hotelRoomTypeRepository;


    public HotelRoomTypeServiceImpl(HotelRoomTypeRepository hotelRoomTypeRepository) {
        this.hotelRoomTypeRepository = hotelRoomTypeRepository;
    }

    //获取房间类型数据
    @Override
    public Object getList(RoomTypeQueryCriteria queryCriteria, Pageable pageable) {
      Page<HotelRoomType> page =  hotelRoomTypeRepository.findAll((root, query, criteriaBuilder)->
            QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    //新增房间类型
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRoomType(HotelRoomType hotelRoomType) {
       HotelRoomType dbhotelRoomType = hotelRoomTypeRepository.save(hotelRoomType);
       if(dbhotelRoomType.getId() == null || dbhotelRoomType.getId() == 0 ){
           throw  new  BadRequestException("新增房间类型失败");
       }

    }

    @Override
    public HotelRoomType getById(Long id) {
        return hotelRoomTypeRepository.findById(id).orElseGet(HotelRoomType::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editRoomType(HotelRoomType hotelRoomType) {
        HotelRoomType dbHotelRoomType = hotelRoomTypeRepository.getReferenceById(hotelRoomType.getId());
        BeanUtil.copyProperties(hotelRoomType,dbHotelRoomType, CopyOptions.create().setIgnoreError(true).setIgnoreNullValue(true));
        hotelRoomTypeRepository.save(dbHotelRoomType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        hotelRoomTypeRepository.deleteById(id);
    }


    //获取所有房间类型
    @Override
    public List<HotelRoomType> getAll() {
        return hotelRoomTypeRepository.findAll();
    }
}
