package org.click.carservice.core.tasks.impl;
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

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.service.DealingSlipCoreService;
import org.click.carservice.core.system.SystemConfig;
import org.click.carservice.core.tasks.service.TaskRunnable;
import org.click.carservice.core.utils.BeanUtil;
import org.click.carservice.db.domain.CarServiceBrand;
import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.db.enums.OrderStatus;
import org.click.carservice.db.service.IBrandService;
import org.click.carservice.db.service.IOrderService;

import java.time.LocalDateTime;

/**
 * 订单自动确认收货延时队列
 * @author click
 */
@Slf4j
public class OrderUnconfirmedTask extends TaskRunnable {

    /**订单ID*/
    private final String orderId;
    /**id前缀*/
    private static final String idPrefix = "OrderUnconfirmedTask-";
    /**任务名称*/
    private static final String taskName = "订单自动收货";
    /**默认延时时间 单位毫秒*/
    private static final long defaultSeconds = SystemConfig.getOrderUnconfirmed() * 24 * 60 * 60 * 1000;


    public OrderUnconfirmedTask(CarServiceOrder order, long delayInMilliseconds) {
        super(idPrefix + order.getId(), delayInMilliseconds, order.getTenantId(), taskName);
        this.orderId = order.getId();
    }

    public OrderUnconfirmedTask(CarServiceOrder order) {
        super(idPrefix + order.getId(), defaultSeconds, order.getTenantId(), taskName);
        this.orderId = order.getId();
    }

    @Override
    public void runTask() {
        IOrderService orderService = BeanUtil.getBean(IOrderService.class);
        IBrandService brandService = BeanUtil.getBean(IBrandService.class);
        DealingSlipCoreService slipCoreService = BeanUtil.getBean(DealingSlipCoreService.class);

        CarServiceOrder order = orderService.findById(this.orderId);
        //判断是否满足收货条件
        if (order == null || !OrderStatus.isShipStatus(order)) {
            return;
        }
        //获取店铺信息
        CarServiceBrand brand = brandService.findById(order.getBrandId());
        if (brand == null) {
            throw new RuntimeException("店铺信息获取失败");
        }

        // 设置订单已收货状态
        order.setConfirmTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.STATUS_AUTO_CONFIRM.getStatus());
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("订单 ID=" + order.getId() + "数据更新失败");
        }

        //获取店铺所有者向店家发放余额
        slipCoreService.addOrderIntegral(order, brand);
    }
}
