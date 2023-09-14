package org.click.carservice.admin.web;
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

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.context.SaTokenContext;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.admin.annotation.RequiresPermissionsDesc;
import org.click.carservice.admin.model.order.body.*;
import org.click.carservice.admin.model.order.result.OrderDetailResult;
import org.click.carservice.admin.model.order.result.OrderListResult;
import org.click.carservice.admin.service.AdminGoodsCommentService;
import org.click.carservice.admin.service.AdminOrderGoodsService;
import org.click.carservice.admin.service.AdminOrderService;
import org.click.carservice.admin.service.AdminUserService;
import org.click.carservice.core.express.service.ExpressService;
import org.click.carservice.core.handler.ActionLogHandler;
import org.click.carservice.core.service.OrderCoreService;
import org.click.carservice.core.tasks.impl.OrderUnconfirmedTask;
import org.click.carservice.core.tasks.service.TaskService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.core.weixin.service.SubscribeMessageService;
import org.click.carservice.core.weixin.service.WxPayRefundService;
import org.click.carservice.db.domain.CarServiceGoodsComment;
import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.db.domain.CarServiceOrderGoods;
import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.db.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 订单管理
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/order")
@Validated
public class AdminOrderController {


    @Autowired
    private AdminOrderGoodsService orderGoodsService;
    @Autowired
    private OrderCoreService orderCoreService;
    @Autowired
    private AdminOrderService orderService;
    @Autowired
    private SubscribeMessageService subscribeMessageService;
    @Autowired
    private AdminUserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AdminGoodsCommentService goodsCommentService;
    @Autowired
    private WxPayRefundService wxPayRefundService;
    @Autowired
    private ExpressService expressService;

    /**
     * 查询订单
     */
    @SaCheckPermission(value = "admin:order:list", mode = SaMode.OR, orRole = {"商户"})
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(OrderListBody body) {
        //获取分页类型下的订单状态
        if (body.getShowType() != null) {
            List<Short> orderStatus = OrderStatus.orderAdminStatus(body.getShowType());
            if (orderStatus != null) {
                if (body.getOrderStatusArray() == null) {
                    body.setOrderStatusArray(orderStatus);
                } else {
                    body.getOrderStatusArray().addAll(orderStatus);
                }
            }
        }
        //查询订单列表
        List<CarServiceOrder> orderList = orderService.querySelective(body);
        //拼接订单信息
        List<OrderListResult> data = new ArrayList<>();
        for (CarServiceOrder order : orderList) {
            OrderListResult result = new OrderListResult();
            BeanUtil.copyProperties(order, result);
            result.setOrderStatusText(OrderStatus.orderStatusText(order));
            result.setShipChannel(expressService.getVendorName(order.getShipChannel()));
            //用户信息
            CarServiceUser user = userService.findById(order.getUserId());
            if (user != null) {
                result.setUserName(user.getNickName());
                result.setUserAvatar(user.getAvatarUrl());
                result.setUserMobile(user.getMobile());
            }
            //商品信息
            List<CarServiceOrderGoods> goodsList = orderGoodsService.queryByOid(order.getId());
            Integer goodsNumber = 0;
            for (CarServiceOrderGoods goods : goodsList) {
                goodsNumber += goods.getNumber();
            }
            result.setGoodsNumber(goodsNumber);
            result.setGoodsVoList(goodsList);
            data.add(result);
        }
        return ResponseUtil.okList(data, orderList);
    }

