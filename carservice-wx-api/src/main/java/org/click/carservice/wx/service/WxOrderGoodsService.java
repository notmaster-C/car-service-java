package org.click.carservice.wx.service;
/**
 * Copyright (c) [click] [927069313@qq.com]
 * [carservice-plus] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.CarServiceOrderGoods;
import org.click.carservice.db.service.impl.OrderGoodsServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单商品服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_order_goods")
public class WxOrderGoodsService extends OrderGoodsServiceImpl {


    @Cacheable(sync = true)
    public Integer getComments(String orderId) {
        QueryWrapper<CarServiceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrderGoods.ORDER_ID, orderId);
        return Math.toIntExact(count(wrapper));
    }


    @Cacheable(sync = true)
    public boolean checkExist(String goodsId) {
        QueryWrapper<CarServiceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrderGoods.GOODS_ID, goodsId);
        return exists(wrapper);
    }

    @Cacheable(sync = true)
    public CarServiceOrderGoods findByOrderId(String orderId) {
        QueryWrapper<CarServiceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrderGoods.ORDER_ID, orderId);
        return getOne(wrapper, false);
    }

    @Cacheable(sync = true)
    public List<CarServiceOrderGoods> queryByOrderId(String orderId) {
        QueryWrapper<CarServiceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrderGoods.ORDER_ID, orderId);
        return queryAll(wrapper);
    }


    @CacheEvict(allEntries = true)
    public void deleteByOrderId(String orderId) {
        QueryWrapper<CarServiceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrderGoods.ORDER_ID, orderId);
        remove(wrapper);
    }


}
