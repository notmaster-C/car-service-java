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
import org.click.carservice.admin.model.role.body.RoleListBody;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceRole;
import org.click.carservice.db.service.impl.RoleServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 角色服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_role")
public class AdminRoleService extends RoleServiceImpl {


    /**
     * 参数校验
     * @param role 权限
     * @return 错误信息
     */
    public Object validate(CarServiceRole role) {
        String name = role.getName();
        if (Objects.isNull(name)) {
            return ResponseUtil.fail("角色名称不能为空");
        }
        String depict = role.getDepict();
        if (Objects.isNull(depict)) {
            return ResponseUtil.fail("角色描述不能为空");
        }
        return null;
    }

    @Cacheable(sync = true)
    public List<CarServiceRole> querySelective(RoleListBody body) {
        QueryWrapper<CarServiceRole> wrapper = startPage(body);
        if (StringUtils.hasText(body.getName())) {
            wrapper.like(CarServiceRole.NAME, body.getName());
        }
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public boolean checkExist(String name) {
        QueryWrapper<CarServiceRole> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceRole.NAME, name);
        return exists(wrapper);
    }


    @Cacheable(sync = true)
    public List<CarServiceRole> queryAll() {
        return list();
    }

}
