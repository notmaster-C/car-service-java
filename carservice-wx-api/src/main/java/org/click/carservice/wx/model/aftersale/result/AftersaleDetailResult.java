package org.click.carservice.wx.model.aftersale.result;

import lombok.Data;
import org.click.carservice.db.domain.carserviceAftersale;
import org.click.carservice.db.domain.carserviceOrder;
import org.click.carservice.db.domain.carserviceOrderGoods;

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
    private carserviceOrder order;

    /**
     * 售后信息
     */
    private carserviceAftersale aftersale;

    /**
     * 售后商品信息
     */
    private List<carserviceOrderGoods> orderGoods;


}
