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
import org.click.carservice.db.domain.carserviceIssue;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.service.impl.IssueServiceImpl;
import org.click.carservice.wx.model.issue.body.IssueListBody;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_issue")
public class WxIssueService extends IssueServiceImpl {


    @Cacheable(sync = true)
    public List<carserviceIssue> getGoodsIssue() {
        return queryAll(startPage(new PageBody(4)));
    }

    @Cacheable(sync = true)
    public List<carserviceIssue> querySelective(IssueListBody body) {
        QueryWrapper<carserviceIssue> wrapper = startPage(body);
        if (StringUtils.hasText(body.getQuestion())) {
            wrapper.like(carserviceIssue.QUESTION, body.getQuestion());
        }
        return queryAll(wrapper);
    }


}
