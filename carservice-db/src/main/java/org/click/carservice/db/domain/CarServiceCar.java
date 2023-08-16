package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.click.carservice.db.validator.order.Order;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户车牌信息对象 car_service_car
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("car_service_car")
@ApiModel(value = "用户车牌信息", description = "用户车牌信息")
public class CarServiceCar implements Serializable {

    private static final long serialVersionUID=1L;

    /** 车牌信息表id */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /** 用户id */
    @ApiModelProperty("用户id")
    private String userId;

    /** 车牌号 */
    @ApiModelProperty("车牌号")
    private String carNumber;

    /** 车辆类型(0:轿车，1:SUV, 2:MPV, 3:其他) */
    @ApiModelProperty("车辆类型(0:轿车，1:SUV, 2:MPV, 3:其他)")
    private String carType;

    /** 动力类型(0:燃油车,1:新能源车) */
    @ApiModelProperty("动力类型(0:燃油车,1:新能源车)")
    private String engineType;

    /** 车龄 */
    @ApiModelProperty("车龄")
    private Long carAge;

    /** 车型 */
    @ApiModelProperty("车型")
    private String carModel;

    /** 车辆性质(运营/非运营) */
    @ApiModelProperty("车辆性质(0:运营/1:非运营)")
    private String carProperties;

    /** 是否默认车牌号（0：是，1：否） */
    @ApiModelProperty("是否默认车牌号（0：是，1：否）")
    private Integer isDefault;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @Order
    private Date addTime;

    /** 修改时间 */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /** 逻辑删除 */
    @ApiModelProperty("逻辑删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /** 租户ID，用于分割多个租户 */
    @ApiModelProperty("租户ID，用于分割多个租户")
    private String tenantId;

    /** 更新版本号 */
    @ApiModelProperty("更新版本号")
    private Long version;

}