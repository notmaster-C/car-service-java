package org.click.carservice.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.click.carservice.db.domain.CarServiceOrderGoods;

import java.util.List;

/**
 * <p>
 * 订单商品表 Mapper 接口
 * </p>
 *
 * @author click
 */
public interface OrderGoodsMapper extends BaseMapper<CarServiceOrderGoods> {

    /**
     * 商品id获取此商铺售出的商品
     * @param ids
     * @return
     */
    List<CarServiceOrderGoods> selectByBrandIds(List<String> ids);
}
