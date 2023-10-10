package cn.xueden.hotel.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "hotel_checkin")
@org.hibernate.annotations.Table(appliesTo = "hotel_checkin",comment = "入住登记表")
public class HotelCheckin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" ,nullable = false)
    private Long id;

    //订单编号
    @Column(name = "ordername",nullable = false)
    private String orderNumber;

    //房间号
    @Column(name = "roomid",nullable = false)
    private Long roomId;

    //会员名称
    @Column(name = "membername",nullable = false )
    private String memberName;

    //身份证
    @Column(name = "idcard",nullable = false)
    private String idCard;

    //入住人数
    @Column(name = "membernumber",nullable = false)
    private Integer memberNumber;

    //出生日期
    @Column(name = "birthday")
    private String birthday;

    //性别
    @Column(name = "gender")
    private String gender;

    //省份
    @Column(name = "province")
    private String province;

    //城市
    @Column(name = "city")
    private String city;

    //家庭住址
    @Column(name = "address")
    private String address;

    //手机号
    @Column(name = "phone",nullable = false)
    private String phone;

    //入住状态，0：已入住未退房，1：已退房
    @Column(name = "status",nullable = false)
    private  Integer status;

    //入住时间
    @Column(name = "checkindate",nullable = false)
    private LocalDate checkinDate;

    //退房时间
    @Column(name = "checkoutdate")
    private LocalDate checkoutDate;

    //总金额
    @Column(name = "amountMoney",nullable = false)
    private Integer amountMoney;

    //房间
    @Transient
    private HotelRoom hotelRoom;
}
