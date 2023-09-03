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
import org.click.carservice.admin.model.feedback.body.FeedbackListBody;
import org.click.carservice.db.domain.CarServiceFeedback;
import org.click.carservice.db.service.impl.FeedbackServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 意见反馈服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_feedback")
public class AdminFeedbackService extends FeedbackServiceImpl {


    //@Cacheable(sync = true)
    public List<CarServiceFeedback> querySelective(FeedbackListBody body) {
        QueryWrapper<CarServiceFeedback> wrapper = startPage(body);
        if (body.getUserId() != null) {
            wrapper.eq(CarServiceFeedback.USER_ID, body.getUserId());
        }
        if (StringUtils.hasText(body.getUsername())) {
            wrapper.like(CarServiceFeedback.USERNAME, body.getUsername());
        }
        if (StringUtils.hasText(body.getMobile())) {
            wrapper.like(CarServiceFeedback.MOBILE, body.getMobile());
        }
        return queryAll(wrapper);
    }


}
