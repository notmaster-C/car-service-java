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
import org.click.carservice.admin.model.topic.body.TopicListBody;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceTopic;
import org.click.carservice.db.service.impl.TopicServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 专题服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_topic")
public class AdminTopicService extends TopicServiceImpl {

    public Object validate(carserviceTopic topic) {
        String title = topic.getTitle();
        if (Objects.isNull(title)) {
            return ResponseUtil.fail("标题不能为空");
        }
        String content = topic.getContent();
        if (Objects.isNull(content)) {
            return ResponseUtil.fail("内容不能为空");
        }
        String author = topic.getAuthor();
        if (author == null) {
            return ResponseUtil.fail("作者不能为空");
        }
        String picUrl = topic.getPicUrl();
        if (picUrl == null) {
            return ResponseUtil.fail("图片不能为空");
        }
        BigDecimal price = topic.getPrice();
        if (Objects.isNull(price)) {
            return ResponseUtil.fail("商品底价不能为空");
        }
        String readCount = topic.getReadCount();
        if (readCount == null) {
            return ResponseUtil.fail("浏览量不能为空");
        }
        return null;
    }

    @Cacheable(sync = true)
    public List<carserviceTopic> querySelective(TopicListBody body) {
        QueryWrapper<carserviceTopic> wrapper = startPage(body);
        if (StringUtils.hasText(body.getTitle())) {
            wrapper.like(carserviceTopic.TITLE, body.getTitle());
        }
        if (StringUtils.hasText(body.getSubtitle())) {
            wrapper.like(carserviceTopic.SUBTITLE, body.getSubtitle());
        }
        wrapper.orderByDesc(carserviceTopic.WEIGHT);
        return queryAll(wrapper);
    }

}
