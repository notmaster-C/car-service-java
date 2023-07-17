package org.click.carservice.wx.model.order.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author click
 */
@Data
public class OrderAdminShipBody implements Serializable {

    /**
     * 物流单号
     */
    @NotNull(message = "物流单号不能为空")
    private String shipSn;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private String orderId;

    /**
     * 物流公司
     */
    @NotNull(message = "物流公司不能为空")
    private String shipChannel;


}
