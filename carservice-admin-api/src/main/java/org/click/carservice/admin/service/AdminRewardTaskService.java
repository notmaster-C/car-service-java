package org.click.carservice.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.admin.model.reward.body.RewardListBody;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceRewardTask;
import org.click.carservice.db.service.impl.RewardTaskServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;


/**
 * 赏金规则服务
 *
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_reward_task")
public class AdminRewardTaskService extends RewardTaskServiceImpl {


    public Object validate(carserviceRewardTask rewardTask) {
        String goodsId = rewardTask.getGoodsId();
        if (goodsId == null) {
            return ResponseUtil.badArgument();
        }

        Integer rewardMember = rewardTask.getRewardMember();
        if (rewardMember == null || rewardMember <= 0) {
            return ResponseUtil.badArgument();
        }

        BigDecimal award = rewardTask.getAward();
        if (award == null || award.intValue() < 1 || award.intValue() > 100) {
            return ResponseUtil.fail("奖励1~100");
        }
        return null;
    }

    @Cacheable(sync = true)
    public carserviceRewardTask findByGid(String goodsId) {
        QueryWrapper<carserviceRewardTask> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceRewardTask.GOODS_ID, goodsId);
        return getOne(wrapper);
    }

    @CacheEvict(allEntries = true)
    public void deleteByGid(String goodsId) {
        QueryWrapper<carserviceRewardTask> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceRewardTask.GOODS_ID, goodsId);
        remove(wrapper);
    }

    @Cacheable(sync = true)
    public Integer countByGoodsId(String goodsId) {
        QueryWrapper<carserviceRewardTask> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceRewardTask.GOODS_ID, goodsId);
        return Math.toIntExact(count(wrapper));
    }

    @Cacheable(sync = true)
    public List<carserviceRewardTask> querySelective(RewardListBody body) {
        QueryWrapper<carserviceRewardTask> wrapper = startPage(body);
        if (StringUtils.hasText(body.getGoodsId())) {
            wrapper.eq(carserviceRewardTask.GOODS_ID, body.getGoodsId());
        }
        return queryAll(wrapper);
    }

}
