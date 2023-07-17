package org.click.carservice.wx.web;
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

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.annotation.JsonBody;
import org.click.carservice.core.service.RewardCoreService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceGoods;
import org.click.carservice.db.domain.carserviceReward;
import org.click.carservice.db.domain.carserviceRewardTask;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.entity.UserInfo;
import org.click.carservice.db.enums.RewardStatus;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.reward.result.RewardJoinResult;
import org.click.carservice.wx.model.reward.result.RewardListInfo;
import org.click.carservice.wx.model.reward.result.RewardListResult;
import org.click.carservice.wx.service.WxGoodsService;
import org.click.carservice.wx.service.WxRewardService;
import org.click.carservice.wx.service.WxRewardTaskService;
import org.click.carservice.wx.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 赏金接口
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/reward")
@Validated
public class WxRewardController {

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
    @GetMapping("/list")
    public Object rewardTaskList(@LoginUser(require = false) String userId, PageBody body) {
        ArrayList<RewardListInfo> rewardMaps = new ArrayList<>();
        //获取所有赏金规则
        List<carserviceRewardTask> rewardTasks = rewardTaskService.querySelective(body);
        for (carserviceRewardTask rewardTask : rewardTasks) {
            RewardListInfo result = new RewardListInfo();
            result.setRewardTask(rewardTask);
            Integer countReward = rewardService.countReward(rewardTask.getId());
            result.setPercentage(rewardTaskService.percentage(countReward, rewardTask.getRewardMember()));
            if (userId != null) {
                //获取分享记录
                carserviceReward sharer = rewardService.findSharer(userId, rewardTask.getId());
                if (sharer != null) {
                    List<carserviceReward> rewards = rewardService.queryJoinRecord(sharer.getId());
                    PageInfo<carserviceReward> rewardPageInfo = PageInfo.of(rewards);
                    //获取用户信息
                    ArrayList<UserInfo> joiners = new ArrayList<>();
                    for (carserviceReward reward : rewards) {
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

        if (userId != null) {
            List<carserviceReward> rewardList = rewardService.querySharerUserId(userId);
            for (carserviceReward reward : rewardList) {
                if (RewardStatus.STATUS_SUCCEED.getStatus().equals(reward.getStatus())) {
                    earnings = earnings.add(reward.getAward());
                } else {
                    balance = balance.add(reward.getAward());
                }
            }
        }

        PageInfo<carserviceRewardTask> pageInfo = PageInfo.of(rewardTasks);
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
    @GetMapping("/join")
    public Object join(@NotNull String rewardId) {
        carserviceReward reward = rewardService.findById(rewardId);
        if (reward == null) {
            return ResponseUtil.badArgumentValue();
        }

        carserviceRewardTask rewardTask = rewardTaskService.findById(reward.getTaskId());
        if (rewardTask == null) {
            return ResponseUtil.badArgumentValue();
        }

        Object serviceReward = rewardCoreService.isReward(reward.getTaskId());
        if (serviceReward != null) {
            return serviceReward;
        }

        carserviceGoods goods = goodsService.findById(rewardTask.getGoodsId());
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
    @PostMapping("/create")
    public Object create(@LoginUser String userId, @JsonBody String rewardTaskId) {
        Object serviceReward = rewardCoreService.isReward(rewardTaskId);
        if (serviceReward != null) {
            return serviceReward;
        }

        carserviceReward reward = rewardService.findSharer(userId, rewardTaskId);
        if (reward != null) {
            return ResponseUtil.ok(reward);
        }

        reward = new carserviceReward();
        reward.setUserId(userId);
        reward.setTaskId(rewardTaskId);
        reward.setAward(BigDecimal.valueOf(0));

        rewardService.add(reward);
        return ResponseUtil.ok(reward);
    }

}