    /**
     * 查询物流公司
     */
    @SaCheckPermission(value = "admin:order:channel", mode = SaMode.OR, orRole = {"商户"})
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "查询物流公司")
    @GetMapping("/channel")
    public Object channel() {
        return ResponseUtil.ok(expressService.getVendors());
    }

    /**
     * 查询订单数量
     */
    @SaCheckPermission(value = "admin:order:info", orRole = {"商户"})
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "查询订单数量")
    @GetMapping("/info")
    public Object info() {
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            List<Short> orderStatus = OrderStatus.orderAdminStatus(i);
            data.put("n" + i, orderService.statusCount(orderStatus));
        }
        return ResponseUtil.ok(data);
    }

    /**
     * 订单详情
     */
    @SaCheckPermission(value = "admin:order:read", mode = SaMode.OR, orRole = {"商户"})
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "详情")
    @GetMapping("/detail")
    public Object detail(@NotNull String id) {
        CarServiceOrder order = orderService.findById(id);
        OrderDetailResult result = new OrderDetailResult();
        result.setOrder(order);
        result.setUser(userService.findUserVoById(order.getUserId()));
        result.setOrderGoods(orderGoodsService.queryByOid(id));
        result.setOrderStatusText(OrderStatus.orderStatusText(order));
        return ResponseUtil.ok(result);
    }

    /**
     * 订单取消
     */
    @SaCheckPermission(value = "admin:order:cancel", mode = SaMode.OR, orRole = {"商户"})
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单取消")
    @PostMapping("/cancel")
    public Object cancel(@NotNull String id) {
        CarServiceOrder order = orderService.findById(id);
        if (order == null) {
            return ResponseUtil.badArgumentValue();
        }
        if (!OrderStatus.hasShip(order)) {
            return ResponseUtil.fail("订单不能取消");
        }
        // 设置订单已取消状态
        if (order.getActualPrice().compareTo(BigDecimal.ZERO) > 0) {
            order.setOrderStatus(OrderStatus.STATUS_REFUND.getStatus());
        } else {
            order.setOrderStatus(OrderStatus.STATUS_ADMIN_CANCEL.getStatus());
        }
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("更新数据已失效");
        }
        // 返还订单
        orderCoreService.orderRelease(order);
        return ResponseUtil.ok();
    }

    /**
     * 订单退款
     */
    @SaCheckPermission(value = "admin:order:refund", mode = SaMode.OR, orRole = {"商户"})
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单退款")
    @PostMapping("/refund")
    public Object refund(@Valid @RequestBody OrderRefundBody body) {
        String orderId = body.getOrderId();
        String refundMoney = body.getRefundMoney();
        CarServiceOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }

        if (order.getActualPrice().compareTo(new BigDecimal(refundMoney)) < 0) {
            return ResponseUtil.fail("退款金额不能大于付款金额");
        }

        // 如果订单不是退款状态，则不能退款
        if (!(OrderStatus.isRefundStatus(order) || OrderStatus.isGrouponFailStatus(order))) {
            return ResponseUtil.fail("订单不是退款状态，不能退款");
        }

        //如果订单金额为零则跳过退款接口直接修改订单状态
        if (new BigDecimal(refundMoney).compareTo(BigDecimal.ZERO) > 0) {
            // 微信退款
            WxPayRefundResult refundResult = wxPayRefundService.wxPayRefund(order);
            if (refundResult != null) {
                order.setRefundContent(refundResult.getRefundId());
            }
        } else {
            order.setRefundContent("线下付款退款");
        }

        // 设置订单取消状态
        order.setRefundTime(LocalDateTime.now());
        order.setRefundType("微信退款接口");
        order.setRefundAmount(order.getActualPrice());
        order.setOrderStatus(OrderStatus.STATUS_REFUND_CONFIRM.getStatus());
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }

        // 返还订单
        orderCoreService.orderRelease(order);

        //订单退款订阅通知
        CarServiceUser user = userService.findById(order.getUserId());
        subscribeMessageService.refundSubscribe(user.getOpenid(), order);

        ActionLogHandler.logOrderSucceed("退款", "订单编号 " + order.getOrderSn());
        return ResponseUtil.ok();
    }

    /**
     * 发货
     */
    @SaCheckPermission(value = "admin:order:ship", mode = SaMode.OR, orRole = {"商户"})
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单发货")
    @PostMapping("/ship")
    public Object ship(@Valid @RequestBody OrderShipBody body) {
        String orderId = body.getOrderId();
        String shipSn = body.getShipSn();
        String shipChannel = body.getShipChannel();

        CarServiceOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }

        // 如果订单不是已付款状态，则不能发货
        if (!OrderStatus.hasShip(order)) {
            return ResponseUtil.fail("订单不能发货");
        }

        order.setShipSn(shipSn);
        order.setShipChannel(shipChannel);
        order.setShipTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.STATUS_SHIP.getStatus());
        if (orderService.updateVersionSelective(order) == 0) {
            return ResponseUtil.updatedDateExpired();
        }

        //订单发货订阅通知
        CarServiceUser user = userService.findById(order.getUserId());
        subscribeMessageService.shipSubscribe(user.getOpenid(), order);

        //添加确认收货定时任务
        taskService.addTask(new OrderUnconfirmedTask(order));

        ActionLogHandler.logOrderSucceed("发货", "订单编号 " + order.getOrderSn());
        return ResponseUtil.ok();
    }

    /**
     * 线下收款
     */
    @SaCheckPermission(value = "admin:order:pay", mode = SaMode.OR, orRole = {"商户"})
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单收款")
    @PostMapping("/pay")
    public Object pay(@Valid @RequestBody OrderPayBody body) {
        String orderId = body.getOrderId();
        String newMoney = body.getNewMoney();
        CarServiceOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }
        if (!(OrderStatus.isCreateStatus(order) || OrderStatus.isGrouponNoneStatus(order))) {
            return ResponseUtil.fail("当前订单状态不支持线下收款");
        }
        //设置订单状态
        order.setActualPrice(new BigDecimal(newMoney));
        orderCoreService.orderPaySuccess(order);
        return ResponseUtil.ok();
    }

    /**
     * 删除订单
     */
    @SaCheckPermission(value = "admin:order:delete", mode = SaMode.OR, orRole = {"商户"})
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单删除")
    @PostMapping("/delete")
    public Object delete(@NotNull String orderId) {
        CarServiceOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }
        // 如果订单不是关闭状态(已取消、系统取消、已退款、用户已确认、系统已确认)，则不能删除
        if (!OrderStatus.hasDelete(order)) {
            return ResponseUtil.fail("订单不能删除");
        }
        // 删除订单
        orderService.deleteById(orderId);
        // 删除订单商品
        orderGoodsService.deleteByOrderId(orderId);
        //添加删除日志
        ActionLogHandler.logOrderSucceed("删除", "订单编号 " + order.getOrderSn());
        return ResponseUtil.ok();
    }

    /**
     * 回复订单商品
     */
    @SaCheckPermission(value = "admin:order:reply", mode = SaMode.OR, orRole = {"商户"})
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单商品回复")
    @PostMapping("/reply")
    public Object reply(@Valid @RequestBody OrderReplyBody body) {
        String commentId = body.getCommentId();
        // 目前只支持回复一次
        CarServiceGoodsComment comment = goodsCommentService.findById(commentId);
        if (comment == null) {
            return ResponseUtil.badArgument();
        }
        if (Objects.isNull(comment.getAdminContent())) {
            return ResponseUtil.fail("订单商品已回复！");
        }
        String content = body.getContent();
        if (Objects.isNull(content)) {
            return ResponseUtil.badArgument();
        }
        // 更新评价回复
        comment.setAdminContent(content);
        if (goodsCommentService.updateVersionSelective(comment) == 0) {
            return ResponseUtil.updatedDateExpired();
        }

        return ResponseUtil.ok();
    }
}
