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
import org.click.carservice.admin.model.log.body.LogListBody;
import org.click.carservice.db.domain.CarServiceLog;
import org.click.carservice.db.service.impl.LogServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 日志服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_log")
public class AdminLogService extends LogServiceImpl {


    @Cacheable(sync = true)
    public List<CarServiceLog> querySelective(LogListBody body) {
        QueryWrapper<CarServiceLog> wrapper = startPage(body);
        if (body.getType() != null) {
            wrapper.eq(CarServiceLog.TYPE, body.getType());
        }
        if (body.getStatus() != null) {
            wrapper.eq(CarServiceLog.STATUS, body.getStatus());
        }
        if (StringUtils.hasText(body.getName())) {
            wrapper.like(CarServiceLog.ADMIN, body.getName());
        }
        return queryAll(wrapper);
    }


}
