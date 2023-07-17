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
import org.click.carservice.admin.model.ad.body.AdListBody;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceAd;
import org.click.carservice.db.service.impl.AdServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 广告服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_ad")
public class AdminAdService extends AdServiceImpl {


    public Object validate(CarServiceAd ad) {
        String name = ad.getName();
        if (Objects.isNull(name)) {
            return ResponseUtil.badArgument();
        }
        String content = ad.getContent();
        if (Objects.isNull(content)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }


    @Cacheable(sync = true)
    public List<CarServiceAd> querySelective(AdListBody body) {
        QueryWrapper<CarServiceAd> wrapper = startPage(body);
        if (StringUtils.hasText(body.getName())) {
            wrapper.like(CarServiceAd.NAME, body.getName());
        }
        if (StringUtils.hasText(body.getContent())) {
            wrapper.like(CarServiceAd.CONTENT, body.getContent());
        }
        wrapper.orderByDesc(CarServiceAd.WEIGHT);
        return list(wrapper);
    }


}
