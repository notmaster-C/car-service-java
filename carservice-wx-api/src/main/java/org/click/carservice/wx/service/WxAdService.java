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
import org.click.carservice.db.domain.CarServiceAd;
import org.click.carservice.db.service.impl.AdServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 广告服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_ad")
public class WxAdService extends AdServiceImpl {


    @Cacheable(sync = true)
    public List<CarServiceAd> queryIndex() {
        QueryWrapper<CarServiceAd> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAd.POSITION, 1);
        wrapper.eq(CarServiceAd.ENABLED, true);
        wrapper.orderByDesc(CarServiceAd.WEIGHT);
        return queryAll(wrapper);
    }

}
