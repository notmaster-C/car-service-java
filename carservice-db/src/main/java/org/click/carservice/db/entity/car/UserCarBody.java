package org.click.carservice.db.entity.car;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * @title: UserCarBody
 * @Author HuangYan
 * @Date: 2023/8/2 18:54
 * @Version 1.0
 * @Description: 用户-车辆 请求体
 */
@Data
@ApiModel("用户-车辆请求体")
public class UserCarBody extends PageBody implements Serializable {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("车牌号")
    private String carNumber;

    /** 车辆类型(0:轿车，1:SUV, 2:MPV, 3:其他) */
    @ApiModelProperty("车辆类型(0:轿车，1:SUV, 2:MPV, 3:其他)")
    private String carType;

    /** 动力类型(0:燃油车,1:新能源车) */
    @ApiModelProperty("动力类型(0:燃油车,1:新能源车)")
    private String engineType;

}
