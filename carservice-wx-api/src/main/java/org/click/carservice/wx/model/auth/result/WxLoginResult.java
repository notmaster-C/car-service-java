package org.click.carservice.wx.model.auth.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.click.carservice.db.entity.UserInfo;

import java.io.Serializable;

/**
 * 登录返回参数
 *
 * @author click
 */
@Data
@ApiModel(value = "登录返回参数", description = "登录返回参数")
public class WxLoginResult implements Serializable {

    /**
     * 用户token
     */
    @ApiModelProperty("用户token")
    private String userToken;

    /**
     * 用户信息
     */
    @ApiModelProperty("用户信息")
    private UserInfo userInfo;

}
