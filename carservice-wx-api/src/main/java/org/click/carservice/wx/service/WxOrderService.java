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
import org.click.carservice.db.domain.carserviceOrder;
import org.click.carservice.db.enums.OrderStatus;
import org.click.carservice.db.service.impl.OrderServiceImpl;
import org.click.carservice.wx.model.brand.body.BrandOrderListBody;
import org.click.carservice.wx.model.order.body.OrderListBody;
import org.click.carservice.wx.model.order.result.UserOrderInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_order")
public class WxOrderService extends OrderServiceImpl {


    @Cacheable(sync = true)
    public carserviceOrder findById(String userId, String orderId) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.USER_ID, userId);
        wrapper.eq(carserviceOrder.ID, orderId);
        return getOne(wrapper);
    }

    @Cacheable(sync = true)
    public Integer count(String userId) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.USER_ID, userId);
        return Math.toIntExact(count(wrapper));
    }


    @Cacheable(sync = true)
    public Integer count(List<Short> orderStatus) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        if (orderStatus != null && orderStatus.size() > 0) {
            wrapper.in(carserviceOrder.ORDER_STATUS, orderStatus);
        }
        return Math.toIntExact(count(wrapper));
    }

    /**
     * 判断订单号是否存在
     */
    @Cacheable(sync = true)
    public Boolean countByOrderSn(String userId, String orderSn) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.USER_ID, userId);
        wrapper.eq(carserviceOrder.ORDER_SN, orderSn);
        return exists(wrapper);
    }

    /**
     * 判断订单号是否存在
     */
    @Cacheable(sync = true)
    public carserviceOrder findBySn(String userId, String orderSn) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.USER_ID, userId);
        wrapper.eq(carserviceOrder.ORDER_SN, orderSn);
        return getOne(wrapper);
    }


    @Cacheable(sync = true)
    public carserviceOrder findByBrandId(String brandId, String orderId) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.ID, orderId);
        wrapper.eq(carserviceOrder.BRAND_ID, brandId);
        return getOne(wrapper);
    }

    /**
     * 判断商户订单号是否存在
     */
    public Boolean countByOutTradeNo(String userId, String outTradeNo) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.USER_ID, userId);
        wrapper.eq(carserviceOrder.OUT_TRADE_NO, outTradeNo);
        return exists(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceOrder> queryByOrderStatus(String userId, List<Short> orderStatus, OrderListBody body) {
        QueryWrapper<carserviceOrder> wrapper = startPage(body);
        wrapper.eq(carserviceOrder.USER_ID, userId);
        if (orderStatus != null && orderStatus.size() > 0) {
            wrapper.in(carserviceOrder.ORDER_STATUS, orderStatus);
        }
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceOrder> queryByBrandOrderStatus(String brandId, List<Short> orderStatus, BrandOrderListBody body) {
        QueryWrapper<carserviceOrder> wrapper = startPage(body);
        wrapper.eq(carserviceOrder.BRAND_ID, brandId);
        if (orderStatus != null && orderStatus.size() > 0) {
            wrapper.in(carserviceOrder.ORDER_STATUS, orderStatus);
        }
        if (StringUtils.hasText(body.getMobile())) {
            wrapper.like(carserviceOrder.MOBILE, body.getMobile());
        }
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public UserOrderInfo orderInfo(String userId) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.USER_ID, userId);
        List<carserviceOrder> orders = queryAll(wrapper);
        Integer unpaid = 0;
        Integer unship = 0;
        Integer unrecv = 0;
        Integer uncomment = 0;
        for (carserviceOrder order : orders) {
            if (OrderStatus.isCreateStatus(order) || OrderStatus.isGrouponNoneStatus(order)) {
                unpaid++;
            } else if (OrderStatus.isPayStatus(order) || OrderStatus.isBtlPayStatus(order) || OrderStatus.isGrouponOnStatus(order) || OrderStatus.isGrouponSucceedStatus(order)) {
                unship++;
            } else if (OrderStatus.isShipStatus(order)) {
                unrecv++;
            } else if (OrderStatus.isConfirmStatus(order) || OrderStatus.isAutoConfirmStatus(order)) {
                uncomment += order.getComments();
            }
        }
        UserOrderInfo orderInfo = new UserOrderInfo();
        orderInfo.setUnpaid(unpaid);
        orderInfo.setUnship(unship);
        orderInfo.setUnrecv(unrecv);
        orderInfo.setUncomment(uncomment);
        return orderInfo;
    }


    @CacheEvict(allEntries = true)
    public void updateAftersaleStatus(String orderId, Short statusReject) {
        carserviceOrder order = new carserviceOrder();
        order.setId(orderId);
        order.setAftersaleStatus(statusReject);
        order.setUpdateTime(LocalDateTime.now());
        updateById(order);
    }


}
