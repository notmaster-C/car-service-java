package org.click.carservice.wx.model.auth.result;

import lombok.Data;
import org.click.carservice.db.entity.AdminInfo;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class LoginResult implements Serializable {

    /**
     * 管理员信息
     */
    private AdminInfo adminInfo;

    /**
     * 管理员token
     */
    private String adminToken;


}
