package cn.xueden.hotel.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.exception.BadRequestException;
import cn.xueden.hotel.domain.HotelMember;
import cn.xueden.hotel.service.IHotelMemberService;
import cn.xueden.hotel.service.dto.MemberQueryCriteria;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("member")
public class HotelMemberController {

    private  final IHotelMemberService hotelMemberService;

    public HotelMemberController(IHotelMemberService hotelMemberService) {
        this.hotelMemberService = hotelMemberService;
    }

    @EnableSysLog("获取会员列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(MemberQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(), Sort.Direction.DESC,"id");
        return new ResponseEntity<>(hotelMemberService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    @EnableSysLog("根据id获取详情")
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null == id){
            throw new BadRequestException("获取信息失败");
        }else {
            HotelMember hotelMember = hotelMemberService.getById(id);
            return BaseResult.success(hotelMember);
        }

    }

    @EnableSysLog("会员充值")
    @PutMapping("/recharge")
    public BaseResult recharge(@RequestBody HotelMember hotelMember){
        hotelMemberService.recharge(hotelMember);
        return BaseResult.success("充值成功");
    }

    @EnableSysLog("删除id的会员信息")
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if(null == id){
            throw  new BadRequestException("删除信息失败");
        }
        hotelMemberService.deleteById(id);
        return  BaseResult.success("删除成功");
    }
}
