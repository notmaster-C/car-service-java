package org.click.carservice.admin.model.auth.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 登录请求参数
 *
 * @author click
 */
@Data
public class LoginBody implements Serializable {

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

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
     * 租户appid
     */
    private String appid;

}
