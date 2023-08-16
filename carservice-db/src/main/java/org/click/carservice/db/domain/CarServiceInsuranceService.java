package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 保险信息服务项对象 car_service_insurance_service
 *
 * @author ruoyi
 * @date 2023-08-07
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("car_service_insurance_service")
@ApiModel(value = "保险信息服务项", description = "保险信息服务项")
public class CarServiceInsuranceService {

    private static final long serialVersionUID=1L;

    /** 保险服务项表id */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /** 保单信息表id */
    @ApiModelProperty("保单信息表id")
    private String insuranceInfoId;

    /** 服务名称 */
    @ApiModelProperty("服务名称")
    private String serviceName;

    /** 服务类型 */
    @ApiModelProperty("服务类型")
    private String serviceCode;

    /** 服务总次数 */
    @ApiModelProperty("服务总次数")
    private Long serviceTotal;

    /** 服务使用次数 */
    @ApiModelProperty("服务使用次数")
    private Long serviceUsed;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    private Date addTime;

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