package org.click.carservice.core.service;
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


import org.click.carservice.db.domain.CarServiceGroupon;
import org.click.carservice.db.domain.CarServiceGrouponRules;
import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.db.enums.GrouponStatus;
import org.click.carservice.db.enums.OrderStatus;
import org.click.carservice.db.service.IGrouponRulesService;
import org.click.carservice.db.service.IGrouponService;
import org.click.carservice.db.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券服务
 * @author click
 */
@Service
public class GrouponCoreService {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private QrcodeCoreService qCodeService;
    @Autowired
    private IGrouponRulesService grouponRulesService;
    @Autowired
    private IGrouponService grouponService;
    @Autowired
    private NotifyCoreService notifyCoreService;
    @Autowired
    private CommonService commonService;


    /**
     * 修改团购状态和订单状态
     * @param groupon 团购
     */
    public void updateGrouponStatus(CarServiceGroupon groupon) {
        //获取团购规则信息
        CarServiceGrouponRules grouponRules = grouponRulesService.findById(groupon.getRulesId());
        //仅当发起者才创建分享图片
        if (groupon.getGrouponId().equals("0")) {
            String url = qCodeService.createGrouponShareImage(grouponRules, groupon.getId());
            groupon.setShareUrl(url);
        }

        //获取团购参与信息
        List<CarServiceGroupon> grouponList = commonService.queryJoinRecord(groupon.getGrouponId());
        if (!groupon.getGrouponId().equals("0") && (grouponList.size() >= grouponRules.getDiscountMember() - 1)) {
            //修改当前用户的团购信息
            CarServiceGroupon grouponSource = commonService.queryById(groupon.getGrouponId());
            this.updateStatus(grouponSource);
            //修改前面已经参与者团购状态
            for (CarServiceGroupon grouponActivity : grouponList) {
                this.updateStatus(grouponActivity);
            }
        } else {
            //修改团购状态
            groupon.setStatus(GrouponStatus.STATUS_ON.getStatus());
            if (grouponService.updateVersionSelective(groupon) == 0) {
                throw new RuntimeException("更新团购信息失败");
            }

            //修改订单团购状态
            CarServiceOrder order = orderService.findById(groupon.getOrderId());
            order.setPayTime(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.STATUS_GROUPON_ON.getStatus());
            if (orderService.updateVersionSelective(order) == 0) {
                throw new RuntimeException("更新团购订单信息失败");
            }
        }
    }


    /**
     * 修改团购和订单状态，并发送通知邮件
     * @param groupon 团购
     */
    private void updateStatus(CarServiceGroupon groupon) {
        groupon.setStatus(GrouponStatus.STATUS_SUCCEED.getStatus());
        if (grouponService.updateVersionSelective(groupon) == 0) {
            throw new RuntimeException("更新团购信息失败");
        }

        //修改订单团购状态
        CarServiceOrder order = orderService.findById(groupon.getOrderId());
        order.setOrderStatus(OrderStatus.STATUS_GROUPON_SUCCEED.getStatus());
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("更新团购订单信息失败");
        }

        //给商家发送通知
        notifyCoreService.orderNotify(order);
    }


}
