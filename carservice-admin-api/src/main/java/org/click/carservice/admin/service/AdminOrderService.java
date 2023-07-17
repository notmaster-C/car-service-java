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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.admin.model.order.body.OrderListBody;
import org.click.carservice.db.domain.carserviceOrder;
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


    @Cacheable(sync = true)
    public List<carserviceOrder> querySelective(OrderListBody body) {
        QueryWrapper<carserviceOrder> wrapper = startPage(body);
        if (body.getBrandId() != null) {
            wrapper.eq(carserviceOrder.BRAND_ID, body.getBrandId());
        }
        if (body.getGoodsId() != null) {
            wrapper.eq(carserviceOrder.GOODS_ID, body.getGoodsId());
        }
        if (body.getEnd() != null) {
            wrapper.le(carserviceOrder.ADD_TIME, body.getEnd());
        }
        if (body.getStart() != null) {
            wrapper.ge(carserviceOrder.ADD_TIME, body.getStart());
        }
        if (StringUtils.hasText(body.getShipSn())) {
            wrapper.le(carserviceOrder.SHIP_SN, body.getShipSn());
        }
        if (StringUtils.hasText(body.getMobile())) {
            wrapper.like(carserviceOrder.MOBILE, body.getMobile());
        }
        if (StringUtils.hasText(body.getOrderSn())) {
            wrapper.like(carserviceOrder.ORDER_SN, body.getOrderSn());
        }
        if (body.getOrderStatusArray() != null && body.getOrderStatusArray().size() > 0) {
            wrapper.in(carserviceOrder.ORDER_STATUS, body.getOrderStatusArray());
        }
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public Integer statusCount(List<Short> orderStatus) {
        if (orderStatus == null || orderStatus.size() <= 0) {
            return 0;
        }
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.in(carserviceOrder.ORDER_STATUS, orderStatus);
        return Math.toIntExact(count(wrapper));
    }

}
