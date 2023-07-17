package org.click.carservice.wx.model.order.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author click
 */
@Data
public class OrderAdminRefundBody implements Serializable {

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private String orderId;

    /**
     * 退款金额
     */
    @NotNull(message = "退款金额不能为空")
    private String refundMoney;

}
