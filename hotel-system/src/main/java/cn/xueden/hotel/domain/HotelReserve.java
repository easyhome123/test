package cn.xueden.hotel.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

//酒店预定实体类
@Entity
@Data
@Table(name = "hotel_reserve")
@org.hibernate.annotations.Table(appliesTo = "hotel_reserve",comment = "酒店预定信息表")
public class HotelReserve extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    //订单号
    @Column(name = "order_number",nullable = false)
    private String orderNumber;

    //房间号
    @Column(name = "room_id" ,nullable = false)
    private Long roomId;

    //订单状态
    @Column(name = "status" ,nullable = false)
    private Integer status;

    //入住时间
    @Column(name = "checkin_date",nullable = false)
    private LocalDate checkinDate;

    @Column(name = "reserve_days",nullable = false)
    private Long reserveDays;

    //退房时间
    @Column(name = "checkout_date",nullable = false)
    private LocalDate checkoutDate;

    @Column(name = "amount_money",nullable = false)
    private Integer amountMoney;

    @Transient
    private HotelRoom hotelRoom;

    //会员信息
    @Transient
    private  HotelMember hotelMember;


}
