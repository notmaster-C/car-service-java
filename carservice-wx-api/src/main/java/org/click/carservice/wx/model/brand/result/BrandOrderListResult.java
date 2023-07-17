package org.click.carservice.wx.model.brand.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceGroupon;
import org.click.carservice.db.domain.CarServiceOrderGoods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author click
 */
@Data
public class BrandOrderListResult implements Serializable {

    /**
     * 订单表ID
     */
    private String id;
    /**
     * 用户表的用户ID
     */
    private String userId;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 收货具体地址
     */
    private String address;
    /**
     * 实付费用， = goods_price + freight_price - grouponPrice - integral_price - coupon_price
     */
    private BigDecimal actualPrice;
    /**
     * 创建时间
     */
    private LocalDateTime addTime;
    /**
     * 订单状态描述
     */
    private String orderStatusText;
    /**
     * 售后状态，0是可申请，1是用户已申请，2是管理员审核通过，3是管理员退款成功，4是管理员审核拒绝，5是用户已取消
     */
    private Short aftersaleStatus;
    /**
     * 团购信息
     */
    private CarServiceGroupon groupon;
    /**
     * 是否团购
     */
    private Boolean isGroupon;

    /**
     * 团购失败（待退款）
     */
    private Boolean refund;
    /**
     * 团购成功（待发货）
     */
    private Boolean ship;
    /**
     * 团购状态
     */
    private String grouponStatus;
    /**
     * 订单商品信息
     */
    private CarServiceOrderGoods goods;

}
