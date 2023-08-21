package org.click.carservice.wx.model.order.body;

import lombok.Data;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class OrderSubmitBody implements Serializable {

    /**
     * 购物车ID
     */
    private String cartId;
    /**
     * 订单留言
     */
    private String message;
    /**
     * 优惠券ID
     */
    private String couponId;
    /**
     * 用户地址ID
     */
    private String addressId;
    /**
     * 用户电话
     */
    private String mobile;
    /**
     * 用户优惠券ID
     */
    private String userCouponId;
    /**
     * 赏金参与ID
     */
    private String rewardLinkId;
    /**
     * 团购参与ID
     */
    private String grouponLinkId;
    /**
     * 团购规格ID
     */
    private String grouponRulesId;

}
