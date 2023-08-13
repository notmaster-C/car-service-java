package org.click.carservice.admin.model.insurance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.click.carservice.core.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @title: InsuranceInfoImportBody
 * @Author HuangYan
 * @Date: 2023/8/8 22:59
 * @Version 1.0
 * @Description: 保险信息导入实体
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "保单信息", description = "保单信息")
public class InsuranceInfoImportBody {

    /** ============================ 保单信息 =============================*/

    /** 用户注册电话 */
    @ApiModelProperty("用户注册电话")
    @Excel(name = "用户注册电话", needMerge = true)
    private String mobile;

    /** 车牌号id */
    @ApiModelProperty("车牌号")
    @Excel(name = "车牌号", needMerge = true)
    private String carNumber;

    /** 投保人 */
    @ApiModelProperty("投保人")
    @Excel(name = "投保人", needMerge = true)
    private String insureUser;

    /** 投保人电话 */
    @ApiModelProperty("投保人电话")
    @Excel(name = "投保人电话", needMerge = true)
    private String insureUserPhone;

    /** 投保单业务流水号 */
    @ApiModelProperty("投保单业务流水号")
    @Excel(name = "投保单业务流水号", needMerge = true)
    private String insureNum;

    /** 投保分公司名称 */
    @ApiModelProperty("投保分公司名称")
    @Excel(name = "投保分公司名称", needMerge = true)
    private String insureCompany;

    /** 投保时间 */
    @ApiModelProperty("投保时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "投保时间", needMerge = true)
    private Date insureTime;

    /** 投保结束时间 */
    @ApiModelProperty("投保结束时间")
    @Excel(name = "投保结束时间", needMerge = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insureEndTime;

    /** ============================ 服务次数信息 =============================*/

    @ApiModelProperty("保险服务项")
    @Excel(name = "保险服务项")
    private List<InsuranceServiceImportBody> insuranceServiceImportBodyList;

}
