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
import org.click.carservice.db.domain.carserviceSystem;
import org.click.carservice.db.service.impl.SystemServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 系统配置
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_system")
public class AdminSystemService extends SystemServiceImpl {


    @Cacheable(sync = true)
    public Map<String, String> listMail() {
        QueryWrapper<carserviceSystem> wrapper = new QueryWrapper<>();
        wrapper.like(carserviceSystem.NAME, "mall_");
        List<carserviceSystem> systemList = queryAll(wrapper);
        Map<String, String> data = new HashMap<>(systemList.size());
        for (carserviceSystem system : systemList) {
            data.put(system.getName(), system.getValue());
        }
        return data;
    }


    @Cacheable(sync = true)
    public Map<String, String> listWx() {
        QueryWrapper<carserviceSystem> wrapper = new QueryWrapper<>();
        wrapper.like(carserviceSystem.NAME, "wx_");
        List<carserviceSystem> systemList = queryAll(wrapper);
        Map<String, String> data = new HashMap<>(systemList.size());
        for (carserviceSystem system : systemList) {
            data.put(system.getName(), system.getValue());
        }
        return data;
    }


    @Cacheable(sync = true)
    public Map<String, String> listOrder() {
        QueryWrapper<carserviceSystem> wrapper = new QueryWrapper<>();
        wrapper.like(carserviceSystem.NAME, "order_");
        List<carserviceSystem> systemList = queryAll(wrapper);
        Map<String, String> data = new HashMap<>(systemList.size());
        for (carserviceSystem system : systemList) {
            data.put(system.getName(), system.getValue());
        }
        return data;
    }


    @Cacheable(sync = true)
    public Map<String, String> listExpress() {
        QueryWrapper<carserviceSystem> wrapper = new QueryWrapper<>();
        wrapper.like(carserviceSystem.NAME, "express_");
        List<carserviceSystem> systemList = queryAll(wrapper);
        Map<String, String> data = new HashMap<>(systemList.size());
        for (carserviceSystem system : systemList) {
            data.put(system.getName(), system.getValue());
        }
        return data;
    }


    @CacheEvict(allEntries = true)
    public void updateConfig(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            QueryWrapper<carserviceSystem> wrapper = new QueryWrapper<>();
            wrapper.eq(carserviceSystem.NAME, entry.getKey());
            carserviceSystem system = new carserviceSystem();
            system.setName(entry.getKey());
            system.setValue(entry.getValue());
            system.setUpdateTime(LocalDateTime.now());
            if (!update(system, wrapper)) {
                system.setId(null);
                system.setDepict("");
                add(system);
            }
        }
    }


}
