package cn.xueden.hotel.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.hotel.domain.*;
import cn.xueden.hotel.service.*;
import cn.xueden.hotel.service.dto.RechargeRecordQueryCriteria;
import cn.xueden.hotel.service.dto.ReserveQueryCriteria;
import cn.xueden.hotel.service.dto.RoomQueryCriteria;
import cn.xueden.hotel.vo.MemberPwdModel;
import cn.xueden.utils.HutoolJWTUtil;
import cn.xueden.utils.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//酒店前端首页
@RestController
@RequestMapping("hotel")
public class IndexController {

    private final IHotelRoomTypeService hotelRoomTypeService;
    private final IHotelFloorService hotelFloorService;

    private final IHotelRoomService hotelRoomService;

    private final IHotelMemberService hotelMemberService;

    private final IHotelReserveService hotelReserveService;

    private final IHotelRechargeRecordService hotelRechargeRecordService;



    public IndexController(IHotelRoomTypeService hotelRoomTypeService, IHotelFloorService hotelFloorService, IHotelRoomService hotelRoomService, IHotelMemberService hotelMemberService, IHotelReserveService hotelReserveService, IHotelRechargeRecordService hotelRechargeRecordService) {
        this.hotelRoomTypeService = hotelRoomTypeService;
        this.hotelFloorService = hotelFloorService;
        this.hotelRoomService = hotelRoomService;
        this.hotelMemberService = hotelMemberService;
        this.hotelReserveService = hotelReserveService;
        this.hotelRechargeRecordService = hotelRechargeRecordService;
    }

    @EnableSysLog("获取酒店前台数据")
    @GetMapping("getIndex")
    public BaseResult getIndex(){

        //获取房间类型
        HashMap<String,Object> resultMap = new HashMap<>();
        List<HotelRoomType> hotelRoomTypeList = hotelRoomTypeService.getAll();
        resultMap.put("indexRoomTypeList",hotelRoomTypeList);

        //h获取首页楼层信息
        List<HotelFloor> hotelFloorList = hotelFloorService.getAll();

        setRoomToFloor(hotelFloorList);
        resultMap.put("hotelFloorList",hotelFloorList);
        return BaseResult.success(resultMap);
    }
    //每个楼层获取房间
    private List<HotelFloor> setRoomToFloor(List<HotelFloor> hotelFloors){
        for(HotelFloor r:hotelFloors){
            if(r.getId()!=null){
                List<HotelRoom> hotelRooms = hotelRoomService.getRoomListByFloorId(r.getId(),3);
                r.setRoomList(hotelRooms);
            }
        }
        return hotelFloors;
    }

    @EnableSysLog("获取楼层信息和房间类型")
    @GetMapping("getAllRoomTypeAndFloor")
    public  BaseResult  getAllRoomTypeAndFloor(){
        Map<String,Object> map = new HashMap<>();
        List<HotelRoomType> roomTypeList = hotelRoomTypeService.getAll();
        map.put("roomTypeList",roomTypeList);
        List<HotelFloor> floorList = hotelFloorService.getAll();
        map.put("floorList",floorList);
        return BaseResult.success(map);
    }

