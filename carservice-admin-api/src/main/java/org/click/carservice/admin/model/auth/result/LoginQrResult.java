package org.click.carservice.admin.model.auth.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class LoginQrResult implements Serializable {

    /**
     *
     */
    private String authCode;
    /**
     * 二维码地址
     */
    private String authQrPath;
}
