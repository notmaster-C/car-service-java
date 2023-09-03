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
import org.click.carservice.db.domain.CarServiceCollect;
import org.click.carservice.db.enums.CollectType;
import org.click.carservice.db.service.impl.CollectServiceImpl;
import org.click.carservice.wx.model.collect.body.CollectListBody;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 收藏服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_collect")
public class WxCollectService extends CollectServiceImpl {


    //@Cacheable(sync = true)
    public Boolean count(String userId, CollectType type, String goodsId) {
        if (userId == null) {
            return false;
        }
        QueryWrapper<CarServiceCollect> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCollect.USER_ID, userId);
        wrapper.eq(CarServiceCollect.TYPE, type.getStatus());
        wrapper.eq(CarServiceCollect.VALUE_ID, goodsId);
        wrapper.eq(CarServiceCollect.CANCEL, false);
        return exists(wrapper);
    }


    //@Cacheable(sync = true)
    public List<CarServiceCollect> queryByType(String userId, CollectListBody body) {
        QueryWrapper<CarServiceCollect> wrapper = startPage(body);
        wrapper.eq(CarServiceCollect.USER_ID, userId);
        wrapper.eq(CarServiceCollect.CANCEL, false);
        if (body.getType() != null) {
            wrapper.eq(CarServiceCollect.TYPE, body.getType());
        }
        return queryAll(wrapper);
    }


    //@Cacheable(sync = true)
    public CarServiceCollect queryByTypeAndValue(String userId, Byte type, String valueId) {
        QueryWrapper<CarServiceCollect> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCollect.USER_ID, userId);
        wrapper.eq(CarServiceCollect.TYPE, type);
        wrapper.eq(CarServiceCollect.VALUE_ID, valueId);
        return getOne(wrapper);
    }

}
