package cn.xueden.hotel.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

//楼层查询
@Data
public class FloorQueryCriteria {
    @EnableXuedenQuery(blurry = "floorNo,floorName")
    private  String searchValue;
}
