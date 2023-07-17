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
import org.click.carservice.db.domain.CarServiceAfterSale;
import org.click.carservice.db.service.impl.AftersaleServiceImpl;
import org.click.carservice.wx.model.aftersale.body.AftersaleListBody;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 售后服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_aftersale")
public class WxAftersaleService extends AftersaleServiceImpl {


    @Cacheable(sync = true)
    public CarServiceAfterSale findById(String userId, String id) {
        QueryWrapper<CarServiceAfterSale> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAfterSale.ID, id);
        wrapper.eq(CarServiceAfterSale.USER_ID, userId);
        return getOne(wrapper);
    }

    @Cacheable(sync = true)
    public List<CarServiceAfterSale> querySelective(String userId, AftersaleListBody body) {
        QueryWrapper<CarServiceAfterSale> wrapper = startPage(body);
        wrapper.eq(CarServiceAfterSale.USER_ID, userId);
        if (body.getStatus() != null) {
            wrapper.eq(CarServiceAfterSale.STATUS, body.getStatus());
        }
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public Boolean countByAftersaleSn(String userId, String aftersaleSn) {
        QueryWrapper<CarServiceAfterSale> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAfterSale.USER_ID, userId);
        wrapper.eq(CarServiceAfterSale.AFTERSALE_SN, aftersaleSn);
        return exists(wrapper);
    }

    @CacheEvict(allEntries = true)
    public void deleteByOrderId(String userId, String orderId) {
        QueryWrapper<CarServiceAfterSale> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAfterSale.USER_ID, userId);
        wrapper.eq(CarServiceAfterSale.ORDER_ID, orderId);
        remove(wrapper);
    }

    @Cacheable(sync = true)
    public CarServiceAfterSale findByOrderId(String userId, String orderId) {
        QueryWrapper<CarServiceAfterSale> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAfterSale.USER_ID, userId);
        wrapper.eq(CarServiceAfterSale.ORDER_ID, orderId);
        return getOne(wrapper);
    }

}
