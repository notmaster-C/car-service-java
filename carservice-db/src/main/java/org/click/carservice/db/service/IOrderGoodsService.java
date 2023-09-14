package org.click.carservice.db.service;

import org.click.carservice.db.domain.CarServiceOrderGoods;
import org.click.carservice.db.mybatis.IBaseService;

import java.util.List;

/**
 * <p>
 * 订单商品表 服务类
 * </p>
 *
 * @author click
 */
public interface IOrderGoodsService extends IBaseService<CarServiceOrderGoods> {

    /**
     * 商品id获取此商铺售出的商品
     * @param ids
     * @return
     */
    List<CarServiceOrderGoods> selectByBrandIds(List<String> ids);

}
