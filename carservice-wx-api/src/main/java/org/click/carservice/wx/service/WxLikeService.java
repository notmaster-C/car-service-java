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
import org.click.carservice.db.domain.carserviceLike;
import org.click.carservice.db.enums.LikeType;
import org.click.carservice.db.service.impl.LikeServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 点赞服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_like")
public class WxLikeService extends LikeServiceImpl {


    @Cacheable(sync = true)
    public Integer count(Short type, String valueId) {
        QueryWrapper<carserviceLike> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceLike.VALUE_ID, valueId);
        wrapper.eq(carserviceLike.TYPE, type);
        wrapper.eq(carserviceLike.CANCEL, false);
        return Math.toIntExact(count(wrapper));
    }


    @Cacheable(sync = true)
    public Boolean count(LikeType constant, String valueId, String userId) {
        if (userId == null) {
            return false;
        }
        QueryWrapper<carserviceLike> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceLike.TYPE, constant.getStatus());
        wrapper.eq(carserviceLike.VALUE_ID, valueId);
        wrapper.eq(carserviceLike.USER_ID, userId);
        wrapper.eq(carserviceLike.CANCEL, false);
        return exists(wrapper);
    }


    @Cacheable(sync = true)
    public carserviceLike query(Short type, String valueId, String userId) {
        QueryWrapper<carserviceLike> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceLike.TYPE, type);
        wrapper.eq(carserviceLike.VALUE_ID, valueId);
        wrapper.eq(carserviceLike.USER_ID, userId);
        return getOne(wrapper, false);
    }

}
