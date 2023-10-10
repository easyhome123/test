package cn.xueden.hotel.service.impl;

import cn.xueden.hotel.domain.HotelRechargeRecord;
import cn.xueden.hotel.repository.HotelRechargeRecordRepository;
import cn.xueden.hotel.service.IHotelRechargeRecordService;
import cn.xueden.hotel.service.dto.RechargeRecordQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//充值记录业务
@Service
@Transactional(readOnly = true)
public class HotelRechargeRecordServiceImpl implements IHotelRechargeRecordService {

    private final HotelRechargeRecordRepository hotelRechargeRecordRepository;

    public HotelRechargeRecordServiceImpl(HotelRechargeRecordRepository hotelRechargeRecordRepository) {
        this.hotelRechargeRecordRepository = hotelRechargeRecordRepository;
    }


    //获取我的充值记录
    @Override
    public Object getMyRechargeRecordList(RechargeRecordQueryCriteria queryCriteria, Pageable pageable) {
        Page<HotelRechargeRecord> page = hotelRechargeRecordRepository.findAll((root,query,criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }
}
