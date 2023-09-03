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
import org.click.carservice.db.domain.CarServiceKeyword;
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


    //@Cacheable(sync = true)
    public CarServiceKeyword queryDefault() {
        QueryWrapper<CarServiceKeyword> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceKeyword.IS_DEFAULT, true);
        return getOne(wrapper, false);
    }


    //@Cacheable(sync = true)
    public List<CarServiceKeyword> queryHots() {
        QueryWrapper<CarServiceKeyword> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceKeyword.IS_HOT, true);
        wrapper.orderByDesc(CarServiceKeyword.WEIGHT);
        return queryAll(wrapper);
    }


    //@Cacheable(sync = true)
    public List<CarServiceKeyword> queryByKeyword(SearchListBody body) {
        QueryWrapper<CarServiceKeyword> wrapper = startPage(body);
        wrapper.like(CarServiceKeyword.KEYWORD, body.getKeyword());
        wrapper.orderByDesc(CarServiceKeyword.WEIGHT);
        return queryAll(wrapper);
    }

}
