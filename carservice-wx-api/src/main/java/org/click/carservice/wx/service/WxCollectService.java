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
import org.click.carservice.db.domain.carserviceCollect;
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


    @Cacheable(sync = true)
    public Boolean count(String userId, CollectType type, String goodsId) {
        if (userId == null) {
            return false;
        }
        QueryWrapper<carserviceCollect> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCollect.USER_ID, userId);
        wrapper.eq(carserviceCollect.TYPE, type.getStatus());
        wrapper.eq(carserviceCollect.VALUE_ID, goodsId);
        wrapper.eq(carserviceCollect.CANCEL, false);
        return exists(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceCollect> queryByType(String userId, CollectListBody body) {
        QueryWrapper<carserviceCollect> wrapper = startPage(body);
        wrapper.eq(carserviceCollect.USER_ID, userId);
        wrapper.eq(carserviceCollect.CANCEL, false);
        if (body.getType() != null) {
            wrapper.eq(carserviceCollect.TYPE, body.getType());
        }
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public carserviceCollect queryByTypeAndValue(String userId, Byte type, String valueId) {
        QueryWrapper<carserviceCollect> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCollect.USER_ID, userId);
        wrapper.eq(carserviceCollect.TYPE, type);
        wrapper.eq(carserviceCollect.VALUE_ID, valueId);
        return getOne(wrapper);
    }

}
