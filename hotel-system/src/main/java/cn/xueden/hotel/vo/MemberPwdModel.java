package cn.xueden.hotel.vo;

import lombok.Data;

//会员修改密码
@Data
public class MemberPwdModel {

    private String oldPassword;

    private String newPassword;


    private Long memberId;
}
