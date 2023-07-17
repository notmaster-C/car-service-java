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
import org.click.carservice.db.domain.carserviceKeyword;
import org.click.carservice.db.service.impl.KeywordServiceImpl;
import org.click.carservice.wx.model.search.body.SearchListBody;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关键字服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_keywords")
public class WxKeywordService extends KeywordServiceImpl {


    @Cacheable(sync = true)
    public carserviceKeyword queryDefault() {
        QueryWrapper<carserviceKeyword> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceKeyword.IS_DEFAULT, true);
        return getOne(wrapper, false);
    }


    @Cacheable(sync = true)
    public List<carserviceKeyword> queryHots() {
        QueryWrapper<carserviceKeyword> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceKeyword.IS_HOT, true);
        wrapper.orderByDesc(carserviceKeyword.WEIGHT);
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceKeyword> queryByKeyword(SearchListBody body) {
        QueryWrapper<carserviceKeyword> wrapper = startPage(body);
        wrapper.like(carserviceKeyword.KEYWORD, body.getKeyword());
        wrapper.orderByDesc(carserviceKeyword.WEIGHT);
        return queryAll(wrapper);
    }

}
