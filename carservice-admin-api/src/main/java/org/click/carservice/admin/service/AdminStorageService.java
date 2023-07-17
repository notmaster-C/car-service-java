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
import org.click.carservice.admin.model.storage.body.StorageListBody;
import org.click.carservice.db.domain.carserviceStorage;
import org.click.carservice.db.service.impl.StorageServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 对象存储服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_storage")
public class AdminStorageService extends StorageServiceImpl {


    @Cacheable(sync = true)
    public List<carserviceStorage> querySelective(StorageListBody body) {
        QueryWrapper<carserviceStorage> wrapper = startPage(body);
        if (StringUtils.hasText(body.getKey())) {
            wrapper.eq(carserviceStorage.KEY, body.getKey());
        }
        if (StringUtils.hasText(body.getName())) {
            wrapper.like(carserviceStorage.NAME, body.getName());
        }
        return queryAll(wrapper);
    }

    @CacheEvict(allEntries = true)
    public void deleteByKey(String key) {
        QueryWrapper<carserviceStorage> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceStorage.KEY, key);
        remove(wrapper);
    }

}
