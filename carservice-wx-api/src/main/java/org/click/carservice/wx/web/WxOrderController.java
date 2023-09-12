package org.click.carservice.wx.web;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.annotation.JsonBody;
import org.click.carservice.core.redis.annotation.RequestRateLimiter;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.order.body.*;
import org.click.carservice.wx.web.impl.WxWebOrderService;
import org.redisson.api.RateIntervalUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单服务
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/wx/order")
@Validated
@Api(value = "微信-订单", tags = "微信-订单")
public class WxOrderController {

    @Autowired
    private WxWebOrderService orderService;

    /**
     * 订单列表
     */
    @GetMapping("list")
    @ApiOperation(value = "查看用户订单列表")
    public Object list(@LoginUser String userId, OrderListBody body) {
        return orderService.list(userId, body);
    }

    /**
     * 订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("detail")
    @ApiOperation(value = "查看用户订单详情")
    public Object detail(@LoginUser String userId, @NotNull String orderId) {
        return orderService.detail(userId, orderId);
    }

    /**
     * 提交订单
     *
     * @param userId 用户ID
     * @param body   订单信息，{ cartId：xxx, addressId: xxx, couponId: xxx, message: xxx, grouponRulesId: xxx,  grouponLinkId: xxx}
     * @return 提交订单操作结果
     */
    @PostMapping("submit")
    @ApiOperation(value = "提交订单")
    @RequestRateLimiter(rate = 1, rateInterval = 3, timeUnit = RateIntervalUnit.SECONDS , errMsg = "你有一笔相同订单已提交，请等待")
    public Object submit(@LoginUser String userId, @Valid @RequestBody OrderSubmitBody body) {
        return orderService.submit(userId, body);
    }


    /**
     * 取消订单
     *
     * @param userId    用户ID
     * @param orderId   订单ID
     * @return 取消订单操作结果
     */
    @PostMapping("cancel")
    @ApiOperation(value = "取消订单")
    public Object cancel(@LoginUser String userId, @JsonBody String orderId) {
        return orderService.cancel(userId, orderId);
    }

    /**
     * 付款订单的预支付会话标识
     *
     * @param userId    用户ID
     * @param orderIds  订单ID列表
     * @return 支付订单ID
     */
    @PostMapping("prepay")
    @ApiOperation(value = "付款订单的预支付")
    public Object prepay(@LoginUser String userId, @JsonBody List<String> orderIds, HttpServletRequest request) {
        return orderService.prepay(userId, orderIds, request);
    }

    /**
     * 微信付款成功或失败回调接口
     * <p>
     *  TODO
     *  注意，这里pay-notify是示例地址，建议开发者应该设立一个隐蔽的回调地址
     *
     * @param request 请求内容
     * @return 操作结果
     */
    @PostMapping("pay-status")
    @ApiOperation(value = "微信付款成功或失败回调接口")
    public Object payNotify(HttpServletRequest request) {
        return orderService.payNotify(request);
    }


    /**
     * 订单申请退款
     *
     * @param userId 用户ID
     * @param orderId   订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    @PostMapping("refund")
    @ApiOperation(value = "订单申请退款")
    public Object refund(@LoginUser String userId, @JsonBody String orderId) {
        return orderService.refund(userId, orderId);
    }


    /**
     * 删除订单
     *
     * @param userId 用户ID
     * @param orderId   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @PostMapping("delete")
    @ApiOperation(value = "删除订单")
    public Object delete(@LoginUser String userId, @JsonBody String orderId) {
        return orderService.delete(userId, orderId);
    }

    /**
     * 待评价订单商品信息
     *
     * @param userId  用户ID
     * @param goodsId 订单商品ID
     * @return 待评价订单商品信息
     */
    @GetMapping("goods")
    @ApiOperation(value = "待评价订单商品信息")
    public Object goods(@LoginUser String userId, @NotNull String goodsId) {
        return orderService.goods(userId, goodsId);
    }

    /**
     * 评价订单商品
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @PostMapping("comment")
    @ApiOperation(value = "评价订单商品")
    public Object comment(@LoginUser String userId, @Valid @RequestBody OrderCommentBody body) {
        return orderService.comment(userId, body);
    }

    /**
     * 订单退款
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    @PostMapping("admin/refund")
    @ApiOperation(value = "订单退款")
    public Object adminRefund(@LoginUser String userId, @Valid @RequestBody OrderAdminRefundBody body) {
        return orderService.adminRefund(userId , body);
    }

    /**
     * 商家取消订单
     *
     * @param orderId 订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    @PostMapping("admin/cancel")
    @ApiOperation(value = "商家取消订单")
    public Object adminCancel( @JsonBody String orderId) {
        return orderService.adminCancel(orderId);
    }

    /**
     * 订单使用,订单待使用-》订单待验收
     *
     * @return 订单操作结果
     */
    @PostMapping("admin/Use")
    @ApiOperation(value = "订单使用")
    public Object adminUse(@JsonBody String orderId) {
        return orderService.adminUse(orderId);
    }
    /**
     * 订单使用后，用户验收确认收货
     *
     * @param orderId   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @PostMapping("confirm")
    @ApiOperation(value = "订单核销-订单使用后，用户验收确认收货")
    public Object confirm( @JsonBody String orderId, @RequestParam("file") MultipartFile file) {
        return orderService.confirm(orderId,file);
    }
    /**
     * 订单生成二维码
     *
     * @param orderId   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @PostMapping("createQrcode")
    @ApiOperation(value = "订单生成二维码")
    public Object createQrcode( @JsonBody String orderId) {
        return orderService.createQrcode(orderId);
    }
    @PostMapping("getQrcode")
    @ApiOperation(value = "获取订单二维码")
    public Object getQrcode( @JsonBody String orderId) {
        return orderService.getQrcode(orderId);
    }
}