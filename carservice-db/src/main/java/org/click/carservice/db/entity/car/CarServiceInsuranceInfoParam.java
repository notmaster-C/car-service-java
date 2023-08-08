package org.click.carservice.db.entity.car;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.click.carservice.db.domain.CarServiceInsuranceInfo;

import java.util.Date;

/**
 * @title: CarServiceInsuranceInfoParam
 * @Author HuangYan
 * @Date: 2023/8/7 23:40
 * @Version 1.0
 * @Description: 保单信息查询实体
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("保单信息查询实体")
public class CarServiceInsuranceInfoParam {

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
    private Date insureTime;

    /** 投保结束时间 */
    @ApiModelProperty("投保结束时间")
    private Date insureEndTime;

}
