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
import org.click.carservice.db.domain.carserviceAdmin;
import org.click.carservice.db.service.impl.AdminServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_admin")
public class WxAdminService extends AdminServiceImpl {


    @Cacheable(sync = true)
    public carserviceAdmin findByOpenId(String openId) {
        QueryWrapper<carserviceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceAdmin.OPENID, openId);
        return getOne(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceAdmin> findByTenantId(String tenantId) {
        QueryWrapper<carserviceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceAdmin.TENANT_ID, tenantId);
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public List<carserviceAdmin> findAdmin(String username) {
        QueryWrapper<carserviceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceAdmin.USERNAME, username);
        return queryAll(wrapper);
    }


}
