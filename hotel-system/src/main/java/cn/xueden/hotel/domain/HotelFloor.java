package cn.xueden.hotel.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

//楼层实体类
@Entity
@Data
@Table(name = "hotel_floor")
@org.hibernate.annotations.Table(appliesTo = "hotel_floor",comment = "酒店楼层信息表")
public class HotelFloor extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "floor_no",nullable = false)
    private Integer floorNo;

    @Column(name = "floor_name")
    private String floorName;

    //房间列表
    @Transient
    private List<HotelRoom> roomList;

}


