package org.click.carservice.wx.web.impl;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.service.RewardCoreService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceGoods;
import org.click.carservice.db.domain.CarServiceReward;
import org.click.carservice.db.domain.CarServiceRewardTask;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.entity.UserInfo;
import org.click.carservice.db.enums.RewardStatus;
import org.click.carservice.wx.model.reward.result.RewardJoinResult;
import org.click.carservice.wx.model.reward.result.RewardListInfo;
import org.click.carservice.wx.model.reward.result.RewardListResult;
import org.click.carservice.wx.service.WxGoodsService;
import org.click.carservice.wx.service.WxRewardService;
import org.click.carservice.wx.service.WxRewardTaskService;
import org.click.carservice.wx.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 赏金接口
 * @author Ysling
 */
@Slf4j
@Service
public class WxWebRewardService {

    @Autowired
    private WxGoodsService goodsService;
    @Autowired
    private WxRewardTaskService rewardTaskService;
    @Autowired
    private WxRewardService rewardService;
    @Autowired
    private WxUserService userService;
    @Autowired
    private RewardCoreService rewardCoreService;

    /**
     * 赏金列表
     * @param userId 用户ID
     * @param body 请求参数
     */
    public Object rewardTaskList(String userId, PageBody body) {
        ArrayList<RewardListInfo> rewardMaps = new ArrayList<>();
        //获取所有赏金规则
        List<CarServiceRewardTask> rewardTasks = rewardTaskService.querySelective(body);
        for (CarServiceRewardTask rewardTask :rewardTasks) {
            RewardListInfo result = new RewardListInfo();
            result.setRewardTask(rewardTask);
            Integer countReward = rewardService.countReward(rewardTask.getId());
            result.setPercentage(rewardTaskService.percentage(countReward, rewardTask.getRewardMember()));
            if (userId != null){
                //获取分享记录
                CarServiceReward sharer = rewardService.findSharer(userId, rewardTask.getId());
                if (sharer != null){
                    List<CarServiceReward> rewards = rewardService.queryJoinRecord(sharer.getId());
                    PageInfo<CarServiceReward> rewardPageInfo = PageInfo.of(rewards);
                    //获取用户信息
                    ArrayList<UserInfo> joiners = new ArrayList<>();
                    for (CarServiceReward reward :rewards) {
                        joiners.add(userService.findUserVoById(reward.getUserId()));
                    }
                    result.setTotal(rewardPageInfo.getTotal());
                    result.setJoiners(joiners);
                }
            }
            rewardMaps.add(result);
        }

        //收益
        BigDecimal earnings = BigDecimal.valueOf(0);
        //余额
        BigDecimal balance = BigDecimal.valueOf(0);

        if (userId != null){
            List<CarServiceReward> rewardList = rewardService.querySharerUserId(userId);
            for (CarServiceReward reward :rewardList) {
                if (RewardStatus.STATUS_SUCCEED.getStatus().equals(reward.getStatus())){
                    earnings = earnings.add(reward.getAward());
                }else {
                    balance = balance.add(reward.getAward());
                }
            }
        }

        PageInfo<CarServiceRewardTask> pageInfo = PageInfo.of(rewardTasks);
        RewardListResult<RewardListInfo> result = new RewardListResult<>();
        result.setEarnings(earnings);
        result.setBalance(balance);
        result.setList(rewardMaps);
        result.setTotal(pageInfo.getTotal());
        result.setPage(pageInfo.getPageNum());
        result.setLimit(pageInfo.getPageSize());
        result.setPages(pageInfo.getPages());
        return ResponseUtil.ok(result);
    }



    /**
     * 参加赏金
     * @param rewardId  活动ID
     * @return 成功
     */
    public Object join(String rewardId) {
        CarServiceReward reward = rewardService.findById(rewardId);
        if (reward == null) {
            return ResponseUtil.badArgumentValue();
        }

        CarServiceRewardTask rewardTask = rewardTaskService.findById(reward.getTaskId());
        if (rewardTask == null) {
            return ResponseUtil.badArgumentValue();
        }

        Object serviceReward = rewardCoreService.isReward(reward.getTaskId());
        if (serviceReward != null){
            return serviceReward;
        }

        CarServiceGoods goods = goodsService.findById(rewardTask.getGoodsId());
        if (goods == null) {
            return ResponseUtil.badArgumentValue();
        }
        RewardJoinResult result = new RewardJoinResult();
        result.setReward(reward);
        result.setRewardTask(rewardTask);
        result.setGoodId(goods.getId());
        return ResponseUtil.ok(result);
    }

    /**
     * 添加赏金参与信息
     * @param userId    用户ID
     * @param rewardTaskId 赏金规则ID
     */
    public Object create(String userId, String rewardTaskId) {
        Object serviceReward = rewardCoreService.isReward(rewardTaskId);
        if (serviceReward != null){
            return serviceReward;
        }
        CarServiceReward reward = rewardService.findSharer(userId, rewardTaskId);
        if (reward != null) {
            return ResponseUtil.ok(reward);
        }
        reward = new CarServiceReward();
        reward.setUserId(userId);
        reward.setTaskId(rewardTaskId);
        reward.setAward(BigDecimal.valueOf(0));
        rewardService.add(reward);
        return ResponseUtil.ok(reward);
    }

}
