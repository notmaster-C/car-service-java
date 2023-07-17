package org.click.carservice.admin.service;
// Copyright (c) [click] [927069313@qq.com]
// [carservice-plus] is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//             http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.admin.model.share.body.ShareListBody;
import org.click.carservice.db.domain.carserviceShare;
import org.click.carservice.db.service.impl.ShareServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 分享记录服务
 *
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_share")
public class AdminShareService extends ShareServiceImpl {


    @Cacheable(sync = true)
    public List<carserviceShare> querySelective(ShareListBody body) {
        QueryWrapper<carserviceShare> wrapper = startPage(body);
        if (body.getInviterId() != null) {
            wrapper.eq(carserviceShare.INVITER_ID, body.getInviterId());
        }
        if (body.getUserId() != null) {
            wrapper.eq(carserviceShare.USER_ID, body.getUserId());
        }
        if (body.getStatus() != null) {
            wrapper.eq(carserviceShare.STATUS, body.getStatus());
        }
        return queryAll(wrapper);
    }


}
