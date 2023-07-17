package org.click.carservice.wx.model.order.result;

import lombok.Data;
import org.click.carservice.db.domain.carserviceGroupon;
import org.click.carservice.db.entity.OrderHandleOption;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author click
 */
@Data
public class OrderListResult implements Serializable {

    /**
     * 订单id
     */
    private String id;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 订单金额
     */
    private BigDecimal actualPrice;
    /**
     * 订单状态文本
     */
    private String orderStatusText;
    /**
     * 可执行操作
     */
    private OrderHandleOption handleOption;
    /**
     * 售后状态
     */
    private Short aftersaleStatus;
    /**
     * 团购信息
     */
    private carserviceGroupon groupon;
    /**
     * 是否团购
     */
    private Boolean isGroupon;
    /**
     * 团购状态
     */
    private String grouponStatus;
    /**
     * 商品列表
     */
    private List<OrderGoodsResult> goodsList;

}
