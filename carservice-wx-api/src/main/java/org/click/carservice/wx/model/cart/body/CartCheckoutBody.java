package org.click.carservice.wx.model.cart.body;

import lombok.Data;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class CartCheckoutBody implements Serializable {

    /**
     * 购物车商品ID：
     * 如果购物车商品ID是空，则下单当前用户所有购物车商品；
     * 如果购物车商品ID非空，则只下单当前购物车商品。
     */
    private String cartId;

    /**
     * 收货地址ID：
     * 如果收货地址ID是空，则查询当前用户的默认地址。
     */
    private String addressId;

    private String carId;

    /**
     * 优惠券ID：
     * 如果优惠券ID是空，则自动选择合适的优惠券。
     */
    private String couponId;

    /**
     * 用户选择的优惠券ID
     */
    private String userCouponId;

    /**
     * 团购规则ID
     */
    private String grouponRulesId;

}
