package org.click.carservice.wx.model.order.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceGroupon;
import org.click.carservice.db.domain.CarServiceGrouponRules;
import org.click.carservice.db.domain.CarServiceOrderGoods;
import org.click.carservice.db.entity.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class OrderDetailResult implements Serializable {

    /**
     * 订单信息
     */
    private OrderInfo orderInfo;
    /**
     * 订单提示
     */
    private Integer orderBasics;
    /**
     * 订单商品信息
     */
    private List<CarServiceOrderGoods> orderGoods;
    /**
     * 团购提示
     */
    private Short grouponBasics;
    /**
     * 团购ID
     */
    private String linkGrouponId;
    /**
     * 用户信息
     */
    private UserInfo creator;
    /**
     * 团购信息
     */
    private CarServiceGroupon groupon;
    /**
     * 团购规则
     */
    private CarServiceGrouponRules rules;
    /**
     * 用户信息列表
     */
    private List<UserInfo> joiners;

}
