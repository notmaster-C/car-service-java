package org.click.carservice.wx.model.user.result;

import lombok.Data;
import org.click.carservice.db.domain.carserviceBrand;
import org.click.carservice.wx.model.order.result.UserOrderInfo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author click
 */
@Data
public class UserIndexResult implements Serializable {

    /**
     * 用户余额
     */
    private BigDecimal integralPrice;
    /**
     * 用户等级
     */
    private String userLevel;
    /**
     * 用户订单信息
     */
    private UserOrderInfo order;

    /**
     * 用户店铺信息
     */
    private carserviceBrand brand;

}
