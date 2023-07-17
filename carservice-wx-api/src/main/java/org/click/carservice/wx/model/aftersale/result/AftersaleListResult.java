package org.click.carservice.wx.model.aftersale.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceAfterSale;
import org.click.carservice.db.domain.CarServiceOrderGoods;

import java.io.Serializable;
import java.util.List;

/**
 * 售后列表响应结果
 *
 * @author click
 */
@Data
public class AftersaleListResult implements Serializable {

    /**
     * 售后状态文本
     */
    private String statusText;

    /**
     * 售后信息
     */
    private CarServiceAfterSale aftersale;

    /**
     * 售后商品信息
     */
    private CarServiceOrderGoods orderGoods;


}
