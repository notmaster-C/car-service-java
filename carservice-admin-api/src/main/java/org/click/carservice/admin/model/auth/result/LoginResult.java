package org.click.carservice.admin.model.auth.result;

import lombok.Data;
import org.click.carservice.db.entity.AdminInfo;

import java.io.Serializable;

/**
 * 登录响应实体
 *
 * @author click
 */
@Data
public class LoginResult implements Serializable {

    /**
     * 管理员token
     */
    private String token;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 管理员信息
     */
    private AdminInfo adminInfo;

}
