package org.click.carservice.wx.web.impl;
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

import cn.hutool.core.bean.BeanUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.click.carservice.core.express.service.ExpressService;
import org.click.carservice.core.handler.ActionLogHandler;
import org.click.carservice.core.notify.service.NotifyMailService;
import org.click.carservice.core.service.*;
import org.click.carservice.core.storage.service.StorageService;
import org.click.carservice.core.system.SystemConfig;
import org.click.carservice.core.tasks.impl.OrderCommentTask;
import org.click.carservice.core.tasks.impl.OrderUnconfirmedTask;
import org.click.carservice.core.tasks.impl.OrderUnpaidTask;
import org.click.carservice.core.tasks.service.TaskService;
import org.click.carservice.core.tenant.handler.TenantContextHolder;
import org.click.carservice.core.utils.JacksonUtil;
import org.click.carservice.core.utils.QRCodeUtil;
import org.click.carservice.core.utils.ip.IpUtil;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.core.weixin.service.SubscribeMessageService;
import org.click.carservice.core.weixin.service.WxPayRefundService;
import org.click.carservice.core.weixin.service.WxSecCheckService;
import org.click.carservice.db.domain.*;
import org.click.carservice.db.entity.OrderHandleOption;
import org.click.carservice.db.entity.UserInfo;
import org.click.carservice.db.enums.GrouponRuleStatus;
import org.click.carservice.db.enums.OrderStatus;
import org.click.carservice.db.service.ICarServiceOrderVerificationService;
import org.click.carservice.wx.model.order.body.OrderAdminRefundBody;
import org.click.carservice.wx.model.order.body.OrderCommentBody;
import org.click.carservice.wx.model.order.body.OrderListBody;
import org.click.carservice.wx.model.order.body.OrderSubmitBody;
import org.click.carservice.wx.model.order.result.AttachResult;
import org.click.carservice.wx.model.order.result.OrderDetailResult;
import org.click.carservice.wx.model.order.result.OrderInfo;
import org.click.carservice.wx.model.order.result.OrderSubmitResult;
import org.click.carservice.wx.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * 订单服务
 *
 * <p>
 * 订单状态：
 * 101 订单生成，未支付；102，下单后未支付用户取消；103，下单后未支付超时系统自动取消
 * 201 支付完成，商家未发货；202，订单生产，已付款未发货，但是退款取消；
 * 301 商家发货，用户未确认；
 * 401 用户确认收货； 402 用户没有确认收货超过一定时间，系统自动确认收货；
 *
 * <p>
 * 用户操作：
 * 当101用户未付款时，此时用户可以进行的操作是取消订单，或者付款操作
 * 当201支付完成而商家未发货时，此时用户可以取消订单并申请退款
 * 当301商家已发货时，此时用户可以有确认收货的操作
 * 当401用户确认收货以后，此时用户可以进行的操作是删除订单，评价商品，申请售后，或者再次购买
 * 当402系统自动确认收货以后，此时用户可以删除订单，评价商品，申请售后，或者再次购买
 *
 * @author click
 */
@Slf4j
@Service
public class WxWebOrderService {

    @Autowired
    private WxSecCheckService secCheckService;
    @Autowired
    private WxUserService userService;
    @Autowired
    private WxOrderService orderService;
    @Autowired
    private NotifyCoreService notifyCoreService;
    @Autowired
    private OrderRandomCode orderRandomCode;
    @Autowired
    private WxBrandService brandService;
    @Autowired
    private WxOrderGoodsService orderGoodsService;
    @Autowired
    private WxCarService carService;
    @Autowired
    private SubscribeMessageService subscribeMessageService;
    @Autowired
    private WxCartService cartService;
    @Autowired
    private WxPayRefundService wxPayRefundService;
    @Autowired
    private DealingSlipCoreService slipCoreService;
    @Autowired
    private WxShareService shareService;
    @Autowired
    private NotifyMailService mailService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private WxGrouponRulesService grouponRulesService;
    @Autowired
    private WxGrouponService grouponService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private WxGoodsCommentService commentService;
    @Autowired
    private CouponVerifyService couponVerifyService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private WxAftersaleService aftersaleService;
    @Autowired
    private OrderCoreService orderCoreService;
    @Autowired
    private WxRewardService rewardService;
    @Autowired
    private RewardCoreService rewardCoreService;
    @Autowired
    private StorageService storageService;

    @Autowired
    private ICarServiceOrderVerificationService iCarServiceOrderVerificationService;

    @Autowired
    private WxGoodsService goodsService;


