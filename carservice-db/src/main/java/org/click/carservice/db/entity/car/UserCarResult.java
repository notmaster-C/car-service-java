package org.click.carservice.db.entity.car;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.click.carservice.db.domain.CarServiceCar;

import java.io.Serializable;

/**
 * @title: UserCarResult
 * @Author HuangYan
 * @Date: 2023/8/2
 * @Version 1.0
 * @Description: 用户-车牌
 */
@Data
@ApiModel("用户-车牌")
public class UserCarResult extends CarServiceCar implements Serializable {

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("用户昵称")
    private String nickName;
    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String mobile;

}
