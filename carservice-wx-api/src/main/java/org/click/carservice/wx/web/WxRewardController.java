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

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.annotation.JsonBody;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.web.impl.WxWebRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;

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
    private WxWebRewardService rewardService;


    /**
     * 赏金列表
     * @param userId 用户ID
     * @param body 请求参数
     */
    @GetMapping("/list")
    public Object rewardTaskList(@LoginUser(require = false) String userId, PageBody body) {
        return rewardService.rewardTaskList(userId , body);
    }

    /**
     * 参加赏金
     * @param rewardId  活动ID
     * @return 成功
     */
    @GetMapping("/join")
    public Object join(@NotNull String rewardId) {
        return rewardService.join(rewardId);
    }

    /**
     * 添加赏金参与信息
     * @param userId    用户ID
     * @param rewardTaskId 赏金规则ID
     */
    @PostMapping("/create")
    public Object create(@LoginUser String userId, @JsonBody String rewardTaskId) {
        return rewardService.create(userId , rewardTaskId);
    }

}