    /**
     * 订单列表
     */
    public Object list(String userId, OrderListBody body) {
        List<Short> orderStatus = OrderStatus.orderStatus(body.getShowType());
        List<CarServiceOrder> orderList = orderService.queryByOrderStatus(userId, orderStatus, body);
        List<org.ysling.CarService.wx.model.order.result.OrderListResult> orderVoList = new ArrayList<>(orderList.size());
        for (CarServiceOrder order : orderList) {
            org.ysling.CarService.wx.model.order.result.OrderListResult orderVo = new org.ysling.CarService.wx.model.order.result.OrderListResult();
            BeanUtil.copyProperties(order, orderVo);
            orderVo.setOrderStatusText(OrderStatus.orderStatusText(order));
            orderVo.setHandleOption(OrderStatus.build(order));
            orderVo.setOrderGoods(orderGoodsService.findByOrderId(order.getId()));
            CarServiceGroupon groupon = grouponService.findByOrderId(order.getId());
            orderVo.setIsGroupon(groupon != null);
            if (groupon != null) {
                orderVo.setGroupon(groupon);
                CarServiceGrouponRules grouponRules = grouponRulesService.findById(groupon.getRulesId());
                orderVo.setGrouponStatus(GrouponRuleStatus.parseValue(grouponRules.getStatus()));
            }
            orderVoList.add(orderVo);
        }
        return ResponseUtil.okList(orderVoList, orderList);
    }


