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
import org.click.carservice.admin.model.footprint.body.FootprintListBody;
import org.click.carservice.db.domain.CarServiceFootprint;
import org.click.carservice.db.service.impl.FootprintServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户浏览足迹服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_footprint")
public class AdminFootprintService extends FootprintServiceImpl {


    @Cacheable(sync = true)
    public List<CarServiceFootprint> querySelective(FootprintListBody body) {
        QueryWrapper<CarServiceFootprint> wrapper = startPage(body);
        if (StringUtils.hasText(body.getUserId())) {
            wrapper.eq(CarServiceFootprint.USER_ID, body.getUserId());
        }
        if (StringUtils.hasText(body.getGoodsId())) {
            wrapper.eq(CarServiceFootprint.GOODS_ID, body.getGoodsId());
        }
        return queryAll(wrapper);
    }


}
