package cn.xueden.hotel.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.exception.BadRequestException;
import cn.xueden.hotel.domain.HotelFloor;
import cn.xueden.hotel.repository.HotelFloorRepository;
import cn.xueden.hotel.service.IHotelFloorService;
import cn.xueden.hotel.service.dto.FloorQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HotelFloorServiceImpl implements IHotelFloorService {

    private final HotelFloorRepository hotelFloorRepository;

    public HotelFloorServiceImpl(HotelFloorRepository hotelFloorRepository) {
        this.hotelFloorRepository = hotelFloorRepository;
    }

    @Override
    public Object getList(FloorQueryCriteria queryCriteria, Pageable pageable) {
        Page<HotelFloor> page = hotelFloorRepository.findAll(
                (root, query, criteriaBuilder)-> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFloor(HotelFloor hotelFloor) {
        HotelFloor hotelFloor1 = hotelFloorRepository.findByFloorNo(hotelFloor.getFloorNo());
        if(hotelFloor1 != null){
            throw  new BadRequestException("添加失败，楼层号已存在");
        }else {
            hotelFloorRepository.save(hotelFloor);
        }
    }

    @Override
    public HotelFloor getById(Long id) {
        return hotelFloorRepository.findById(id).orElseGet(HotelFloor::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editFloor(HotelFloor hotelFloor) {
        HotelFloor hotelFloor1 = hotelFloorRepository.getReferenceById(hotelFloor.getId());

        HotelFloor hotelFloor2 = hotelFloorRepository.findByFloorNo(hotelFloor.getFloorNo());
        if(hotelFloor2 != null && !hotelFloor2.getId().equals(hotelFloor.getId())){
            throw  new BadRequestException("更新失败");
        }else {
            BeanUtil.copyProperties(hotelFloor,hotelFloor1, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            hotelFloorRepository.save(hotelFloor1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        hotelFloorRepository.deleteById(id);
    }


    //获取楼层列表
    @Override

    public List<HotelFloor> getAll() {
        return hotelFloorRepository.findAll();
    }


}
