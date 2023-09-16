package org.click.carservice.wx.model.cart.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceAddress;
import org.click.carservice.db.domain.CarServiceCart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author click
 */
@Data
public class CartCheckoutResult implements Serializable {

    /**
     * 购物车ID
     */
    private String cartId;
    /**
     * 收货地址ID
     */
    private String addressId;
    /**
     * 优惠券ID
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
    /**
     * 团购金额
     */
    private BigDecimal grouponPrice;
    /**
     * 余额金额
     */
    private BigDecimal integralPrice;
    /**
     * 选中地址
     */
    private CarServiceAddress checkedAddress;
    /**
     * 优惠券可用数量
     */
    private long availableCouponLength;
    /**
     * 选中商品金额
     */
    private BigDecimal goodsTotalPrice;
    /**
     * 运费金额
     */
    private BigDecimal freightPrice;
    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;
    /**
     * 订单金额
     */
    private BigDecimal orderTotalPrice;
    /**
     * 合计金额
     */
    private BigDecimal actualPrice;
    /**
     * 选中商品列表
     */
    private List<CarServiceCart> checkedGoodsList;

}
