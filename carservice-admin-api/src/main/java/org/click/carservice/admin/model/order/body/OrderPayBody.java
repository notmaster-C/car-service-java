package org.click.carservice.admin.model.order.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 线下收款
 *
 * @author click
 */
@Data
public class OrderPayBody implements Serializable {

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private String orderId;
    /**
     * 收款金额
     */
    @NotNull(message = "收款金额不能为空")
    private String newMoney;


}
