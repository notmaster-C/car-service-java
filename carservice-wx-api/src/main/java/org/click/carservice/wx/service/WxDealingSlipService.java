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
import org.click.carservice.db.domain.carserviceDealingSlip;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.service.impl.DealingSlipServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易记录服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_dealing_slip")
public class WxDealingSlipService extends DealingSlipServiceImpl {


    /**
     * 判断outBatchNo是否存在
     * @param userId  用户id
     * @param outBatchNo 转账单号
     * @return true
     */
    @Cacheable(sync = true)
    public Boolean isByOutBatchNo(String userId, String outBatchNo) {
        QueryWrapper<carserviceDealingSlip> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceDealingSlip.USER_ID, userId);
        wrapper.eq(carserviceDealingSlip.OUT_BATCH_NO, outBatchNo);
        return exists(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceDealingSlip> querySelective(String userId, String openId, PageBody body) {
        QueryWrapper<carserviceDealingSlip> wrapper = startPage(body);
        if (userId != null) {
            wrapper.eq(carserviceDealingSlip.USER_ID, userId);
        }
        if (openId != null) {
            wrapper.eq(carserviceDealingSlip.OPENID, openId);
        }
        return queryAll(wrapper);
    }


}
