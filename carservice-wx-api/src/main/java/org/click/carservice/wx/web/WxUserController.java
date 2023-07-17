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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.annotation.JsonBody;
import org.click.carservice.core.service.DealingSlipCoreService;
import org.click.carservice.core.service.QrcodeCoreService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.core.weixin.enums.FailReasonStatus;
import org.click.carservice.core.weixin.enums.TransferStatus;
import org.click.carservice.core.weixin.service.WxPayTransferService;
import org.click.carservice.db.domain.CarServiceDealingSlip;
import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.entity.UserInfo;
import org.click.carservice.db.enums.DealType;
import org.click.carservice.db.enums.UserLevel;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.user.result.TradingRecordResult;
import org.click.carservice.wx.model.user.result.UserIndexResult;
import org.click.carservice.wx.model.user.result.UserShareResult;
import org.click.carservice.wx.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 用户服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/user")
@Validated
public class WxUserController {

    @Autowired
    private WxUserService userService;
    @Autowired
    private WxOrderService orderService;
    @Autowired
    private WxBrandService brandService;
    @Autowired
    private WxPayTransferService wxPayTransferService;
    @Autowired
    private DealingSlipCoreService slipCoreService;
    @Autowired
    private QrcodeCoreService qCodeService;
    @Autowired
    private WxDealingSlipService dealingSlipService;
    @Autowired
    private OrderRandomCode orderRandomCode;

    /**
     * 用户个人页面数据
     * <p>
     * 目前是用户订单统计信息
     *
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("index")
    public Object index(@LoginUser String userId) {
        CarServiceUser user = userService.findById(userId);
        UserIndexResult result = new UserIndexResult();
        result.setOrder(orderService.orderInfo(userId));
        result.setBrand(brandService.findByUserId(userId));
        result.setUserLevel(UserLevel.parseValue(user.getUserLevel()));
        result.setIntegralPrice(user.getIntegral());
        return ResponseUtil.ok(result);
    }

    /**
     * 用户基本信息
     */
    @GetMapping("info")
    public Object info(@LoginUser String userId) {
        CarServiceUser user = userService.findById(userId);
        UserInfo info = new UserInfo();
        BeanUtil.copyProperties(user, info);
        return ResponseUtil.ok(info);
    }

    /**
     * 用户个人分享记录
     * <p>
     * 目前是用户订单统计信息
     *
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("share")
    public Object share(@LoginUser String userId) {
        CarServiceUser userShare = userService.findByShare(userId);
        if (!StringUtils.hasText(userShare.getShareUrl())) {
            userShare.setShareUrl(qCodeService.createUserShareQrcode(userShare));
            userService.updateSelective(userShare);
        }
        ArrayList<UserInfo> inviterList = new ArrayList<>();
        List<CarServiceUser> userList = userService.queryByInviter(userId);
        for (CarServiceUser user : userList) {
            if (!StringUtils.hasText(user.getMobile())) {
                user.setMobile("18500007139");
            }
            user.setMobile(DesensitizedUtil.mobilePhone(user.getMobile()));
            UserInfo userInfo = new UserInfo();
            BeanUtil.copyProperties(user, userInfo);
            inviterList.add(userInfo);
        }
        UserInfo userInfo = new UserInfo();
        BeanUtil.copyProperties(userShare, userInfo);
        UserShareResult result = new UserShareResult();
        result.setUserShare(userInfo);
        result.setInviterList(inviterList);
        return ResponseUtil.ok(result);
    }


    /**
     *  获取用户余额
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("integral")
    public Object integral(@LoginUser String userId) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }

        CarServiceUser userShare = userService.findById(userId);
        if (userShare == null) {
            return ResponseUtil.fail("用户不存在");
        }

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("integral", userShare.getIntegral());
        return ResponseUtil.ok(dataMap);
    }

    /**
     * 获取用户交易记录
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("trading-record")
    public Object tradingRecord(@LoginUser String userId, PageBody body) {
        CarServiceUser user = userService.findById(userId);
        List<CarServiceDealingSlip> dealingSlips = dealingSlipService.querySelective(userId, user.getOpenid(), body);
        ArrayList<TradingRecordResult> dataList = new ArrayList<>();
        for (CarServiceDealingSlip dealingSlip : dealingSlips) {
            TradingRecordResult result = new TradingRecordResult();
            result.setBatchTime(dealingSlip.getAddTime());
            result.setDealTypeText(DealType.parseValue(dealingSlip.getDealType()));
            result.setAward(dealingSlip.getAward());
            result.setStatus(dealingSlip.getStatus());
            result.setPicUrl(user.getAvatarUrl());
            String value = FailReasonStatus.parseValue(dealingSlip.getRemark());
            if (StringUtils.hasText(value)) {
                result.setStatusText(value);
            } else {
                result.setStatusText(TransferStatus.parseValue(dealingSlip.getStatus()));
            }
            dataList.add(result);
        }
        return ResponseUtil.okList(dataList, dealingSlips);
    }


    /**
     * 余额提现
     * @param userId 用户id
     * @return 成功or失败
     */
    @PostMapping("withdraw-deposit")
    public Object withdrawDeposit(@LoginUser String userId, @JsonBody String cashAmount) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }

        CarServiceUser user = userService.findById(userId);
        if (user == null) {
            return ResponseUtil.unlogin();
        }

        //提现金额
        BigDecimal cashAmountPrice = new BigDecimal(cashAmount);
        if (cashAmountPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseUtil.fail("提现金额不能小于零");
        }

        if (cashAmountPrice.compareTo(BigDecimal.valueOf(1000)) > 0) {
            return ResponseUtil.fail("提现金额不能大于1000");
        }

        BigDecimal integral = user.getIntegral();
        if (integral.compareTo(cashAmountPrice) < 0) {
            return ResponseUtil.fail("余额不足");
        }

        //资金校验
        Object deal = slipCoreService.isDeduction(user);
        if (deal != null) {
            return deal;
        }

        //转账批次单号
        String outTradeNo = orderRandomCode.generateOutBatchNo(userId);
        //余额减少
        slipCoreService.subtractIntegral(user, cashAmountPrice, outTradeNo);
        //发起转账
        wxPayTransferService.withdrawDeposit(user.getOpenid(), user.getTrueName(), cashAmountPrice, outTradeNo);
        return ResponseUtil.ok();
    }

}