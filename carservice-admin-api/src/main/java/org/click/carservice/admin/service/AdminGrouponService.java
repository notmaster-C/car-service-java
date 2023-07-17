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
import org.click.carservice.admin.model.groupon.body.GrouponListBody;
import org.click.carservice.db.domain.carserviceGroupon;
import org.click.carservice.db.service.impl.GrouponServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 团购活动服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_groupon")
public class AdminGrouponService extends GrouponServiceImpl {


    @Cacheable(sync = true)
    public List<carserviceGroupon> querySelective(GrouponListBody body) {
        QueryWrapper<carserviceGroupon> wrapper = startPage(body);
        if (body.getUserId() != null) {
            wrapper.eq(carserviceGroupon.USER_ID, body.getUserId());
        }
        if (body.getRuleId() != null) {
            wrapper.eq(carserviceGroupon.RULES_ID, body.getRuleId());
        }
        if (body.getStatus() != null) {
            wrapper.eq(carserviceGroupon.STATUS, body.getStatus());
        }
        if (body.getGrouponId() != null) {
            wrapper.eq(carserviceGroupon.GROUPON_ID, body.getGrouponId());
        }
        return queryAll(wrapper);
    }


}