    /**
     * 订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    public Object detail(String userId, String orderId) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }
        // 订单信息
        CarServiceOrder order = orderService.findById(userId, orderId);
        if (order == null) {
            CarServiceBrand brand = brandService.findByUserId(userId);
            if (brand == null) {
                return ResponseUtil.fail("订单不存在");
            }
            order = orderService.findByBrandId(brand.getId(), orderId);
            if (order == null) {
                return ResponseUtil.fail("订单不存在");
            }
        }
        if (order.getCarId() == null) {
            return ResponseUtil.fail("订单信息异常，车辆不存在");
        }

        OrderInfo orderInfo = new OrderInfo();
        BeanUtil.copyProperties(order, orderInfo);
        orderInfo.setOrderStatusText(OrderStatus.orderStatusText(order));
        orderInfo.setHandleOption(OrderStatus.build(order));
        orderInfo.setExpCode(order.getShipChannel());
        orderInfo.setExpName(expressService.getVendorName(order.getShipChannel()));
        orderInfo.setExpNo(order.getShipSn());
        if (!Objects.equals(order.getShipChannel(), "ZS") && StringUtils.hasText(order.getShipSn())) {
            orderInfo.setExpSuccess(true);
        }

        OrderDetailResult result = new OrderDetailResult();
        result.setOrderInfo(orderInfo);
        result.setGrouponBasics((short) 0);
        result.setOrderBasics(OrderStatus.orderBasics(order));
        result.setOrderGoods(orderGoodsService.findByOrderId(order.getId()));
        //团购信息
        CarServiceGroupon groupon = grouponService.findByOrderId(order.getId());
        if (groupon != null) {
            CarServiceGrouponRules rules = grouponRulesService.findById(groupon.getRulesId());

            // 获取团购加入id
            String grouponId = groupon.getGrouponId();
            String linkGrouponId = "0".equals(grouponId) ? groupon.getId() : groupon.getGrouponId();

            //参与记录
            UserInfo creator = userService.findUserVoById(groupon.getCreatorUserId());
            List<UserInfo> joiners = new ArrayList<>();
            joiners.add(creator);
            List<CarServiceGroupon> grouponList = grouponService.queryJoinRecord(linkGrouponId);
            for (CarServiceGroupon grouponItem : grouponList) {
                joiners.add(userService.findUserVoById(grouponItem.getUserId()));
            }

            result.setGrouponBasics(groupon.getStatus() > 3 ? -1 : groupon.getStatus());
            result.setLinkGrouponId(linkGrouponId);
            result.setCreator(creator);
            result.setJoiners(joiners);
            result.setGroupon(groupon);
            result.setRules(rules);
        }
        return ResponseUtil.ok(result);
    }

    /**
     * 提交订单
     * <p>
     * 1. 创建订单表项和订单商品表项;
     * 2. 购物车清空;
     * 3. 优惠券设置已用;
     * 4. 商品货品库存减少;
     * 5. 如果是团购商品，则创建团购活动表项。
     *
     * @param userId 用户ID
     * @param body   订单信息，{ cartId：xxx, addressId: xxx, couponId: xxx, message: xxx, grouponRulesId: xxx,  grouponLinkId: xxx}
     * @return 提交订单操作结果
     */
    public Object submit(String userId, OrderSubmitBody body) {
        String cartId = body.getCartId();
        String message = body.getMessage();
        String couponId = body.getCouponId();
//        String addressId = body.getAddressId();
        String carId = body.getCarId();
        String userCouponId = body.getUserCouponId();
        String rewardLinkId = body.getRewardLinkId();
        String grouponLinkId = body.getGrouponLinkId();
        String grouponRulesId = body.getGrouponRulesId();
        String mobile = body.getMobile();

        //如果是团购项目,验证活动是否有效
        if (grouponRulesId != null && !"0".equals(grouponRulesId)) {
            Object groupon = grouponService.isGroupon(grouponRulesId, grouponLinkId, userId);
            if (groupon != null) {
                return groupon;
            }
        }

        //判断是否存在赏金活动
        if (rewardLinkId != null && !"0".equals(rewardLinkId)) {
            CarServiceReward reward = rewardService.findById(rewardLinkId);
            if (reward == null) {
                return ResponseUtil.badArgument();
            }
            Object serviceReward = rewardCoreService.isReward(reward.getTaskId());
            if (serviceReward != null) {
                return serviceReward;
            }
        }

        //判断参数是否错误
        if (cartId == null || mobile == null || couponId == null) {
            return ResponseUtil.badArgument();
        }

//        // 收货地址
//        CarServiceAddress checkedAddress = addressService.query(userId, addressId);
//        if (checkedAddress == null) {
//            return ResponseUtil.badArgument();
//        }

        //选中的商品
        List<CarServiceCart> checkedGoodsList = cartService.getCheckedGoods(userId, cartId);
        if (checkedGoodsList == null) {
            return ResponseUtil.badArgument();
        }

        // 获取可用的优惠券信息,使用优惠券减免的金额
        BigDecimal couponPrice = BigDecimal.valueOf(0);
        // 所以分类的商品id
        List<String> goodsTypeIds = new ArrayList<>();
        // 如果couponId=0则没有优惠券，couponId=-1则不使用优惠券
        if (!"0".equals(couponId) && !"-1".equals(couponId)) {
            CarServiceCoupon coupon = couponVerifyService.checkCoupon(userId, couponId, userCouponId, carId, checkedGoodsList);
            if (coupon == null) {
                return ResponseUtil.badArgumentValue();
            } else {
                // 所有分类的商品id
                goodsTypeIds = Arrays.asList(coupon.getGoodsIds());
            }
        }

        //获取当前用户信息
        CarServiceUser user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        //订单总价
        BigDecimal allPrice = BigDecimal.valueOf(0);
        //保存订单id集合
        ArrayList<String> orderIds = new ArrayList<>();
        //商户订单号
        String outTradeNo = orderRandomCode.generateOutTradeNo(userId);
        // 添加订单商品表项
        for (CarServiceCart cartGoods : checkedGoodsList) {
            // 创建订单
            CarServiceOrder order = new CarServiceOrder();
            order.setUserId(userId);
            order.setMessage(message);
            order.setOutTradeNo(outTradeNo);
            order.setGoodsId(cartGoods.getGoodsId());
            order.setBrandId(cartGoods.getBrandId());
            order.setMobile(mobile);
//            order.setConsignee(checkedAddress.getName());
            order.setBrandId(cartGoods.getBrandId());
//            order.setAddress(checkedAddress.getAddressAll());
            order.setCarId(carId);
            order.setOrderStatus(OrderStatus.STATUS_CREATE.getStatus());
            //订单编号
            order.setOrderSn(orderRandomCode.generateOrderSn(userId));
            //团购金额
            CarServiceGrouponRules grouponRules = grouponRulesService.findById(grouponRulesId);
            //团购价格默认为零如果有团购直接相加
            order.setGrouponPrice(grouponRules == null ? BigDecimal.valueOf(0) : grouponRules.getDiscount());
//            // 如果没有服务类商品
//            //将优惠券金额分担给每件商品
//            if (BigDecimal.valueOf(-1).equals(couponPrice)) {
//                checkedGoodsList.stream().forEach();
//            } else {
//                order.setCouponPrice(couponPrice.divide(BigDecimal.valueOf(checkedGoodsList.size()), 2, RoundingMode.HALF_UP));
//            }
//            // 如果有则对应服务使用优惠券
            // 判断当前商品的种类是否与可使用优惠券的商品种类一致
            String goodsId = cartGoods.getGoodsId();
            CarServiceGoods dbGoods = goodsService.findById(goodsId);
            String typeId = dbGoods.getCategoryId();
            // 一致则使用优惠券
            if (goodsTypeIds.contains(typeId)) {
                // 设置优惠券价格就是商品的价格
                order.setCouponPrice(cartGoods.getPrice());
            } else {
                // 如果使用了优惠券但是优惠券不适用于此商品还是需要报错，防止前端未验证
                if (!"0".equals(couponId) && !"-1".equals(couponId)) {
                    throw new RuntimeException("优惠券异常，优惠券不适用当前商品!");
                }
                order.setCouponPrice(BigDecimal.ZERO);
            }
            //添加订单
            orderCoreService.addOrderAndOrderGoods(cartGoods, order, user);

            //减少库存
            orderCoreService.reduceStock(cartGoods);

            //添加分享记录
            shareService.addShare(user, order);

            //添加赏金活动
            rewardCoreService.addReward(rewardLinkId, userId, order);

            //如果是团购项目，添加团购信息
            grouponService.addGroupon(order.getId(), userId, grouponRulesId, grouponLinkId);

            // 如果couponId=0则没有优惠券，couponId=-1则不使用优惠券
            commonService.usedCoupon(couponId, userCouponId, order.getId());

            // NOTE: 建议开发者从业务场景核实下面代码，防止用户利用业务BUG使订单跳过支付环节。
            // 如果订单实际支付费用是0，则直接跳过支付变成待发货状态-----------单独
            if (order.getActualPrice().compareTo(BigDecimal.ZERO) <= 0 || !SystemConfig.isAutoPay()) {
                //下单成功
                orderCoreService.orderPaySuccess(order);
            } else {
                //计算订单总金额
                allPrice = allPrice.add(order.getActualPrice());
                //将订单id加入集合返回出去
                orderIds.add(order.getId());
                // 订单支付超期任务
                taskService.addTask(new OrderUnpaidTask(order));
            }
        }

        OrderSubmitResult result = new OrderSubmitResult();
        result.setIsPay(allPrice.compareTo(BigDecimal.ZERO) <= 0 || !SystemConfig.isAutoPay());
        result.setOrderIds(orderIds);
        return ResponseUtil.ok(result);
    }

