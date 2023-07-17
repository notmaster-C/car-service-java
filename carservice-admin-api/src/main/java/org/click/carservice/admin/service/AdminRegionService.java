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
import org.click.carservice.db.domain.carserviceRegion;
import org.click.carservice.db.service.impl.RegionServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行政区域服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_region")
public class AdminRegionService extends RegionServiceImpl {


    @Cacheable(sync = true)
    public List<carserviceRegion> getAll() {
        QueryWrapper<carserviceRegion> wrapper = new QueryWrapper<>();
        wrapper.ne(carserviceRegion.TYPE, 4);
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public List<carserviceRegion> queryByPid(String parentId) {
        QueryWrapper<carserviceRegion> wrapper = new QueryWrapper<>();
        wrapper.ne(carserviceRegion.PID, parentId);
        return queryAll(wrapper);
    }

}
