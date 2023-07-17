package org.click.carservice.wx.model.order.result;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author click
 */
@Data
public class OrderSubmitResult implements Serializable {

    /**
     * 是否需要调用支付
     */
    private Boolean isPay;

    /**
     * 订单ID列表
     */
    private ArrayList<String> orderIds;

}
