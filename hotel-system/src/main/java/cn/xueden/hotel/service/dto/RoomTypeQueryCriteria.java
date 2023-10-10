package cn.xueden.hotel.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

//房间类型查询条件类
@Data

public class RoomTypeQueryCriteria {

    //根据房间类型名称模糊查询
    @EnableXuedenQuery(blurry = "typeName")
    private String searchValue;


}
