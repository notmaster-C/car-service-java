package org.ysling.litemall.admin.service;
/**
 *  Copyright (c) [ysling] [927069313@qq.com]
 *  [litemall-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.ysling.litemall.db.domain.LitemallSystem;
import org.ysling.litemall.db.service.impl.SystemServiceImpl;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 系统配置
 * @author Ysling
 */
@Service
@CacheConfig(cacheNames = "litemall_system")
public class AdminSystemService extends SystemServiceImpl {


    @Cacheable(sync = true)
    public Map<String, String> listMail() {
        QueryWrapper<LitemallSystem> wrapper = new QueryWrapper<>();
        wrapper.like(LitemallSystem.NAME , "mall_");
        List<LitemallSystem> systemList = queryAll(wrapper);
        Map<String, String> data = new HashMap<>(systemList.size());
        for(LitemallSystem system : systemList){
            data.put(system.getName(), system.getValue());
        }
        return data;
    }

    
    @Cacheable(sync = true)
    public Map<String, String> listWx() {
        QueryWrapper<LitemallSystem> wrapper = new QueryWrapper<>();
        wrapper.like(LitemallSystem.NAME , "wx_");
        List<LitemallSystem> systemList = queryAll(wrapper);
        Map<String, String> data = new HashMap<>(systemList.size());
        for(LitemallSystem system : systemList){
            data.put(system.getName(), system.getValue());
        }
        return data;
    }

    
    @Cacheable(sync = true)
    public Map<String, String> listOrder() {
        QueryWrapper<LitemallSystem> wrapper = new QueryWrapper<>();
        wrapper.like(LitemallSystem.NAME , "order_");
        List<LitemallSystem> systemList = queryAll(wrapper);
        Map<String, String> data = new HashMap<>(systemList.size());
        for(LitemallSystem system : systemList){
            data.put(system.getName(), system.getValue());
        }
        return data;
    }

    
    @Cacheable(sync = true)
    public Map<String, String> listExpress() {
        QueryWrapper<LitemallSystem> wrapper = new QueryWrapper<>();
        wrapper.like(LitemallSystem.NAME , "express_");
        List<LitemallSystem> systemList = queryAll(wrapper);
        Map<String, String> data = new HashMap<>(systemList.size());
        for(LitemallSystem system : systemList){
            data.put(system.getName(), system.getValue());
        }
        return data;
    }


    @CacheEvict(allEntries = true)
    public void updateConfig(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            QueryWrapper<LitemallSystem> wrapper = new QueryWrapper<>();
            wrapper.eq(LitemallSystem.NAME , entry.getKey());
            LitemallSystem system = new LitemallSystem();
            system.setName(entry.getKey());
            system.setValue(entry.getValue());
            system.setUpdateTime(LocalDateTime.now());
            if (!update(system, wrapper)){
                system.setId(null);
                system.setDepict("");
                add(system);
            }
        }
    }
    

}
