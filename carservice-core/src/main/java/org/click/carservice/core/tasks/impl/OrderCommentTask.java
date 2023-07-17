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
import org.click.carservice.core.service.CommonService;
import org.click.carservice.core.system.SystemConfig;
import org.click.carservice.core.tasks.service.TaskRunnable;
import org.click.carservice.core.utils.BeanUtil;
import org.click.carservice.db.domain.carserviceOrder;
import org.click.carservice.db.domain.carserviceOrderGoods;
import org.click.carservice.db.enums.OrderStatus;
import org.click.carservice.db.service.IOrderGoodsService;
import org.click.carservice.db.service.IOrderService;

import java.util.List;

/**
 * 订单评论超时
 * @author click
 */
@Slf4j
public class OrderCommentTask extends TaskRunnable {

    /**订单ID*/
    private final String orderId;
    /**id前缀*/
    private static final String idPrefix = "OrderCommentTask-";
    /**任务名称*/
    private static final String taskName = "订单评论超时";
    /**默认延时时间 单位毫秒*/
    private static final long defaultSeconds = SystemConfig.getOrderComment() * 24 * 60 * 60 * 1000;


    public OrderCommentTask(carserviceOrder order, long delayInMilliseconds) {
        super(idPrefix + order.getId(), delayInMilliseconds, order.getTenantId(), taskName);
        this.orderId = order.getId();
    }

    public OrderCommentTask(carserviceOrder order) {
        super(idPrefix + order.getId(), defaultSeconds, order.getTenantId(), taskName);
        this.orderId = order.getId();
    }

    @Override
    public void runTask() {
        CommonService commonService = BeanUtil.getBean(CommonService.class);
        IOrderService orderService = BeanUtil.getBean(IOrderService.class);
        IOrderGoodsService orderGoodsService = BeanUtil.getBean(IOrderGoodsService.class);

        carserviceOrder order = orderService.findById(this.orderId);
        if (order == null || !OrderStatus.isConfirmStatus(order)) {
            return;
        }

        order.setComments((short) 0);
        order.setOrderStatus(OrderStatus.STATUS_COMMENT_OVERTIME.getStatus());
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }

        //设置订单商品禁止评论
        List<carserviceOrderGoods> orderGoodsList = commonService.queryByOid(order.getId());
        for (carserviceOrderGoods orderGoods : orderGoodsList) {
            orderGoods.setComment(-1);
            if (orderGoodsService.updateVersionSelective(orderGoods) == 0) {
                throw new RuntimeException("商品评论更新失败");
            }
        }
    }


}
