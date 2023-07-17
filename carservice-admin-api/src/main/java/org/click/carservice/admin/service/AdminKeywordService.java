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
import org.click.carservice.admin.model.keyword.body.KeywordListBody;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceKeyword;
import org.click.carservice.db.service.impl.KeywordServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 关键字服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_keyword")
public class AdminKeywordService extends KeywordServiceImpl {


    public Object validate(CarServiceKeyword keywords) {
        String keyword = keywords.getKeyword();
        if (Objects.isNull(keyword)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @Cacheable(sync = true)
    public List<CarServiceKeyword> querySelective(KeywordListBody body) {
        QueryWrapper<CarServiceKeyword> wrapper = startPage(body);
        if (StringUtils.hasText(body.getKeyword())) {
            wrapper.like(CarServiceKeyword.KEYWORD, body.getKeyword());
        }
        if (StringUtils.hasText(body.getUrl())) {
            wrapper.like(CarServiceKeyword.URL, body.getUrl());
        }
        wrapper.orderByDesc(CarServiceKeyword.WEIGHT);
        return queryAll(wrapper);
    }

}
