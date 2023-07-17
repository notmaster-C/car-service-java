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
import org.click.carservice.admin.model.admin.body.AdminListBody;
import org.click.carservice.core.utils.RegexUtil;
import org.click.carservice.core.utils.bcrypt.BCryptPasswordEncoder;
import org.click.carservice.core.utils.response.ResponseStatus;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceAdmin;
import org.click.carservice.db.service.impl.AdminServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;


/**
 * 管理员服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_admin")
public class AdminAdminService extends AdminServiceImpl {


    public Object validate(CarServiceAdmin admin) {
        String name = admin.getUsername();
        if (Objects.isNull(name) || name.length() < 6) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0110);
        }
        if (RegexUtil.isQQMail(admin.getMail())) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0153);
        }
        if (!RegexUtil.isMobileSimple(admin.getMobile())) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0151);
        }
        String password = admin.getPassword();
        if (Objects.isNull(password) || password.length() < 6) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0122);
        }
        return null;
    }

    @CacheEvict(allEntries = true)
    public boolean saveAdmin(CarServiceAdmin admin) {
        String rawPassword = admin.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        admin.setPassword(encodedPassword);
        return saveOrUpdate(admin);
    }

    @Cacheable(sync = true)
    public List<CarServiceAdmin> findAdmin(String username) {
        QueryWrapper<CarServiceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAdmin.USERNAME, username);
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public List<CarServiceAdmin> findByTenantId(String tenantId) {
        QueryWrapper<CarServiceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAdmin.TENANT_ID, tenantId);
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public List<CarServiceAdmin> querySelective(AdminListBody body) {
        QueryWrapper<CarServiceAdmin> wrapper = startPage(body);
        if (StringUtils.hasText(body.getMail())) {
            wrapper.like(CarServiceAdmin.MAIL, body.getMail());
        }
        if (StringUtils.hasText(body.getMobile())) {
            wrapper.like(CarServiceAdmin.MOBILE, body.getMobile());
        }
        if (StringUtils.hasText(body.getUsername())) {
            wrapper.like(CarServiceAdmin.USERNAME, body.getUsername());
        }
        return queryAll(wrapper);
    }


}
