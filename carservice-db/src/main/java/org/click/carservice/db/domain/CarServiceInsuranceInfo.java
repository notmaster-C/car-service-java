package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 保单信息对象 car_service_insurance_info
 *
 * @author huangYan
 * @date 2023-08-04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("car_service_insurance_info")
@ApiModel(value = "保单信息", description = "保单信息")
public class CarServiceInsuranceInfo {

    private static final long serialVersionUID=1L;

    /** 保单表id */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /** 用户id */
    @ApiModelProperty("用户id")
    private String userId;

    /** 车牌号id */
    @ApiModelProperty("车牌号id")
    private String carId;

    /** 投保人 */
    @ApiModelProperty("投保人")
    private String insureUser;

    /** 投保人电话 */
    @ApiModelProperty("投保人电话")
    private String insureUserPhone;

    /** 投保单业务流水号 */
    @ApiModelProperty("投保单业务流水号")
    private String insureNum;

    /** 投保分公司名称 */
    @ApiModelProperty("投保分公司名称")
    private String insureCompany;

    /** 投保时间 */
    @ApiModelProperty("投保时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insureTime;

    /** 投保结束时间 */
    @ApiModelProperty("投保结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insureEndTime;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    private Date addTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    /** 逻辑删除 */
    @ApiModelProperty("逻辑删除")
    private Integer deleted;

    /** 租户ID，用于分割多个租户 */
    @ApiModelProperty("租户ID，用于分割多个租户")
    private String tenantId;

    /** 更新版本号 */
    @ApiModelProperty("更新版本号")
    private Long version;

    /** 保险信息服务项信息 */
    @TableField(exist = false)
    @ApiModelProperty("保险信息服务项信息")
    private List<CarServiceInsuranceService> carServiceInsuranceServiceList;

}