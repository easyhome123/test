package cn.xueden.hotel.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

//酒店会员实体类

@Entity
@Data
@Table(name = "hotel_member")
@org.hibernate.annotations.Table(appliesTo = "hotel_member",comment = "酒店会员信息表")
public class HotelMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    //会员名称
    @Column(name = "member_name")
    private String memberName;

    //会员真实姓名
    @Column(name = "real_name")
    private String realName;

    @Column(name = "member_phone",nullable = false)
    private String phone;

    //会员邮箱
    @Column(name = "member_email")
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    //会员登录次数
    @Column( name = "login_times")
    private Integer loginTimes;

    //会员状态
    @Column(name = "status" ,nullable = false)
    private Integer status;

    //会员头像
    @Column(name = "member_icon")
    private String memberIcon;

    @Column(name = "balance")
    private Integer balance;

    @Transient
    private String memberToken;
}

