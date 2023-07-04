package org.ysling.litemall.admin.web;
/**
 *  Copyright (c) [ysling] [927069313@qq.com]
 *  [litemall-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */

import lombok.extern.slf4j.Slf4j;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.admin.annotation.RequiresPermissionsDesc;
import org.ysling.litemall.admin.model.reward.body.RewardJoinBody;
import org.ysling.litemall.admin.model.reward.body.RewardListBody;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.domain.*;
import org.ysling.litemall.admin.service.AdminGoodsService;
import org.ysling.litemall.admin.service.AdminRewardService;
import org.ysling.litemall.admin.service.AdminRewardTaskService;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 赏金管理
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/admin/reward")
@Validated
public class AdminRewardController {

    @Autowired
    private AdminGoodsService goodsService;
    @Autowired
    private AdminRewardTaskService rewardTaskService;
    @Autowired
    private AdminRewardService rewardService;

    /**
     * 查询参与用户
     */
    @SaCheckPermission("admin:reward:join")
    @RequiresPermissionsDesc(menu = {"推广管理", "赏金管理"}, button = "查询参与用户")
    @GetMapping("/join")
    public Object listUser(RewardJoinBody body) {
        return ResponseUtil.okList(rewardService.querySelective(body));
    }

    /**
     * 赏金规则查询
     */
    @SaCheckPermission("admin:reward:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "赏金管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(RewardListBody body) {
        return ResponseUtil.okList(rewardTaskService.querySelective(body));
    }


    /**
     * 赏金规则详情
     * @param id 赏金规则ID
     */
    @SaCheckPermission("admin:reward:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "赏金管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull String id) {
        return ResponseUtil.ok(rewardTaskService.findById(id));
    }

    /**
     * 编辑赏金规则
     */
    @SaCheckPermission("admin:reward:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "赏金管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@Valid @RequestBody LitemallRewardTask rewardTask) {
        Object error = rewardTaskService.validate(rewardTask);
        if (error != null) {
            return error;
        }

        LitemallRewardTask task = rewardTaskService.findById(rewardTask.getId());
        if(task == null) {
            return ResponseUtil.badArgumentValue();
        }
        
        LitemallGoods goods = goodsService.findById(rewardTask.getGoodsId());
        if (goods == null) {
            return ResponseUtil.badArgumentValue();
        }

        rewardTask.setGoodsName(goods.getName());
        rewardTask.setPicUrl(goods.getPicUrl());
        rewardTask.setGoodsPrice(goods.getRetailPrice());
        if (rewardTaskService.updateVersionSelective(rewardTask) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }

    /**
     * 添加赏金规则
     */
    @SaCheckPermission("admin:reward:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "赏金管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody LitemallRewardTask rewardTask) {
        Object error = rewardTaskService.validate(rewardTask);
        if (error != null) {
            return error;
        }

        String goodsId = rewardTask.getGoodsId();
        LitemallGoods goods = goodsService.findById(goodsId);
        if (goods == null) {
            return ResponseUtil.fail( "商品不存在");
        }
        if(rewardTaskService.countByGoodsId(goodsId) > 0){
            return ResponseUtil.fail( "商品已经存在");
        }
        //添加赏金活动信息
        rewardTask.setGoodsName(goods.getName());
        rewardTask.setPicUrl(goods.getPicUrl());
        rewardTask.setGoodsPrice(goods.getRetailPrice());
        rewardTaskService.add(rewardTask);
        return ResponseUtil.ok(rewardTask);
    }

    /**
     * 删除赏金规则
     * @param id 赏金规则id
     */
    @SaCheckPermission("admin:reward:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "赏金管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@NotNull String id) {
        rewardTaskService.deleteById(id);
        return ResponseUtil.ok();
    }


}
