package cn.xueden.hotel.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

//房间类型实体类
@Entity
@Data
@Table(name = "hotel_room_type")
@org.hibernate.annotations.Table(appliesTo = "hotel_room_type",comment = "房间类型信息表")
public class HotelRoomType extends BaseEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private  Long id;

    //房间类型
    @Column(name = "type_name",nullable = false)
    private String typeName;

    //排序
    @Column(name = "type_sort",nullable = false)
    private Integer typeSort;

    @Column(name = "sum",nullable = false)
    private Integer sum;
}
