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
import org.click.carservice.db.domain.CarServiceAdmin;
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
    public CarServiceAdmin findByOpenId(String openId) {
        QueryWrapper<CarServiceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAdmin.OPENID, openId);
        return getOne(wrapper);
    }


    @Cacheable(sync = true)
    public List<CarServiceAdmin> findByTenantId(String tenantId) {
        QueryWrapper<CarServiceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAdmin.TENANT_ID, tenantId);
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public List<CarServiceAdmin> findAdmin(String username) {
        QueryWrapper<CarServiceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAdmin.USERNAME, username);
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public CarServiceAdmin findByMobile(String mobile) {
        QueryWrapper<CarServiceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAdmin.MOBILE, mobile);
        return getOne(wrapper);
    }
}
