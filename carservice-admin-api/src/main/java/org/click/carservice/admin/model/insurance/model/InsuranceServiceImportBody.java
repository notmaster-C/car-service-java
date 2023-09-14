package org.click.carservice.admin.model.insurance.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.click.carservice.db.annotation.Excel;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "保单服务项信息", description = "保单服务项信息")
public class InsuranceServiceImportBody {
        
        /** 服务名称 */
        @ApiModelProperty("服务名称")
        @Excel(name = "服务名称")
        private String serviceName;

        /** 服务类型 */
        @ApiModelProperty("服务类型")
        @Excel(name = "服务类型", combo = {"100001", "100002", "100003"})
        private String serviceCode;

        /** 服务总次数 */
        @ApiModelProperty("服务总次数")
        @Excel(name = "服务总次数")
        private Long serviceTotal;

    }