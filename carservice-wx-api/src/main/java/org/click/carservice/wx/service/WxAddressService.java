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
import org.click.carservice.db.domain.carserviceAddress;
import org.click.carservice.db.service.impl.AddressServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 收货地址服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_address")
public class WxAddressService extends AddressServiceImpl {


    @Cacheable(sync = true)
    public List<carserviceAddress> queryByUid(String userId) {
        QueryWrapper<carserviceAddress> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceAddress.USER_ID, userId);
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public carserviceAddress query(String userId, String id) {
        QueryWrapper<carserviceAddress> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceAddress.USER_ID, userId);
        wrapper.eq(carserviceAddress.ID, id);
        return getOne(wrapper);
    }


    @Cacheable(sync = true)
    public void deleteByUser(String userId, String id) {
        QueryWrapper<carserviceAddress> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceAddress.USER_ID, userId);
        wrapper.eq(carserviceAddress.ID, id);
        remove(wrapper);
    }

    @Cacheable(sync = true)
    public carserviceAddress findDefault(String userId) {
        QueryWrapper<carserviceAddress> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceAddress.USER_ID, userId);
        wrapper.eq(carserviceAddress.IS_DEFAULT, true);
        return getOne(wrapper, false);
    }


    @CacheEvict(allEntries = true)
    public void resetDefault(String userId) {
        carserviceAddress address = new carserviceAddress();
        address.setIsDefault(false);
        address.setUpdateTime(LocalDateTime.now());
        QueryWrapper<carserviceAddress> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceAddress.USER_ID, userId);
        update(address, wrapper);
    }


}
