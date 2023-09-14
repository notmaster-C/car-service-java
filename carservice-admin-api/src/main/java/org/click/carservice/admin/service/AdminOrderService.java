package org.click.carservice.admin.service;
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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.core.config.DataScopeConfig;
import org.click.carservice.admin.model.order.body.OrderListBody;
import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.db.service.impl.OrderServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 订单服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_order")
public class AdminOrderService extends OrderServiceImpl {

//    @Cacheable(sync = true)
    public List<CarServiceOrder> querySelective(OrderListBody body) {
        QueryWrapper<CarServiceOrder> wrapper = startPage(body);
        // 根据当前管理系统用户id获取到小程序商户id,从而查询到用户的所有订单
        if (DataScopeConfig.dataScope(wrapper)) return CollUtil.empty(List.class);
        if (body.getBrandId() != null) {
            wrapper.eq(CarServiceOrder.BRAND_ID, body.getBrandId());
        }
        if (body.getGoodsId() != null) {
            wrapper.eq(CarServiceOrder.GOODS_ID, body.getGoodsId());
        }
        if (body.getEnd() != null) {
            wrapper.le(CarServiceOrder.ADD_TIME, body.getEnd());
        }
        if (body.getStart() != null) {
            wrapper.ge(CarServiceOrder.ADD_TIME, body.getStart());
        }
        if (StringUtils.hasText(body.getShipSn())) {
            wrapper.le(CarServiceOrder.SHIP_SN, body.getShipSn());
        }
        if (StringUtils.hasText(body.getMobile())) {
            wrapper.like(CarServiceOrder.MOBILE, body.getMobile());
        }
        if (StringUtils.hasText(body.getOrderSn())) {
            wrapper.like(CarServiceOrder.ORDER_SN, body.getOrderSn());
        }
        if (body.getOrderStatusArray() != null && body.getOrderStatusArray().size() > 0) {
            wrapper.in(CarServiceOrder.ORDER_STATUS, body.getOrderStatusArray());
        }
        return queryAll(wrapper);
    }

//    @Cacheable(sync = true)
    public Integer statusCount(List<Short> orderStatus) {
        if (orderStatus == null || orderStatus.size() <= 0) {
            return 0;
        }
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        if (DataScopeConfig.dataScope(wrapper)) return 0;
        wrapper.in(CarServiceOrder.ORDER_STATUS, orderStatus);
        return Math.toIntExact(count(wrapper));
    }

}
