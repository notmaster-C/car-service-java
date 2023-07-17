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
import org.click.carservice.db.domain.carserviceGrouponRules;
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


    @Cacheable(sync = true)
    public List<carserviceGrouponRules> queryOnByGoodsId(String goodsId) {
        QueryWrapper<carserviceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGrouponRules.GOODS_ID, goodsId);
        wrapper.eq(carserviceGrouponRules.STATUS, GrouponRuleStatus.RULE_STATUS_ON.getStatus());
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public List<carserviceGrouponRules> queryByGoodsId(String goodsId) {
        QueryWrapper<carserviceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGrouponRules.GOODS_ID, goodsId);
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceGrouponRules> queryByGroupon(Integer limit) {
        QueryWrapper<carserviceGrouponRules> wrapper = startPage(new PageBody(limit));
        wrapper.eq(carserviceGrouponRules.STATUS, GrouponRuleStatus.RULE_STATUS_ON.getStatus());
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceGrouponRules> queryList(PageBody body) {
        QueryWrapper<carserviceGrouponRules> wrapper = startPage(body);
        wrapper.eq(carserviceGrouponRules.STATUS, GrouponRuleStatus.RULE_STATUS_ON.getStatus());
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public boolean isExpired(carserviceGrouponRules rules) {
        return (rules == null || rules.getExpireTime().isBefore(LocalDateTime.now()));
    }

}