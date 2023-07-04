package org.ysling.litemall.admin.model.order.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单发货
 * @author Ysling
 */
@Data
public class OrderShipBody implements Serializable {

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private String orderId;
    /**
     * 物流单号
     */
    @NotNull(message = "物流单号不能为空")
    private String shipSn;
    /**
     * 物流公司
     */
    @NotNull(message = "物流公司不能为空")
    private String shipChannel;

}
