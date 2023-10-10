package cn.xueden.system.vo;

import lombok.Data;

/**
 */
@Data
public class ModifyPwdModel {
    /**
     * 旧密码
     */
    private String usedPass;

    /**
     * 新密码
     */
    private String newPass;

    /**
     * 用户ID
     */
    private Long userId;
}
