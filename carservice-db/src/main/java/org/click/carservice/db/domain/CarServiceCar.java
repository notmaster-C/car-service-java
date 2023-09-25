package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户车牌信息对象 car_service_car
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("car_service_car")
@ApiModel(value = "用户车牌信息", description = "用户车牌信息")
public class CarServiceCar implements Serializable {

    private static final long serialVersionUID=1L;

    /** 车牌信息表id */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /** 用户id */
    @TableField("`user_id`")
    @ApiModelProperty("用户id")
    private String userId;

    /** 车牌号 */
    @ApiModelProperty("车牌号")
    @TableField("`car_number`")
    private String carNumber;

    /** 车辆类型(0:轿车，1:SUV, 2:MPV, 3:其他) */
    @ApiModelProperty("车辆类型(0:轿车，1:SUV, 2:MPV, 3:其他)")
    @TableField("`car_type`")
    private String carType;

    /** 动力类型(0:燃油车,1:新能源车) */
    @ApiModelProperty("动力类型(0:燃油车,1:新能源车)")
    @TableField("`engine_type`")
    private String engineType;

    /** 车龄 */
    @ApiModelProperty("车龄")
    @TableField("`car_age`")
    private Long carAge;

    /** 车型 */
    @ApiModelProperty("车型")
    @TableField("`car_model`")
    private String carModel;

    /** 车辆性质(运营/非运营) */
    @ApiModelProperty("车辆性质(0:运营/1:非运营)")
    @TableField("`car_properties`")
    private String carProperties;

    /** 是否默认车牌号（0：是，1：否） */
    @ApiModelProperty("是否默认车牌号（0：是，1：否）")
    @TableField("`is_default`")
    private Integer isDefault;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @TableField(value="`add_time`", fill = FieldFill.INSERT)
    private LocalDateTime addTime;

    /** 修改时间 */
    @ApiModelProperty("修改时间")
    @TableField(value="`update_time`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除 */
    @ApiModelProperty("逻辑删除")
    @TableLogic(value = "0", delval = "1")
    @TableField("`deleted`")
    private Integer deleted;

    /** 租户ID，用于分割多个租户 */
    @ApiModelProperty("租户ID，用于分割多个租户")
    @TableField("`tenant_id`")
    private String tenantId;

    /** 更新版本号 */
    @ApiModelProperty("更新版本号")
    @TableField("`version`")
    private Long version;

    /////////////////////////////////
    // 数据库字段常量
    ////////////////////////////////

    /** 车牌信息表id */
    public static final String ID="`id`";

    /** 用户id */
    public static final String USER_ID="`user_id`";

    /** 车牌号 */
    public static final String CAR_NUMBER="`car_number`";

    /** 车辆类型(0:轿车，1:SUV, 2:MPV, 3:其他) */
    public static final String CAR_TYPE="`car_type`";

    /** 动力类型(0:燃油车,1:新能源车) */
    public static final String ENGINE_TYPE="`engine_type`";

    /** 车龄 */
    public static final String CAR_AGE="`car_age`";

    /** 车型 */
    public static final String CAR_MODEL="`car_model`";

    /** 车辆性质(运营/非运营) */
    public static final String CAR_PROPERTIES="`car_properties`";

    /** 是否默认车牌号（0：是，1：否） */
    public static final String IS_DEFAULT="`is_default`";

    /** 创建时间 */
    public static final String ADD_TIME="`add_time`";

    /** 修改时间 */
    public static final String UPDATE_TIME="`update_time`";

    /** 逻辑删除 */
    public static final String DELETED="`deleted`";

    /** 租户ID，用于分割多个租户 */
    public static final String TENANT_ID="`tenant_id`";

    /** 更新版本号 */
    public static final String VERSION="version";

}
