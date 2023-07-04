package org.ysling.litemall.wx.model.auth.body;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ysling
 */
@Data
public class AuthLoginBody implements Serializable {

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

}
