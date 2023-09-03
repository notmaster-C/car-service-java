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
import org.click.carservice.db.domain.CarServiceStorage;
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


    //@Cacheable(sync = true)
    public List<CarServiceStorage> querySelective(StorageListBody body) {
        QueryWrapper<CarServiceStorage> wrapper = startPage(body);
        if (StringUtils.hasText(body.getKey())) {
            wrapper.eq(CarServiceStorage.KEY, body.getKey());
        }
        if (StringUtils.hasText(body.getName())) {
            wrapper.like(CarServiceStorage.NAME, body.getName());
        }
        return queryAll(wrapper);
    }

    @CacheEvict(allEntries = true)
    public void deleteByKey(String key) {
        QueryWrapper<CarServiceStorage> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceStorage.KEY, key);
        remove(wrapper);
    }

}
