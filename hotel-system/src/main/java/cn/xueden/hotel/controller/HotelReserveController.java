package cn.xueden.hotel.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.exception.BadRequestException;
import cn.xueden.hotel.domain.HotelCheckin;
import cn.xueden.hotel.domain.HotelMember;
import cn.xueden.hotel.domain.HotelReserve;
import cn.xueden.hotel.service.IHotelReserveService;
import cn.xueden.hotel.service.dto.ReserveQueryCriteria;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//预定模块
@RestController
@RequestMapping("reserve")
public class HotelReserveController {
    private final IHotelReserveService hotelReserveService;

    public HotelReserveController(IHotelReserveService hotelReserveService) {
        this.hotelReserveService = hotelReserveService;
    }

    @EnableSysLog("获取预定列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(ReserveQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(hotelReserveService.getList(queryCriteria,pageable), HttpStatus.OK);
    }
    @EnableSysLog("根据id获取详情信息")
    @GetMapping("/{id}")
    public BaseResult deatil(@PathVariable Long id){
        if(null == id){
            throw new BadRequestException("获取信息失败");
        }else {
            HotelReserve hotelReserve =hotelReserveService.getById(id);
            return BaseResult.success(hotelReserve);
        }
    }

    @EnableSysLog("退订房间")
    @PutMapping("/unsubscribe/{id}")
    public BaseResult unsubscribe(@PathVariable Long id){
        if(null == id){
             return BaseResult.fail("获取信息失败");
        }else {
            hotelReserveService.unsubscribe(id);
            return BaseResult.success("退订成功");
        }

    }
    @EnableSysLog("确认收款")
    @PutMapping("/payment/{id}")
    public BaseResult payment(@PathVariable Long id){
        if(null == id){
            return BaseResult.fail("获取信息失败");
        }else {
            hotelReserveService.payment(id);
            return BaseResult.success("收款成功");
        }
    }

    @EnableSysLog("登记入住酒店")
    @PostMapping("checkin")
    public BaseResult checkin(@RequestBody HotelCheckin hotelCheckin){
        hotelReserveService.checkin(hotelCheckin);
        return BaseResult.success("登记成功");
    }
}
