package cn.xueden.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.xueden.hotel.domain.HotelMember;
import cn.xueden.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**功能描述：JWT工具类

 */
public class HutoolJWTUtil {
    /**
     * 生成系统用户token
     * @param sysUser
     * @return
     */
    private static final  String key = "wangjian";
    public static String createToken(SysUser sysUser){
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE,120);
        Map<String,Object> payload = new HashMap<String,Object>();
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT,now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT,newTime);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE,now);
        // 载荷
        payload.put("username",sysUser.getUsername());
        payload.put("aid",sysUser.getId());
        String token = JWTUtil.createToken(payload,key.getBytes());
        return token;
    }

    //生成酒店会员tokrn
    public static String createToken(HotelMember hotelMember){
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE,120);
        Map<String,Object> payload = new HashMap<>();
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT,now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT,newTime);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE,now);
        //载荷
        payload.put("username",hotelMember.getMemberToken());
        payload.put("aid",hotelMember.getId());
        String token = JWTUtil.createToken(payload,key.getBytes());
        return token;

    }


    /**
     * 解析token
     * @param token
     * @return
     */

    public static Long parseToken(String token){
        final JWT jwt = JWTUtil.parseToken(token);
        String aid = jwt.getPayload("aid").toString();
        return Long.parseLong(aid);
    }

}
