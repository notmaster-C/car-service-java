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
import org.click.carservice.db.domain.CarServiceReward;
import org.click.carservice.db.enums.RewardStatus;
import org.click.carservice.db.service.impl.RewardServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 赏金参与服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_reward")
public class WxRewardService extends RewardServiceImpl {


    @Cacheable(sync = true)
    public Integer countReward(String taskId) {
        QueryWrapper<CarServiceReward> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceReward.TASK_ID, taskId);
        wrapper.eq(CarServiceReward.STATUS, RewardStatus.STATUS_SUCCEED.getStatus());
        return Math.toIntExact(count(wrapper));
    }

    @Cacheable(sync = true)
    public Integer countAndReward(String userId, String taskId) {
        QueryWrapper<CarServiceReward> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceReward.USER_ID, userId);
        wrapper.eq(CarServiceReward.TASK_ID, taskId);
        return Math.toIntExact(count(wrapper));
    }

    /**
     * 获取分享者记录
     */
    @Cacheable(sync = true)
    public CarServiceReward findSharer(String userId, String taskId) {
        QueryWrapper<CarServiceReward> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceReward.USER_ID, userId);
        wrapper.eq(CarServiceReward.TASK_ID, taskId);
        wrapper.eq(CarServiceReward.REWARD_ID, "0");
        return getOne(wrapper);
    }

    /**
     * 获取用户所有分享记录
     */
    @Cacheable(sync = true)
    public List<CarServiceReward> querySharerUserId(String userId) {
        QueryWrapper<CarServiceReward> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceReward.USER_ID, userId);
        wrapper.eq(CarServiceReward.STATUS, RewardStatus.STATUS_SUCCEED.getStatus());
        return queryAll(wrapper);
    }

    /**
     * 获取某个赏金活动参与的记录
     */
    @Cacheable(sync = true)
    public List<CarServiceReward> queryJoinRecord(String rewardId) {
        QueryWrapper<CarServiceReward> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceReward.REWARD_ID, rewardId);
        wrapper.eq(CarServiceReward.STATUS, RewardStatus.STATUS_SUCCEED.getStatus());
        wrapper.orderByDesc(CarServiceReward.ADD_TIME);
        return queryAll(wrapper);
    }

}
