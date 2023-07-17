package org.click.carservice.admin.service;
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
import org.click.carservice.admin.model.groupon.body.GrouponRuleListBody;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceGrouponRules;
import org.click.carservice.db.service.impl.GrouponRulesServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 团购规则服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_groupon_rules")
public class AdminGrouponRulesService extends GrouponRulesServiceImpl {


    public Object validate(CarServiceGrouponRules grouponRules) {
        String goodsId = grouponRules.getGoodsId();
        if (goodsId == null) {
            return ResponseUtil.badArgument();
        }
        BigDecimal discount = grouponRules.getDiscount();
        if (discount == null) {
            return ResponseUtil.badArgument();
        }
        Integer discountMember = grouponRules.getDiscountMember();
        if (discountMember == null) {
            return ResponseUtil.badArgument();
        }
        LocalDateTime expireTime = grouponRules.getExpireTime();
        if (expireTime == null) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @Cacheable(sync = true)
    public CarServiceGrouponRules findByGid(String goodsId) {
        QueryWrapper<CarServiceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGrouponRules.GOODS_ID, goodsId);
        return getOne(wrapper);
    }

    @Cacheable(sync = true)
    public Integer countByGoodsId(String goodsId) {
        QueryWrapper<CarServiceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGrouponRules.GOODS_ID, goodsId);
        return Math.toIntExact(count(wrapper));
    }

    @Cacheable(sync = true)
    public List<CarServiceGrouponRules> queryByGoodsId(String goodsId) {
        QueryWrapper<CarServiceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGrouponRules.GOODS_ID, goodsId);
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public List<CarServiceGrouponRules> querySelective(GrouponRuleListBody body) {
        QueryWrapper<CarServiceGrouponRules> wrapper = startPage(body);
        if (StringUtils.hasText(body.getGoodsId())) {
            wrapper.eq(CarServiceGrouponRules.GOODS_ID, body.getGoodsId());
        }
        return queryAll(wrapper);
    }


}