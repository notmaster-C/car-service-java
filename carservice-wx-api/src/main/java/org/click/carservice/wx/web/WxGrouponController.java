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

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceGoods;
import org.click.carservice.db.domain.CarServiceGroupon;
import org.click.carservice.db.domain.CarServiceGrouponRules;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.enums.GoodsStatus;
import org.click.carservice.wx.model.groupon.result.GrouponJoinResult;
import org.click.carservice.wx.model.groupon.result.GrouponRuleResult;
import org.click.carservice.wx.service.WxGoodsService;
import org.click.carservice.wx.service.WxGrouponRulesService;
import org.click.carservice.wx.service.WxGrouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * 团购服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/groupon")
@Validated
public class WxGrouponController {

    @Autowired
    private WxGrouponRulesService rulesService;
    @Autowired
    private WxGrouponService grouponService;
    @Autowired
    private WxGoodsService goodsService;

    /**
     * 团购规则列表
     */
    @GetMapping("list")
    public Object list(PageBody body) {
        List<CarServiceGrouponRules> grouponRulesList = rulesService.queryList(body);
        ArrayList<GrouponRuleResult> grouponList = new ArrayList<>();
        for (CarServiceGrouponRules rule : grouponRulesList) {
            String goodsId = rule.getGoodsId();
            CarServiceGoods goods = goodsService.findById(goodsId);
            if (goods == null || !GoodsStatus.getIsOnSale(goods)) {
                continue;
            }
            GrouponRuleResult result = new GrouponRuleResult();
            result.setId(goods.getId());
            result.setName(goods.getName());
            result.setBrief(goods.getBrief());
            result.setPicUrl(goods.getPicUrl());
            result.setCounterPrice(goods.getCounterPrice());
            result.setRetailPrice(goods.getRetailPrice());
            result.setGrouponPrice(goods.getRetailPrice().subtract(rule.getDiscount()));
            result.setGrouponDiscount(rule.getDiscount());
            result.setGrouponMember(rule.getDiscountMember());
            result.setExpireTime(rule.getExpireTime());
            grouponList.add(result);
        }
        return ResponseUtil.okList(grouponList, grouponRulesList);
    }

    /**
     * 参加团购
     * @param grouponId 团购活动ID
     * @return 操作结果
     */
    @GetMapping("join")
    public Object join(@NotNull String grouponId) {
        CarServiceGroupon groupon = grouponService.findById(grouponId);
        if (groupon == null) {
            return ResponseUtil.badArgumentValue();
        }

        CarServiceGrouponRules rules = rulesService.findById(groupon.getRulesId());
        if (rules == null) {
            return ResponseUtil.badArgumentValue();
        }

        CarServiceGoods goods = goodsService.findById(rules.getGoodsId());
        if (goods == null) {
            return ResponseUtil.badArgumentValue();
        }

        GrouponJoinResult result = new GrouponJoinResult();
        result.setGroupon(groupon);
        result.setGoodId(goods.getId());
        return ResponseUtil.ok(result);
    }

}
