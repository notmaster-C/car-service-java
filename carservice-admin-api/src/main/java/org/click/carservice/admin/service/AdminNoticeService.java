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
import org.click.carservice.admin.model.notice.body.NoticeListBody;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceNotice;
import org.click.carservice.db.service.impl.NoticeServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 通知服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_notice")
public class AdminNoticeService extends NoticeServiceImpl {


    public Object validate(CarServiceNotice notice) {
        String title = notice.getTitle();
        if (Objects.isNull(title)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    //@Cacheable(sync = true)
    public List<CarServiceNotice> querySelective(NoticeListBody body) {
        QueryWrapper<CarServiceNotice> wrapper = startPage(body);
        if (StringUtils.hasText(body.getTitle())) {
            wrapper.like(CarServiceNotice.TITLE, body.getTitle());
        }
        if (StringUtils.hasText(body.getContent())) {
            wrapper.like(CarServiceNotice.CONTENT, body.getContent());
        }
        return queryAll(wrapper);
    }


}
