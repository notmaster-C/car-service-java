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
import org.click.carservice.db.domain.CarServiceTopic;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.service.impl.TopicServiceImpl;
import org.click.carservice.wx.model.topic.body.TopicListBody;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 专题服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_topic")
public class WxTopicService extends TopicServiceImpl {


    @Cacheable(sync = true)
    public List<CarServiceTopic> queryList(PageBody body) {
        return queryAll(startPage(body));
    }


    @Cacheable(sync = true)
    public List<CarServiceTopic> queryList(TopicListBody body) {
        QueryWrapper<CarServiceTopic> wrapper = startPage(body);
        if (StringUtils.hasText(body.getTitle())) {
            wrapper.like(CarServiceTopic.TITLE, body.getTitle());
        }
        wrapper.orderByDesc(CarServiceTopic.WEIGHT);
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<CarServiceTopic> queryRelatedList(String id, Integer limit) {
        QueryWrapper<CarServiceTopic> wrapper = startPage(new PageBody(limit));
        wrapper.notIn(CarServiceTopic.ID, id);
        wrapper.orderByDesc(CarServiceTopic.WEIGHT);
        return queryAll(wrapper);
    }


}