    @EnableSysLog("获取房间列表")
    @GetMapping("getRoomList")
    public BaseResult getRoomList(RoomQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(), Sort.Direction.DESC,"id");
        return BaseResult.success(hotelRoomService.getList(queryCriteria,pageable));
    }

    @EnableSysLog("会员注册")
    @PostMapping("register")
    public BaseResult register(@RequestBody HotelMember hotelMember){
        hotelMemberService.register(hotelMember);
        return BaseResult.success("注册成功");
    }

    @EnableSysLog("会员登陆")
    @PostMapping("login")
    public BaseResult login(@RequestBody HotelMember hotelMember){
        HotelMember hotelMember1 = hotelMemberService.login(hotelMember);
        String token = HutoolJWTUtil.createToken(hotelMember1);
        hotelMember1.setMemberToken(token);
        return BaseResult.success("登陆成功",hotelMember1);
    }

    @EnableSysLog("获取酒店详情页")
    @GetMapping("/detail/{id}")
    public BaseResult detail(@PathVariable Long id){
        HotelRoom hotelRoom = hotelRoomService.getById(id);
        HotelFloor hotelFloor = hotelFloorService.getById(hotelRoom.getFloorId());
        hotelRoom.setHotelFloor(hotelFloor);
        return BaseResult.success(hotelRoom);
    }

    @EnableSysLog("根据房间id获取预定日期数据")
    @GetMapping("getDatesByRoomId/{id}")
    public BaseResult getDatesByRoomId(@PathVariable Long id){
        Set<LocalDate> set = hotelReserveService.getDatesByRoomId(id);
        return BaseResult.success(set);
    }

    @EnableSysLog("会员预定房间")
    @PostMapping("addReserve")
    public BaseResult addReserve(@RequestBody HotelReserve hotelReserve,
                                 @RequestHeader("memberToken") String memberToken){
        if(StringUtils.isBlank(memberToken)){
            return BaseResult.fail("预定失败，请先登录");
        } else {
            Long memberId = HutoolJWTUtil.parseToken(memberToken);
            hotelReserve.setCreateBy(memberId);
            hotelReserveService.addReserve(hotelReserve);
            return BaseResult.success("预定成功");
        }
    }

    @EnableSysLog("获取登录会员信息")
    @GetMapping("getLoginMemberInfo")
    public BaseResult getLoginMemberInfo(@RequestHeader("memberToken") String memberToken){
        if(null == memberToken){
            return BaseResult.fail("获取失败，请先登录");
        } else {
            Long memberId = HutoolJWTUtil.parseToken(memberToken);
            HotelMember hotelMember = hotelMemberService.getLoginMemberInfo(memberId);
            return BaseResult.success(hotelMember);
        }
    }

    @EnableSysLog("更新会员信息")
    @PutMapping("updateMember")
    public BaseResult updateMember(@RequestBody HotelMember hotelMember){
        hotelMemberService.updateMember(hotelMember);
        return BaseResult.success("更新成功");
    }

    @EnableSysLog("获取我的预定列表")
    @GetMapping("getMyReserveList")
    public BaseResult getMyReserveList(ReserveQueryCriteria reserveQueryCriteria, PageVo pageVo ,@RequestHeader("memberToken") String memberToken){
        if(null == memberToken){
            return BaseResult.fail("获取预定列表失败");
        } else {
            Long memberId = HutoolJWTUtil.parseToken(memberToken);
            reserveQueryCriteria.setCreateBy(memberId);
            Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(),Sort.Direction.DESC,"id");
            return BaseResult.success(hotelReserveService.getMyReserveList(reserveQueryCriteria,pageable));
        }
    }

    @EnableSysLog("订单支付")
    @PutMapping("payment/{id}")
    public BaseResult payment(@PathVariable Long id){
        hotelReserveService.payment(id);
        return BaseResult.success("付款成功");
    }

    //查询充值记录
    @EnableSysLog("查询充值记录")
    @GetMapping("getMyRechargeRecordList")
    public BaseResult getMyRechargeRecordList(RechargeRecordQueryCriteria queryCriteria,PageVo pageVo,@RequestHeader("memberToken") String memberToken){
        if (null == memberToken ){
            return BaseResult.fail("查看充值记录失败，请先登录");
        } else {
            Long memberId = HutoolJWTUtil.parseToken(memberToken);
            Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(),Sort.Direction.DESC,"id");
            queryCriteria.setMemberId(memberId);
            return BaseResult.success(hotelRechargeRecordService.getMyRechargeRecordList(queryCriteria,pageable));
        }
    }

    @EnableSysLog("修改密码")
    @PutMapping("updateMemberPwd")
    public BaseResult updateMemberPwd(@RequestBody MemberPwdModel memberPwdModel,@RequestHeader("memberToken") String memberToken){
        if (null == memberToken ){
            return BaseResult.fail("修改失败，请先登录");
        } else {
            Long memberId = HutoolJWTUtil.parseToken(memberToken);
            memberPwdModel.setMemberId(memberId);
            hotelMemberService.updateMemberPwd(memberPwdModel);
            return BaseResult.success("修改密码成功");
        }

    }
}
