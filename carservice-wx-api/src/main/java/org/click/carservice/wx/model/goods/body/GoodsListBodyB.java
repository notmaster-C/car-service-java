package org.click.carservice.wx.model.goods.body;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceGoods;

import java.io.Serializable;

@Data
public class GoodsListBodyB implements Serializable {
    /**
     * 商品信息
     */
    private CarServiceGoods goods;
    /**
     * 经度
     */
    private Float latitude;
    /**
     * 纬度
     */
    private Float longitude;
}
