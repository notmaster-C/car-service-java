package org.click.carservice.db.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @title: OrderVerificationQuery
 * @Author HuangYan
 * @Date: 2023/8/23 21:50
 * @Version 1.0
 * @Description: 核销查询实体
 */
@Data
@ApiModel("核销查询实体")
public class OrderVerificationQuery {

    @ApiModelProperty("订单编号")
    private String orderSn;

    /**
     * 商铺名称
     */
    @ApiModelProperty("商铺名称")
    private String brandName;

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private Integer orderStatus;

}
