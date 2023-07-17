package org.click.carservice.wx.model.auth.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 密码重置
 *
 * @author click
 */
@Data
public class AuthResetBody implements Serializable {

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;
    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String mobile;
    /**
     * 验证码
     */
    @NotNull(message = "验证码不能为空")
    private String code;

}
