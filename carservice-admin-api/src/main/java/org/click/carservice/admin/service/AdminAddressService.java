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
import org.click.carservice.admin.model.address.body.AddressListBody;
import org.click.carservice.db.domain.carserviceAddress;
import org.click.carservice.db.service.impl.AddressServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户收货地址
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_address")
public class AdminAddressService extends AddressServiceImpl {


    @Cacheable(sync = true)
    public List<carserviceAddress> querySelective(AddressListBody body) {
        QueryWrapper<carserviceAddress> wrapper = startPage(body);
        if (body.getUserId() != null) {
            wrapper.eq(carserviceAddress.USER_ID, body.getUserId());
        }
        if (StringUtils.hasText(body.getName())) {
            wrapper.like(carserviceAddress.NAME, body.getName());
        }
        if (StringUtils.hasText(body.getMobile())) {
            wrapper.like(carserviceAddress.MOBILE, body.getMobile());
        }
        return queryAll(wrapper);
    }

}
