package org.ysling.litemall.wx.model.auth.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 账号注册请求参数
 * @author Ysling
 */
@Data
public class AuthRegisterBody implements Serializable {

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String mobile;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 验证码
     */
    @NotNull(message = "验证码不能为空")
    private String code;

    /**
     * 微信授权（非必填）
     */
    private String wxCode;

}
