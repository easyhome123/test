package cn.xueden.hotel.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

//充值记录实体类
@Entity
@Data
@Table(name = "hotel_recharge_recode")
@org.hibernate.annotations.Table(appliesTo = "hotel_recharge_recode",comment = "充值记录表")
public class HotelRechargeRecord extends BaseEntity {

    //充值记录id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    //会员id
    @Column(name = "member_id",nullable = false)
    private Long memberId;

    @Column(name = "money",nullable = false)
    private Integer money;

}

