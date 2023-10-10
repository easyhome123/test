package cn.xueden.hotel.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

@Data
public class RoomQueryCriteria {

    @EnableXuedenQuery(blurry = "roomNumber,roomName")
    private String searchValue;

    @EnableXuedenQuery
    private  Long floorId;

    @EnableXuedenQuery
    private Long roomTypeId;
}
