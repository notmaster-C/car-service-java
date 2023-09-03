package org.click.carservice.wx.service;
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.CarServiceGrouponRules;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.enums.GrouponRuleStatus;
import org.click.carservice.db.service.impl.GrouponRulesServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 团购规则服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_groupon_rules")
public class WxGrouponRulesService extends GrouponRulesServiceImpl {


    //@Cacheable(sync = true)
    public List<CarServiceGrouponRules> queryOnByGoodsId(String goodsId) {
        QueryWrapper<CarServiceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGrouponRules.GOODS_ID, goodsId);
        wrapper.eq(CarServiceGrouponRules.STATUS, GrouponRuleStatus.RULE_STATUS_ON.getStatus());
        return queryAll(wrapper);
    }

    //@Cacheable(sync = true)
    public List<CarServiceGrouponRules> queryByGoodsId(String goodsId) {
        QueryWrapper<CarServiceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGrouponRules.GOODS_ID, goodsId);
        return queryAll(wrapper);
    }


    //@Cacheable(sync = true)
    public List<CarServiceGrouponRules> queryByGroupon(Integer limit) {
        QueryWrapper<CarServiceGrouponRules> wrapper = startPage(new PageBody(limit));
        wrapper.eq(CarServiceGrouponRules.STATUS, GrouponRuleStatus.RULE_STATUS_ON.getStatus());
        return queryAll(wrapper);
    }


    //@Cacheable(sync = true)
    public List<CarServiceGrouponRules> queryList(PageBody body) {
        QueryWrapper<CarServiceGrouponRules> wrapper = startPage(body);
        wrapper.eq(CarServiceGrouponRules.STATUS, GrouponRuleStatus.RULE_STATUS_ON.getStatus());
        return queryAll(wrapper);
    }


    //@Cacheable(sync = true)
    public boolean isExpired(CarServiceGrouponRules rules) {
        return (rules == null || rules.getExpireTime().isBefore(LocalDateTime.now()));
    }

}