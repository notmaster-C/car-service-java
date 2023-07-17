package org.click.carservice.wx.web.impl;

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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 团购服务
 * @author click
 */
@Slf4j
@Service
public class WxWebGrouponService {

    @Autowired
    private WxGrouponRulesService rulesService;
    @Autowired
    private WxGrouponService grouponService;
    @Autowired
    private WxGoodsService goodsService;

    /**
     * 团购规则列表
     */
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
    public Object join(String grouponId) {
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
