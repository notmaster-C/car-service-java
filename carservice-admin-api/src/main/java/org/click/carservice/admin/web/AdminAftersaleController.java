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

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.admin.annotation.RequiresPermissionsDesc;
import org.click.carservice.admin.model.aftersale.body.AftersaleListBody;
import org.click.carservice.admin.service.AdminAftersaleService;
import org.click.carservice.admin.service.AdminOrderService;
import org.click.carservice.admin.service.AdminUserService;
import org.click.carservice.core.handler.ActionLogHandler;
import org.click.carservice.core.service.AftersaleCoreService;
import org.click.carservice.core.service.OrderCoreService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.core.weixin.service.SubscribeMessageService;
import org.click.carservice.core.weixin.service.WxPayRefundService;
import org.click.carservice.db.domain.CarServiceAfterSale;
import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.db.entity.IdsBody;
import org.click.carservice.db.enums.AftersaleStatus;
import org.click.carservice.db.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单售后
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/aftersale")
@Validated
public class AdminAftersaleController {


    @Autowired
    private AftersaleCoreService aftersaleCoreService;
    @Autowired
    private SubscribeMessageService subscribeMessageService;
    @Autowired
    private AdminAftersaleService aftersaleService;
    @Autowired
    private AdminOrderService orderService;
    @Autowired
    private WxPayRefundService wxPayRefundService;
    @Autowired
    private AdminUserService userService;
    @Autowired
    private OrderCoreService orderCoreService;


    /**
     * 查询
     */
    @SaCheckPermission("admin:aftersale:list")
    @RequiresPermissionsDesc(menu = {"商场管理", "售后管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(AftersaleListBody body) {
        return ResponseUtil.okList(aftersaleService.querySelective(body));
    }


    /**
     * 审核通过
     */
    @SaCheckPermission("admin:aftersale:recept")
    @RequiresPermissionsDesc(menu = {"商场管理", "售后管理"}, button = "审核通过")
    @PostMapping("/recept")
    public Object recept(@NotNull String id) {
        CarServiceAfterSale aftersale = aftersaleService.findById(id);
        if (aftersale == null) {
            return ResponseUtil.fail("售后不存在");
        }
        return aftersaleCoreService.recept(aftersale);
    }

    /**
     * 批量通过
     */
    @SaCheckPermission("admin:aftersale:batch-recept")
    @RequiresPermissionsDesc(menu = {"商场管理", "售后管理"}, button = "批量通过")
    @PostMapping("/batch-recept")
    public Object batchRecept(@Valid @RequestBody IdsBody body) {
        for (String id : body.getIds()) {
            CarServiceAfterSale aftersale = aftersaleService.findById(id);
            if (aftersale == null) {
                continue;
            }
            aftersaleCoreService.recept(aftersale);
        }
        return ResponseUtil.ok();
    }


    /**
     * 审核拒绝
     */
    @SaCheckPermission("admin:aftersale:reject")
    @RequiresPermissionsDesc(menu = {"商场管理", "售后管理"}, button = "审核拒绝")
    @PostMapping("/reject")
    public Object reject(@NotNull String id) {
        CarServiceAfterSale aftersale = aftersaleService.findById(id);
        if (aftersale == null) {
            return ResponseUtil.badArgumentValue();
        }
        return aftersaleCoreService.reject(aftersale);
    }


    /**
     * 批量拒绝
     */
    @SaCheckPermission("admin:aftersale:batch-reject")
    @RequiresPermissionsDesc(menu = {"商场管理", "售后管理"}, button = "批量拒绝")
    @PostMapping("/batch-reject")
    public Object batchReject(@Valid @RequestBody IdsBody body) {
        for (String id : body.getIds()) {
            CarServiceAfterSale aftersale = aftersaleService.findById(id);
            if (aftersale == null) {
                continue;
            }
            aftersaleCoreService.reject(aftersale);
        }
        return ResponseUtil.ok();
    }


    /**
     * 退款
     */
    @SaCheckPermission("admin:aftersale:refund")
    @RequiresPermissionsDesc(menu = {"商场管理", "售后管理"}, button = "退款")
    @PostMapping("/refund")
    public Object refund(@NotNull String id) {
        CarServiceAfterSale aftersale = aftersaleService.findById(id);
        if (aftersale == null) {
            return ResponseUtil.badArgumentValue();
        }

        if (!aftersale.getStatus().equals(AftersaleStatus.STATUS_RECEPT.getStatus())) {
            return ResponseUtil.fail("售后不能进行退款操作");
        }

        CarServiceOrder order = orderService.findById(aftersale.getOrderId());
        if (order == null) {
            return ResponseUtil.badArgument();
        }

        //如果订单金额为零则跳过退款接口直接修改订单状态
        if (order.getActualPrice().compareTo(BigDecimal.ZERO) > 0) {
            WxPayRefundResult refundResult = wxPayRefundService.wxPayAftersaleRefund(order, aftersale);
            if (refundResult != null) {
                order.setRefundContent(refundResult.getRefundId());
            }
        } else {
            order.setRefundContent("线下付款退款");
        }

        //修改售后信息
        aftersale.setStatus(AftersaleStatus.STATUS_REFUND.getStatus());
        aftersale.setHandleTime(LocalDateTime.now());
        if (aftersaleService.updateVersionSelective(aftersale) == 0) {
            return ResponseUtil.fail("售后更新失败");
        }

        // 记录订单退款相关信息
        order.setRefundType("微信退款接口");
        order.setRefundTime(LocalDateTime.now());
        order.setRefundAmount(order.getActualPrice());
        order.setAftersaleStatus(AftersaleStatus.STATUS_REFUND.getStatus());
        //修改订单状态
        order.setOrderStatus(OrderStatus.STATUS_FINISH_AFTERSALE.getStatus());
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }

        // 返还订单
        orderCoreService.orderRelease(order);

        //订单退款订阅通知
        CarServiceUser user = userService.findById(order.getUserId());
        subscribeMessageService.refundSubscribe(user.getOpenid(), order);

        //记录操作日志
        ActionLogHandler.logOrderSucceed("退款", "订单编号 " + order.getOrderSn());
        return ResponseUtil.ok();
    }


}
