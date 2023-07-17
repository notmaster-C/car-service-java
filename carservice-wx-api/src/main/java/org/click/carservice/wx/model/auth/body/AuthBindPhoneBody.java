package org.click.carservice.wx.model.auth.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 微信绑定手机
 *
 * @author click
 */
@Data
public class AuthBindPhoneBody implements Serializable {

    /**
     * 密钥
     */
    @NotNull(message = "密钥不能为空")
    private String iv;

    /**
     * 密钥
     */
    @NotNull(message = "密钥不能为空")
    private String encryptedData;

}
