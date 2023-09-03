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
import org.click.carservice.admin.model.reward.body.RewardJoinBody;
import org.click.carservice.db.domain.CarServiceReward;
import org.click.carservice.db.service.impl.RewardServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 赏金服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_reward")
public class AdminRewardService extends RewardServiceImpl {


    //@Cacheable(sync = true)
    public List<CarServiceReward> querySelective(RewardJoinBody body) {
        QueryWrapper<CarServiceReward> wrapper = startPage(body);
        if (body.getUserId() != null) {
            wrapper.eq(CarServiceReward.USER_ID, body.getUserId());
        }
        if (body.getTaskId() != null) {
            wrapper.eq(CarServiceReward.TASK_ID, body.getTaskId());
        }
        if (body.getStatus() != null) {
            wrapper.eq(CarServiceReward.STATUS, body.getStatus());
        }
        if (body.getRewardId() != null) {
            wrapper.eq(CarServiceReward.REWARD_ID, body.getRewardId());
        }
        return queryAll(wrapper);
    }


}