    /**
     * 付款订单的预支付会话标识
     * <p>
     * 1. 检测当前订单是否能够付款
     * 2. 微信商户平台返回支付订单ID
     * 3. 设置订单付款状态
     *
     * @param userId   用户ID
     * @param orderIds 订单ID列表
     * @return 支付订单ID
     */
    public Object prepay(String userId, List<String> orderIds, HttpServletRequest request) {
        AttachResult attach = new AttachResult();
        attach.setTenantId(TenantContextHolder.getLocalTenantId());
        attach.setOrderIds(orderIds);
        String toJson = JacksonUtil.toJson(attach);
        if (toJson == null || toJson.length() > 128) {
            return ResponseUtil.fail("支付商品超限，请减少支付商品");
        }

        //获取当前用户
        CarServiceUser user = userService.findById(userId);
        if (user == null || user.getOpenid() == null) {
            return ResponseUtil.fail("未找到用户");
        }

        //订单支付总金额
        BigDecimal allPrice = BigDecimal.valueOf(0);
        //保存验证后的订单
        ArrayList<CarServiceOrder> orders = new ArrayList<>();
        //检查订单
        for (String orderId : orderIds) {
            if (orderId == null) {
                return ResponseUtil.fail("未找到订单");
            }

            //获取订单
            CarServiceOrder order = orderService.findById(userId, orderId);
            if (order == null) {
                return ResponseUtil.fail("未找到订单");
            }

            // 检测是否能够取消
            OrderHandleOption handleOption = OrderStatus.build(order);
            if (!handleOption.isPay()) {
                return ResponseUtil.fail("订单不能支付");
            }

            orders.add(order);
            //订单金额相加
            allPrice = allPrice.add(order.getActualPrice());
        }

        WxPayMpOrderResult result;
        try {
            //支付请求参数
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            //商户订单号
            String tradeNo = orders.get(0).getOutTradeNo();
            String orderSn = orders.get(0).getOrderSn();
            String outTradeNo = orders.size() > 1 ? tradeNo : orderSn;
            //将订单id集合传入微信支付自定义参数限制长度128该参数会在回调原样返回
            orderRequest.setAttach(toJson);
            orderRequest.setBody(outTradeNo);
            orderRequest.setOutTradeNo(outTradeNo);
            orderRequest.setOpenid(user.getOpenid());

            // 总支付金额元转成分
            int totalFee = allPrice.multiply(BigDecimal.valueOf(100)).intValue();
            orderRequest.setTotalFee(totalFee);
            orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));
            result = wxPayService.createOrder(orderRequest);
            // 添加操作日志
            ActionLogHandler.logOrderSucceed("订单预支付", "{订单编号：" + outTradeNo + "}{支付金额：" + allPrice + "}");
        } catch (WxPayException e) {
            //给管理员发送订单支付错误邮件
            mailService.notifyMail("订单支付异常-订单不能支付", e.getXmlString());
            // 添加操作日志
            ActionLogHandler.logOrderSucceed("订单预支付", "订单支付异常-订单不能支付", e.getXmlString());
            return ResponseUtil.fail("订单不能支付");
        }

        for (CarServiceOrder order : orders) {
            if (orderService.updateVersionSelective(order) == 0) {
                throw new RuntimeException("网络繁忙，请刷新重试");
            }
        }

        return ResponseUtil.ok(result);
    }

    /**
     * 微信付款成功或失败回调接口
     * <p>
     * 1. 检测当前订单是否是付款状态;
     * 2. 设置订单付款成功状态相关信息;
     * 3. 响应微信商户平台.
     *
     * @param request 请求内容
     * @return 操作结果
     */
    public Object payNotify(HttpServletRequest request) {
        String xmlResult;
        try {
            xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        } catch (IOException e) {
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        WxPayOrderNotifyResult result;
        try {
            result = wxPayService.parseOrderNotifyResult(xmlResult);
            if (!WxPayConstants.ResultCode.SUCCESS.equals(result.getResultCode())) {
                // 添加操作日志
                ActionLogHandler.logOrderFail("支付回调", xmlResult);
                return WxPayNotifyResponse.fail("微信通知支付失败！");
            }
            if (!WxPayConstants.ResultCode.SUCCESS.equals(result.getReturnCode())) {
                // 添加操作日志
                ActionLogHandler.logOrderFail("支付回调", xmlResult);
                return WxPayNotifyResponse.fail("微信通知支付失败！");
            }
        } catch (WxPayException e) {
            ActionLogHandler.logOrderFail("支付回调失败", e.getXmlString());
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        String tenantId = JacksonUtil.parseString(result.getAttach(), "tenantId");
        TenantContextHolder.setLocalTenantId(tenantId);
        if (tenantId == null) {
            ActionLogHandler.logOrderFail("支付回调", "未找到当前租户");
            return WxPayNotifyResponse.fail("未找到当前租户");
        }

        //获取自定义参数
        List<String> orderIds = JacksonUtil.parseStringList(result.getAttach(), "orderIds");
        if (orderIds == null || orderIds.size() <= 0) {
            log.error("自定义参数异常" + result);
            ActionLogHandler.logOrderFail("支付回调", "自定义参数异常" + result);
            return WxPayNotifyResponse.fail("自定义参数异常");
        }

        //订单总支付金额
        BigDecimal allPrice = BigDecimal.valueOf(0);
        //保存验证后的订单
        ArrayList<CarServiceOrder> orders = new ArrayList<>();
        //判断所有订单id是否正常并保存订单信息
        for (String orderId : orderIds) {
            //查询订单
            CarServiceOrder order = orderService.findById(orderId);
            if (order == null) {
                log.error("订单不存在 =" + orderId);
                ActionLogHandler.logOrderFail("支付回调", "订单不存在 =" + orderId);
                continue;
            }

            // 检查这个订单是否已经处理过
            if (OrderStatus.hasPayed(order) || !OrderStatus.isPayed(order)) {
                log.error(orderId + "=订单已经处理!");
                ActionLogHandler.logOrderFail("支付回调", orderId + "=订单已经处理!");
                continue;
            }

            orders.add(order);
            //订单金额相加得订单总支付金额
            allPrice = allPrice.add(order.getActualPrice());
        }

        // 分转化成元
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
        // 检查支付订单金额
        if (!totalFee.equals(allPrice.toString())) {
            log.error(result.getAttach() + " : 支付金额不符合 totalFee=" + totalFee);
            ActionLogHandler.logOrderFail("支付回调", result.getAttach() + " : 支付金额不符合 totalFee=" + totalFee);
            return WxPayNotifyResponse.fail(result.getAttach() + " : 支付金额不符合 totalFee=" + totalFee);
        }

        //更新订单信息
        for (CarServiceOrder order : orders) {
            //添加支付信息,添加订单联合支付总费用orderPrice
            order.setOrderPrice(allPrice);
            order.setPayId(result.getTransactionId());
            order.setOrderStatus(OrderStatus.STATUS_PAY.getStatus());
            orderCoreService.orderPaySuccess(order);
        }

        // 添加操作日志
        ActionLogHandler.logOrderSucceed("支付回调成功", "{订单编号：" + result.getOutTradeNo() + "}{支付金额：" + totalFee + "}");
        return WxPayNotifyResponse.success("处理成功!");
    }

    /**
     * 取消订单
     * <p>
     * 1. 检测当前订单是否能够取消；
     * 2. 设置订单取消状态；
     * 3. 商品货品库存恢复；
     * 4. 返还优惠券；
     *
     * @param userId  用户ID
     * @param orderId 订单信息，{ orderId：xxx }
     * @return 取消订单操作结果
     */
    public Object cancel(String userId, String orderId) {
        CarServiceOrder order = orderService.findById(userId, orderId);
        if (order == null) {
            return ResponseUtil.badArgumentValue();
        }

        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        // 检测是否能够取消
        OrderHandleOption handleOption = OrderStatus.build(order);
        if (!handleOption.isCancel()) {
            return ResponseUtil.fail("订单不能取消");
        }

        // 设置订单已取消状态
        order.setOrderStatus(OrderStatus.STATUS_USER_CANCEL.getStatus());
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }

        // 返还订单
        orderCoreService.orderRelease(order);
        return ResponseUtil.ok();
    }

    /**
     * 订单申请退款
     * <p>
     * 1. 检测当前订单是否能够退款；
     * 2. 设置订单申请退款状态。
     *
     * @param userId  用户ID
     * @param orderId 订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    public Object refund(String userId, String orderId) {
        CarServiceOrder order = orderService.findById(userId, orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }

        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        OrderHandleOption handleOption = OrderStatus.build(order);
        if (!handleOption.isRefund()) {
            return ResponseUtil.fail("订单不能取消");
        }

        // 设置订单申请退款状态
        order.setOrderStatus(OrderStatus.STATUS_REFUND.getStatus());
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }

        //给商家发送通知
        notifyCoreService.orderRefundNotify(order);

        return ResponseUtil.ok();
    }

    /**
     * 确认收货
     * <p>
     * 1. 检测当前订单是否能够确认收货；
     * 2. 设置订单确认收货状态。
     *
     * @param orderId 订单信息，{ orderId：xxx }
     * @param file    核销照片，MultipartFile
     * @return 订单操作结果
     */
    public Object confirm(String orderId, @RequestParam("file") MultipartFile file) {
        //获取订单
        CarServiceOrder order = orderService.findById(orderId);
        CarServiceOrderVerification carServiceOrderVerification = new CarServiceOrderVerification();
        if (order == null) {
            return ResponseUtil.fail("未找到订单");
        }

        //判断订单状态
        OrderHandleOption handleOption = OrderStatus.build(order);
        if (!handleOption.isConfirm() || !OrderStatus.isShipStatus(order)) {
            return ResponseUtil.fail("订单不能使用");
        }

        //获取商品信息
        CarServiceOrderGoods orderGoods = orderGoodsService.findByOrderId(orderId);
        if (orderGoods == null) {
            return ResponseUtil.fail("商品不存在");
        }

        //获取店铺信息
        CarServiceBrand brand = brandService.findById(order.getBrandId());
        if (brand == null) {
            return ResponseUtil.fail("店铺信息获取失败");
        }
        //存放照片到数据库
        String storageId = storageService.store(file).getId();
        //更新订单
        Integer comments = orderGoodsService.getComments(orderId);
        order.setComments(comments.shortValue());
        order.setOrderStatus(OrderStatus.STATUS_CONFIRM.getStatus());
        order.setQrcode(null);
        order.setConfirmTime(LocalDateTime.now());
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("订单数据失效");
        }
        //保存订单核销记录
        carServiceOrderVerification.setUserId(order.getUserId());
        carServiceOrderVerification.setGoodsId(order.getGoodsId());
        carServiceOrderVerification.setOrderSn(order.getId());
        carServiceOrderVerification.setAddress(order.getAddress());
        carServiceOrderVerification.setVerificationTime(LocalDateTime.now());
        carServiceOrderVerification.setStorageId(storageId);
        iCarServiceOrderVerificationService.add(carServiceOrderVerification);

        //删除确认收货定时任务
        taskService.removeTask(new OrderUnconfirmedTask(order));

        //添加评论超时定时任务
        taskService.addTask(new OrderCommentTask(order));

        //获取店铺所有者向店家发放余额
//        slipCoreService.addOrderIntegral(order , brand);
        return ResponseUtil.ok();
    }

    /**
     * 删除订单
     * <p>
     * 1. 检测当前订单是否可以删除；
     * 2. 删除订单。
     *
     * @param userId  用户ID
     * @param orderId 订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    public Object delete(String userId, String orderId) {
        CarServiceOrder order = orderService.findById(userId, orderId);
        if (order == null) {
            return ResponseUtil.fail("未找到订单");
        }

        OrderHandleOption handleOption = OrderStatus.build(order);
        if (!handleOption.isDelete()) {
            return ResponseUtil.fail("订单不能删除");
        }

        // 订单order_status没有字段用于标识删除,而是存在专门的delete字段表示是否删除
        orderService.deleteById(orderId);
        // 删除订单商品信息
        orderGoodsService.deleteByOrderId(orderId);
        // 售后也同时删除
        aftersaleService.deleteByOrderId(userId, orderId);
        return ResponseUtil.ok();
    }

    /**
     * 待评价订单商品信息
     *
     * @param userId  用户ID
     * @param goodsId 订单商品ID
     * @return 待评价订单商品信息
     */
    public Object goods(String userId, String goodsId) {
        CarServiceOrderGoods orderGoods = orderGoodsService.findById(goodsId);
        if (orderGoods != null) {
            String orderId = orderGoods.getOrderId();
            CarServiceOrder order = orderService.findById(orderId);
            if (!order.getUserId().equals(userId)) {
                return ResponseUtil.badArgument();
            }
        }
        return ResponseUtil.ok(orderGoods);
    }

    /**
     * 评价订单商品
     * <p>
     * 确认商品收货或者系统自动确认商品收货后7天内可以评价，过期不能评价。
     *
     * @param userId 用户ID
     * @param body   订单商品信息
     * @return 订单操作结果
     */
    public Object comment(String userId, OrderCommentBody body) {
        CarServiceOrderGoods orderGoods = orderGoodsService.findById(body.getGoodsId());
        if (orderGoods == null) {
            return ResponseUtil.badArgumentValue();
        }
        CarServiceOrder order = orderService.findById(userId, orderGoods.getOrderId());
        if (order == null) {
            return ResponseUtil.badArgumentValue();
        }
        if (!OrderStatus.isConfirmStatus(order) && !OrderStatus.isAutoConfirmStatus(order)) {
            return ResponseUtil.fail("当前商品不能评价");
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.fail("当前商品不属于用户");
        }
        Integer commentId = orderGoods.getComment();
        if (commentId == -1 || OrderStatus.isCommentOvertimeStatus(order)) {
            return ResponseUtil.fail("当前商品评价时间已经过期");
        }
        if (commentId != 0 || OrderStatus.isOrderSucceedStatus(order)) {
            return ResponseUtil.fail("订单商品已评价");
        }

        String content = body.getContent();
        if (!StringUtils.hasText(content)) {
            return ResponseUtil.fail("评论内容不能为空");
        }
        Integer star = body.getStar();
        if (star == null || star < 0 || star > 5) {
            return ResponseUtil.badArgumentValue();
        }
        Boolean hasPicture = body.getHasPicture();
        List<String> picUrls = body.getPicUrls();
        if (hasPicture == null || !hasPicture || picUrls == null) {
            picUrls = new ArrayList<>(0);
        }

        //文本校验
        CarServiceUser user = userService.findById(userId);
        secCheckService.checkMessage(user.getOpenid(), content);

        // 1. 创建评价
        CarServiceGoodsComment comment = new CarServiceGoodsComment();
        comment.setUserId(userId);
        comment.setGoodsId(orderGoods.getGoodsId());
        comment.setStar(star.shortValue());
        comment.setContent(content);
        comment.setHasPicture(hasPicture);
        comment.setPicUrls(picUrls.toArray(new String[]{}));
        commentService.add(comment);

        // 2. 更新订单商品的评价列表
        orderGoods.setComment(200);
        if (orderGoodsService.updateVersionSelective(orderGoods) == 0) {
            throw new RuntimeException("商品评论更新失败");
        }

        // 3. 更新订单中未评价的订单商品可评价数量
        Short commentCount = order.getComments();
        if (commentCount > 0) {
            commentCount--;
        }

        //判断是否还有待评论订单
        if (commentCount == 0) {
            order.setOrderStatus(OrderStatus.ORDER_SUCCEED.getStatus());
        }

        order.setComments(commentCount);
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }

        return ResponseUtil.ok();
    }

    /**
     * 订单退款
     * <p>
     * 1. 检测当前订单是否能够退款;
     * 2. 微信退款操作;
     * 3. 设置订单退款确认状态；
     * 4. 订单商品库存回库。
     * <p>
     * TODO
     * 虽然接入了微信退款API，但是从安全角度考虑，建议开发者删除这里微信退款代码，采用以下两步走步骤：
     * 1. 管理员登录微信官方支付平台点击退款操作进行退款
     * 2. 管理员登录CarService管理后台点击退款操作进行订单状态修改和商品库存回库
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    public Object adminRefund(String userId, OrderAdminRefundBody body) {
        String orderId = body.getOrderId();
        String refundMoney = body.getRefundMoney();

        CarServiceBrand brand = brandService.findByUserId(userId);
        if (brand == null) {
            return ResponseUtil.fail("未找到店铺");
        }

        CarServiceOrder order = orderService.findByBrandId(brand.getId(), orderId);
        if (order == null) {
            return ResponseUtil.fail("未找到订单");
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
        order.setRefundType("微信直接退款");
        order.setRefundTime(LocalDateTime.now());
        order.setRefundAmount(new BigDecimal(refundMoney));
        order.setOrderStatus(OrderStatus.STATUS_REFUND_CONFIRM.getStatus());
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

    /**
     * 生成订单二维码
     * <p>
     * 1. 检测当前订单是否付款能够生成；
     * 2. 检测当前订单是否已经有二维码；
     * 3. 生成二维码；
     * 4. 返回二维码；
     *
     * @param orderId 订单信息，{ orderId：xxx }
     * @return 取消订单操作结果
     */
    public Object createQrcode(String orderId) {
        CarServiceOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.fail("未找到订单");
        }
        if (order.getQrcode() != null) {
            return ResponseUtil.fail("订单已有二维码");
        }
        if (order.getOrderStatus().toString().equals(OrderStatus.STATUS_PAY.getStatus().toString()) || order.getOrderStatus().toString().equals(OrderStatus.STATUS_BTL_PAY.getStatus().toString())) {
            byte[] qrcode = QRCodeUtil.createQRCodeByte(orderId, 300);
            order.setQrcode(qrcode);
            if (orderService.updateById(order)) return ResponseUtil.ok(order.getQrcode());
            else ResponseUtil.fail("订单二维码更新失败！请联系管理员");
        }
        return ResponseUtil.fail("订单状态不是已付款");
    }

    /**
     * 获取订单二维码
     * <p>
     * 1. 检测当前订单是否付款能够生成；
     * 2. 检测当前订单是否已经有二维码；
     * 3. 生成二维码；
     * 4. 返回二维码；
     *
     * @param orderId 订单信息，{ orderId：xxx }
     * @return 取消订单操作结果
     */
    public Object getQrcode(String orderId) {
        CarServiceOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.fail("未找到订单");
        }
        if (order.getQrcode() == null) {
            return ResponseUtil.fail();
        }
        byte[] qrcode = order.getQrcode();
        return ResponseUtil.ok(qrcode);
    }

    /**
     * 管理员取消订单
     * <p>
     * 1. 检测当前订单是否能够取消；
     * 2. 设置订单取消状态；
     * 3. 商品货品库存恢复；
     * 4. 返还优惠券；
     *
     * @param orderId 订单信息，{ orderId：xxx }
     * @return 取消订单操作结果
     */
    public Object adminCancel(String orderId) {
//        CarServiceBrand brand = brandService.findByUserId(userId);
//        if (brand == null) {
//            return ResponseUtil.fail("未找到店铺");
//        }

        CarServiceOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.fail("未找到订单");
        }

        // 如果订单不是已付款状态，则不能取消
        if (!OrderStatus.hasShip(order)) {
            return ResponseUtil.fail("订单不能取消");
        }

        // 设置订单已取消状态
        if (order.getActualPrice().compareTo(BigDecimal.ZERO) > 0) {
            order.setOrderStatus(OrderStatus.STATUS_REFUND.getStatus());
        } else {
            order.setOrderStatus(OrderStatus.STATUS_BRAND_CANCEL.getStatus());
        }
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("更新数据已失效");
        }
        order.setQrcode(null);
        // 返还订单
        orderCoreService.orderRelease(order);
        return ResponseUtil.ok();
    }

    /**
     * v0发货
     * 商品扫码核销后待验收
     * 1. 检测当前订单是否能够发货
     * 2. 设置订单待验收状态
     *
     * @return 订单操作结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    public Object adminUse(String userId, String orderId) {

        CarServiceOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.fail("未找到订单");
        }
        CarServiceBrand brand = brandService.findById(order.getBrandId());
        if (brand == null || brand.getStatus() != '0') {
            return ResponseUtil.fail("未找到店铺或者店铺已禁用！");
        }
        if (brand.getUserId() != userId) {
            return ResponseUtil.fail("请对应店铺商家进行核销！");
        }
        // 如果订单不是已付款状态，则不能发货
        if (!OrderStatus.hasShip(order)) {
            return ResponseUtil.fail("订单不是已付款状态，不能使用");
        }
        CarServiceGoods goods = goodsService.findById(order.getGoodsId());
        if (goods.getIsValuable() == false) {
            order.setOrderStatus(OrderStatus.STATUS_CONFIRM.getStatus());
            order.setQrcode(null);
            //记录操作日志
            ActionLogHandler.logOrderSucceed("验收完成非高价值商品", "订单时间 " + order.getShipTime());
            return ResponseUtil.ok();
        }
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }

        order.setShipTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.STATUS_SHIP.getStatus());
        order.setQrcode(null);
        //添加确认收货定时任务
        taskService.addTask(new OrderUnconfirmedTask(order));
        //订单发货订阅通知
//        CarServiceUser user = userService.findById(order.getUserId());
//        subscribeMessageService.shipSubscribe(user.getOpenid(),order);

        //记录操作日志
        ActionLogHandler.logOrderSucceed("验收完成高价值商品", "订单时间 " + order.getShipTime());

        return ResponseUtil.ok();
    }

}
