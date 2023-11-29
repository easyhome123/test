package cn.xueden.hotel.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.exception.BadRequestException;
import cn.xueden.hotel.domain.HotelMember;
import cn.xueden.hotel.domain.HotelRechargeRecord;
import cn.xueden.hotel.repository.HotelMemberRepository;
import cn.xueden.hotel.repository.HotelRechargeRecordRepository;
import cn.xueden.hotel.service.IHotelMemberService;
import cn.xueden.hotel.service.dto.MemberQueryCriteria;
import cn.xueden.hotel.vo.MemberPwdModel;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HotelMemberServiceImpl implements IHotelMemberService {

    private final HotelMemberRepository hotelMemberRepository;

    private final  HotelRechargeRecordRepository hotelRechargeRecordRepository;
    public HotelMemberServiceImpl(HotelMemberRepository hotelMemberRepository, HotelRechargeRecordRepository hotelRechargeRecordRepository) {
        this.hotelMemberRepository = hotelMemberRepository;
        this.hotelRechargeRecordRepository = hotelRechargeRecordRepository;
    }

    @Override
    public Object getList(MemberQueryCriteria queryCriteria, Pageable pageable) {

        Page<HotelMember> page =  hotelMemberRepository.findAll((root, query, criteriaBuilder)-> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public HotelMember getById(Long id) {
        return hotelMemberRepository.findById(id).orElseGet(HotelMember::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recharge(HotelMember hotelMember) {
        HotelMember hotelMember1 = hotelMemberRepository.getReferenceById(hotelMember.getId());
        if(hotelMember1 == null){
            throw new BadRequestException("充值失败");
        }else {
            hotelMember1.setBalance(hotelMember.getBalance() == null ? hotelMember.getBalance(): hotelMember1.getBalance()+hotelMember.getBalance());
            hotelMemberRepository.save(hotelMember1);
            //保存到充值记录表中
            HotelRechargeRecord hotelRechargeRecord = new HotelRechargeRecord();
            hotelRechargeRecord.setMoney(hotelMember.getBalance());
            hotelRechargeRecord.setMemberId(hotelMember.getId());
            hotelRechargeRecord.setRemarks(hotelMember.getRemarks());
            hotelRechargeRecordRepository.save(hotelRechargeRecord);
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        hotelMemberRepository.deleteById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(HotelMember hotelMember) {
        HotelMember hotelMember1 = hotelMemberRepository.findByPhone(hotelMember.getPhone());
        if(hotelMember1!= null){
            throw  new BadRequestException("注册失败，该手机号已注册");
        }else {
            hotelMember.setStatus(1);
            hotelMember.setLoginTimes(0);
            hotelMember.setBalance(0);
            hotelMemberRepository.save(hotelMember);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HotelMember login(HotelMember hotelMember) {
        HotelMember hotelMember1 = hotelMemberRepository.findByPhone(hotelMember.getPhone());
        if(hotelMember1==null){
            throw new BadRequestException("登陆失败，手机号"+hotelMember.getPhone()+"未注册");
        }else if(!hotelMember1.getPassword().equals(hotelMember.getPassword())){
            throw new BadRequestException("登陆失败,输入密码不正确");
        }else {
            hotelMember1.setLoginTimes(hotelMember1.getLoginTimes()+1);
            hotelMemberRepository.save(hotelMember1);
        }
        return hotelMember1;
    }

    //获取等会员信息
    @Override
    public HotelMember getLoginMemberInfo(Long memberId) {
        return hotelMemberRepository.findById(memberId).orElseGet(HotelMember::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMember(HotelMember hotelMember) {
        HotelMember hotelMember1 = hotelMemberRepository.getReferenceById(hotelMember.getId());
        BeanUtil.copyProperties(hotelMember,hotelMember1,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        hotelMemberRepository.save(hotelMember1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMemberPwd(MemberPwdModel memberPwdModel) {
        HotelMember hotelMember = hotelMemberRepository.getReferenceById(memberPwdModel.getMemberId());
        if(!hotelMember.getPassword().equals(memberPwdModel.getOldPassword())){
            throw new BadRequestException("修改密码失败,旧密码错误");
        } else {
            hotelMember.setPassword(memberPwdModel.getNewPassword());
            hotelMemberRepository.save(hotelMember);
        }
    }



}
