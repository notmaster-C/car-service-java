package org.click.carservice.wx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.carserviceRewardTask;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.enums.RewardTaskStatus;
import org.click.carservice.db.service.impl.RewardTaskServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;


/**
 * 赏金规则服务
 *
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_reward_task")
public class WxRewardTaskService extends RewardTaskServiceImpl {


    /**
     * 获取两数百分比
     *
     * @param divisor  除数
     * @param dividend 被除数
     * @return 百分比
     */
    public String percentage(Integer divisor, Integer dividend) {
        BigDecimal divideNum = BigDecimal.valueOf(divisor).divide(
                BigDecimal.valueOf(dividend), 2, RoundingMode.CEILING);
        //将结果百分比
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        return percent.format(divideNum.doubleValue());
    }


    @Cacheable(sync = true)
    public List<carserviceRewardTask> querySelective(String goodsId, PageBody body) {
        QueryWrapper<carserviceRewardTask> wrapper = startPage(body);
        if (StringUtils.hasText(goodsId)) {
            wrapper.eq(carserviceRewardTask.GOODS_ID, goodsId);
        }
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceRewardTask> queryByReward(Integer limit) {
        QueryWrapper<carserviceRewardTask> wrapper = startPage(new PageBody(limit));
        wrapper.eq(carserviceRewardTask.STATUS, RewardTaskStatus.TASK_STATUS_ON.getStatus());
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<carserviceRewardTask> querySelective(PageBody body) {
        QueryWrapper<carserviceRewardTask> wrapper = startPage(body);
        wrapper.eq(carserviceRewardTask.STATUS, RewardTaskStatus.TASK_STATUS_ON.getStatus());
        return queryAll(wrapper);
    }

}
