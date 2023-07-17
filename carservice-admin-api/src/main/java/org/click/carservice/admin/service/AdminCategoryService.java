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
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceCategory;
import org.click.carservice.db.service.impl.CategoryServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * 分类服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_category")
public class AdminCategoryService extends CategoryServiceImpl {


    public Object validate(carserviceCategory category) {
        String name = category.getName();
        if (Objects.isNull(name)) {
            return ResponseUtil.badArgument();
        }
        String level = category.getLevel();
        if (Objects.isNull(level)) {
            return ResponseUtil.badArgument();
        }
        if (!"L1".equals(level) && !"L2".equals(level)) {
            return ResponseUtil.badArgumentValue();
        }
        String pid = category.getPid();
        if ("L2".equals(level) && (pid == null)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @Cacheable(sync = true)
    public List<carserviceCategory> queryL1() {
        QueryWrapper<carserviceCategory> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCategory.LEVEL, "L1");
        wrapper.orderByDesc(carserviceCategory.WEIGHT);
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceCategory> queryByPid(String pid) {
        QueryWrapper<carserviceCategory> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCategory.PID, pid);
        wrapper.orderByDesc(carserviceCategory.WEIGHT);
        return queryAll(wrapper);
    }

}
