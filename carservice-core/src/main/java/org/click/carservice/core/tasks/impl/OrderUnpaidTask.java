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
import org.click.carservice.core.service.OrderCoreService;
import org.click.carservice.core.system.SystemConfig;
import org.click.carservice.core.tasks.service.TaskRunnable;
import org.click.carservice.core.utils.BeanUtil;
import org.click.carservice.db.domain.carserviceOrder;
import org.click.carservice.db.enums.OrderStatus;
import org.click.carservice.db.service.IOrderService;

/**
 * 订单超时支付延时队列
 * @author click
 */
@Slf4j
public class OrderUnpaidTask extends TaskRunnable {

    /**订单ID*/
    private final String orderId;
    /**id前缀*/
    private static final String idPrefix = "OrderUnpaidTask-";
    /**任务名称*/
    private static final String taskName = "订单超时未付款";
    /**默认延时时间 单位毫秒*/
    private static final long defaultSeconds = SystemConfig.getOrderUnpaid() * 60 * 1000;


    public OrderUnpaidTask(carserviceOrder order, long delayInMilliseconds) {
        super(idPrefix + order.getId(), delayInMilliseconds, order.getTenantId(), taskName);
        this.orderId = order.getId();
    }

    public OrderUnpaidTask(carserviceOrder order) {
        super(idPrefix + order.getId(), defaultSeconds, order.getTenantId(), taskName);
        this.orderId = order.getId();
    }

    @Override
    public void runTask() {
        OrderCoreService orderCoreService = BeanUtil.getBean(OrderCoreService.class);
        IOrderService orderService = BeanUtil.getBean(IOrderService.class);

        carserviceOrder order = orderService.findById(this.orderId);
        if (order == null || !OrderStatus.isCreateStatus(order)) {
            return;
        }
        // 设置订单已取消状态
        order.setOrderStatus(OrderStatus.STATUS_AUTO_CANCEL.getStatus());
        if (orderService.updateVersionSelective(order) <= 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }
        // 返还订单
        orderCoreService.orderRelease(order);
    }
}
