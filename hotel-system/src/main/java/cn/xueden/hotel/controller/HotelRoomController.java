package cn.xueden.hotel.controller;


import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.exception.BadRequestException;
import cn.xueden.hotel.domain.HotelRoom;
import cn.xueden.hotel.service.IHotelRoomService;
import cn.xueden.hotel.service.dto.RoomQueryCriteria;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("room")
public class HotelRoomController {
    private final IHotelRoomService hotelRoomService;

    public HotelRoomController(IHotelRoomService hotelRoomService) {
        this.hotelRoomService = hotelRoomService;
    }

    @EnableSysLog("获取房间列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(RoomQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(hotelRoomService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    @EnableSysLog("添加房间信息")
    @PostMapping
    public BaseResult addRoom(@RequestBody HotelRoom hotelRoom){
        hotelRoomService.addRoom(hotelRoom);
        return BaseResult.success("添加成功");

    }

    @EnableSysLog("根据id获取房间信息id")
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null == id){
            throw new BadRequestException("获取信息失败");
        }else {
            HotelRoom hotelRoom = hotelRoomService.getById(id);
            return BaseResult.success(hotelRoom);
        }
    }

    @EnableSysLog("编辑房间信息")
    @PutMapping
    public  BaseResult editRoom(@RequestBody HotelRoom hotelRoom){
        hotelRoomService.editRoom(hotelRoom);
        return BaseResult.success("更新成功");
    }

    @EnableSysLog("删除房间信息")
    @DeleteMapping("/{id}")
    public BaseResult delRoom(@PathVariable Long id){
        if(null == id){
            throw  new BadRequestException("删除信息失败");
        } else {
            hotelRoomService.deleteById(id);
            return BaseResult.success("删除成功");
        }

    }
}
