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
import org.click.carservice.admin.model.user.body.DealingSlipListBody;
import org.click.carservice.db.domain.CarServiceDealingSlip;
import org.click.carservice.db.service.impl.DealingSlipServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户余额交易记录服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_dealing_slip")
public class AdminDealingSlipService extends DealingSlipServiceImpl {


    //@Cacheable(sync = true)
    public List<CarServiceDealingSlip> querySelective(DealingSlipListBody body) {
        QueryWrapper<CarServiceDealingSlip> wrapper = startPage(body);
        if (body.getUserId() != null) {
            wrapper.eq(CarServiceDealingSlip.USER_ID, body.getUserId());
        }
        if (body.getDealType() != null) {
            wrapper.eq(CarServiceDealingSlip.DEAL_TYPE, body.getDealType());
        }
        return queryAll(wrapper);
    }


}
