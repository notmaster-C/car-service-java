package org.click.carservice.wx.model.cart.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceCart;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class CartIndexResult implements Serializable {

    /**
     * 计算购物车信息
     */
    private CartTotalResult cartTotal;
    /**
     * 购物车商品
     */
    private List<CarServiceCart> cartList;

}
