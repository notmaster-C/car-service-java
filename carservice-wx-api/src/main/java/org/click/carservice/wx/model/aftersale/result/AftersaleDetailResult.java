package org.click.carservice.wx.model.aftersale.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceAfterSale;
import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.db.domain.CarServiceOrderGoods;

import java.io.Serializable;
import java.util.List;

/**
 * 售后详情响应结果
 *
 * @author click
 */
@Data
public class AftersaleDetailResult implements Serializable {

    /**
     * 订单信息
     */
    private CarServiceOrder order;

    /**
     * 售后信息
     */
    private CarServiceAfterSale aftersale;

    /**
     * 售后商品信息
     */
    private CarServiceOrderGoods orderGoods;


}
