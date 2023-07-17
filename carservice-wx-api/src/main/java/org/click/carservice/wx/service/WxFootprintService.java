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
import org.click.carservice.db.domain.carserviceFootprint;
import org.click.carservice.db.domain.carserviceGoods;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.service.impl.FootprintServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户浏览足迹
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_footprint")
public class WxFootprintService extends FootprintServiceImpl {


    @Cacheable(sync = true)
    public List<carserviceFootprint> queryByAddTime(String userId, PageBody body) {
        QueryWrapper<carserviceFootprint> wrapper = startPage(body);
        wrapper.eq(carserviceFootprint.USER_ID, userId);
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public carserviceFootprint findById(String userId, String id) {
        QueryWrapper<carserviceFootprint> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceFootprint.USER_ID, userId);
        wrapper.eq(carserviceFootprint.ID, id);
        return getOne(wrapper);
    }

    @Cacheable(sync = true)
    public carserviceFootprint findByGoodId(String userId, String goodId) {
        QueryWrapper<carserviceFootprint> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceFootprint.USER_ID, userId);
        wrapper.eq(carserviceFootprint.GOODS_ID, goodId);
        return getOne(wrapper, false);
    }


    @CacheEvict(allEntries = true)
    public void createFootprint(String userId, carserviceGoods goods) {
        if (userId == null) {
            return;
        }
        carserviceFootprint footprint = findByGoodId(userId, goods.getId());
        if (footprint != null) {
            footprint.setName(goods.getName());
            footprint.setBrief(goods.getBrief());
            footprint.setPicUrl(goods.getPicUrl());
            footprint.setRetailPrice(goods.getRetailPrice());
            updateVersionSelective(footprint);
        } else {
            footprint = new carserviceFootprint();
            footprint.setUserId(userId);
            footprint.setGoodsId(goods.getId());
            footprint.setName(goods.getName());
            footprint.setBrief(goods.getBrief());
            footprint.setPicUrl(goods.getPicUrl());
            footprint.setRetailPrice(goods.getRetailPrice());
            add(footprint);
        }
    }

}
