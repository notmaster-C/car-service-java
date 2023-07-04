package org.ysling.litemall.wx.web;
/**
 *  Copyright (c) [ysling] [927069313@qq.com]
 *  [litemall-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.core.annotation.JsonBody;
import org.ysling.litemall.core.service.DealingSlipCoreService;
import org.ysling.litemall.core.service.QrcodeCoreService;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.core.weixin.enums.FailReasonStatus;
import org.ysling.litemall.core.weixin.service.WxPayTransferService;
import org.ysling.litemall.db.entity.PageBody;
import org.ysling.litemall.db.entity.UserInfo;
import org.ysling.litemall.db.domain.LitemallDealingSlip;
import org.ysling.litemall.db.domain.LitemallUser;
import org.ysling.litemall.db.enums.DealType;
import org.ysling.litemall.core.weixin.enums.TransferStatus;
import org.ysling.litemall.db.enums.UserLevel;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.springframework.validation.annotation.Validated;
import org.ysling.litemall.wx.service.WxBrandService;
import org.ysling.litemall.wx.service.WxDealingSlipService;
import org.ysling.litemall.wx.service.WxOrderService;
import org.ysling.litemall.wx.service.WxUserService;
import org.ysling.litemall.wx.model.user.result.TradingRecordResult;
import org.ysling.litemall.wx.model.user.result.UserIndexResult;
import org.ysling.litemall.wx.model.user.result.UserShareResult;
import org.ysling.litemall.wx.service.OrderRandomCode;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用户服务
 * @author Ysling
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
        LitemallUser user = userService.findById(userId);
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
        LitemallUser user = userService.findById(userId);
        UserInfo info = new UserInfo();
        BeanUtil.copyProperties(user , info);
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
        LitemallUser userShare = userService.findByShare(userId);
        if (!StringUtils.hasText(userShare.getShareUrl())){
            userShare.setShareUrl(qCodeService.createUserShareQrcode(userShare));
            userService.updateSelective(userShare);
        }
        ArrayList<UserInfo> inviterList = new ArrayList<>();
        List<LitemallUser> userList = userService.queryByInviter(userId);
        for (LitemallUser user :userList) {
            if (!StringUtils.hasText(user.getMobile())) {
                user.setMobile("18500007139");
            }
            user.setMobile(DesensitizedUtil.mobilePhone(user.getMobile()));
            UserInfo userInfo = new UserInfo();
            BeanUtil.copyProperties(user , userInfo);
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

        LitemallUser userShare = userService.findById(userId);
        if (userShare == null) {
            return ResponseUtil.fail("用户不存在");
        }

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("integral" , userShare.getIntegral());
        return ResponseUtil.ok(dataMap);
    }

    /**
     * 获取用户交易记录
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("trading-record")
    public Object tradingRecord(@LoginUser String userId, PageBody body) {
        LitemallUser user = userService.findById(userId);
        List<LitemallDealingSlip> dealingSlips = dealingSlipService.querySelective(userId, user.getOpenid(), body);
        ArrayList<TradingRecordResult> dataList = new ArrayList<>();
        for (LitemallDealingSlip dealingSlip :dealingSlips) {
            TradingRecordResult result = new TradingRecordResult();
            result.setBatchTime(dealingSlip.getAddTime());
            result.setDealTypeText(DealType.parseValue(dealingSlip.getDealType()));
            result.setAward(dealingSlip.getAward());
            result.setStatus(dealingSlip.getStatus());
            result.setPicUrl(user.getAvatarUrl());
            String value = FailReasonStatus.parseValue(dealingSlip.getRemark());
            if (StringUtils.hasText(value)){
                result.setStatusText(value);
            }else {
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
    public Object withdrawDeposit(@LoginUser String userId ,@JsonBody String cashAmount){
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }

        LitemallUser user = userService.findById(userId);
        if (user == null){
            return ResponseUtil.unlogin();
        }

        //提现金额
        BigDecimal cashAmountPrice = new BigDecimal(cashAmount);
        if (cashAmountPrice.compareTo(BigDecimal.ZERO) <= 0){
            return ResponseUtil.fail("提现金额不能小于零");
        }

        if (cashAmountPrice.compareTo(BigDecimal.valueOf(1000)) > 0){
            return ResponseUtil.fail("提现金额不能大于1000");
        }

        BigDecimal integral = user.getIntegral();
        if (integral.compareTo(cashAmountPrice) < 0){
            return ResponseUtil.fail("余额不足");
        }

        //资金校验
        Object deal = slipCoreService.isDeduction(user);
        if (deal != null){
            return deal;
        }

        //转账批次单号
        String outTradeNo = orderRandomCode.generateOutBatchNo(userId);
        //余额减少
        slipCoreService.subtractIntegral(user, cashAmountPrice , outTradeNo);
        //发起转账
        wxPayTransferService.withdrawDeposit(user.getOpenid(), user.getTrueName(), cashAmountPrice, outTradeNo);
        return ResponseUtil.ok();
    }

}