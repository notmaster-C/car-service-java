package org.click.carservice.wx.model.auth.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author click
 */
@Data
public class AuthLoginByQrBody implements Serializable {

    /**
     * 授权码
     */
    @NotNull(message = "授权码不能为空")
    private String authCode;

}
