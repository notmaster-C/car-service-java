package org.ysling.litemall.admin.model.auth.body;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CaptchaBody implements Serializable {

    /**
     * 账号
     */
    @NotNull(message = "账号不能为空")
    private String username;

    /**
     * 租户appid
     */
    private String appid;

}
