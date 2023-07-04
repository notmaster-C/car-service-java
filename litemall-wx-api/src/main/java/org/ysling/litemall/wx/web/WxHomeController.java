package org.ysling.litemall.wx.web;
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
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.core.annotation.JsonBody;
import org.ysling.litemall.core.service.RewardCoreService;
import org.ysling.litemall.core.system.SystemConfig;
import org.ysling.litemall.core.tenant.handler.TenantContextHolder;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.domain.*;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.ysling.litemall.wx.service.*;
import org.springframework.validation.annotation.Validated;
import org.ysling.litemall.core.utils.Inheritable.InheritableCallable;
import org.ysling.litemall.wx.model.home.body.HomeNavigateBody;
import org.ysling.litemall.wx.model.home.result.HomeAboutResult;
import org.ysling.litemall.wx.model.home.result.HomeIndexResult;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.*;

/**
 * 首页服务
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/wx/home")
@Validated
public class WxHomeController {


    @Autowired
    private WxAdService adService;
    @Autowired
    private WxGoodsService goodsService;
    @Autowired
    private WxCategoryService categoryService;
    @Autowired
    private WxGrouponService grouponService;
    @Autowired
    private WxGrouponRulesService grouponRulesService;
    @Autowired
    private WxCouponService couponService;
    @Autowired
    private WxRewardService rewardService;
    @Autowired
    private RewardCoreService rewardCoreService;
    @Autowired
    private WxTenantService tenantService;
    @Autowired
    private WxRewardTaskService rewardTaskService;
    @Autowired
    private ThreadPoolExecutor executorService;

    /**
     * 首页数据
     * @param userId 当用户已经登录时，非空。未登录状态为null
     * @return 首页数据
     */
    @GetMapping("/index")
    public Object index(@LoginUser(require = false) String userId, @JsonBody String appid) {
        String tenant = tenantService.getTenant(appid);
        // 广告列表
        FutureTask<List<LitemallAd>> bannerTask = new FutureTask<>(
            new InheritableCallable<List<LitemallAd>>(){
                @Override
                public List<LitemallAd> runTask() {
                    return adService.queryIndex();
                }
            }
        );

        // 分类列表
        FutureTask<List<LitemallCategory>> channelTask = new FutureTask<>(
            new InheritableCallable<List<LitemallCategory>>(){
                @Override
                public List<LitemallCategory> runTask() {
                    return categoryService.queryChannel();
                }
            }
        );

        // 优惠券列表
        FutureTask<List<LitemallCoupon>> couponListTask = new FutureTask<>(
            new InheritableCallable<List<LitemallCoupon>>(){
                @Override
                public List<LitemallCoupon> runTask() {
                    return couponService.queryAvailableList(userId, SystemConfig.getCouponLimit());
                }
            }
        );

        // 新品首发
        FutureTask<List<LitemallGoods>> newGoodsListTask = new FutureTask<>(
            new InheritableCallable<List<LitemallGoods>>(){
                @Override
                public List<LitemallGoods> runTask() {
                    return goodsService.queryByNew(SystemConfig.getNewLimit());
                }
            }
        );

        // 热卖专区
        FutureTask<List<LitemallGoods>> hotGoodsListTask = new FutureTask<>(
            new InheritableCallable<List<LitemallGoods>>(){
                @Override
                public List<LitemallGoods> runTask() {
                    return goodsService.queryByHot(SystemConfig.getHotLimit());
                }
            }
        );

        // 赏金任务
        FutureTask<List<LitemallRewardTask>> rewardTaskListTask = new FutureTask<>(
            new InheritableCallable<List<LitemallRewardTask>>(){
                @Override
                public List<LitemallRewardTask> runTask() {
                    return rewardTaskService.queryByReward(SystemConfig.getRewardLimit());
                }
            }
        );

        // 团购专区
        FutureTask<List<LitemallGrouponRules>> grouponRuleListTask = new FutureTask<>(
            new InheritableCallable<List<LitemallGrouponRules>>(){
                @Override
                public List<LitemallGrouponRules> runTask() {
                    return grouponRulesService.queryByGroupon(SystemConfig.getGrouponLimit());
                }
            }
        );

        // 首页商品分页
        FutureTask<PageInfo<LitemallGoods>> allGoodsListTask = new FutureTask<>(
            new InheritableCallable<PageInfo<LitemallGoods>>(){
                @Override
                public PageInfo<LitemallGoods> runTask() {
                    return PageInfo.of(goodsService.queryByAll(SystemConfig.getAllLimit()));
                }
            }
        );

        executorService.submit(bannerTask);
        executorService.submit(channelTask);
        executorService.submit(couponListTask);
        executorService.submit(newGoodsListTask);
        executorService.submit(hotGoodsListTask);
        executorService.submit(allGoodsListTask);
        executorService.submit(rewardTaskListTask);
        executorService.submit(grouponRuleListTask);

        HomeIndexResult result = new HomeIndexResult();
        try {
            result.setTenantId(tenant);
            result.setBanner(bannerTask.get());
            result.setChannel(channelTask.get());
            result.setCouponList(couponListTask.get());
            result.setNewGoodsList(newGoodsListTask.get());
            result.setHotGoodsList(hotGoodsListTask.get());
            result.setAllGoodsList(allGoodsListTask.get());
            result.setRewardTaskList(rewardTaskListTask.get());
            result.setGrouponRuleList(grouponRuleListTask.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseUtil.ok(result);
    }



    /**
     * 判断首页初始化参数是否支持跳转
     */
    @PostMapping("/navigate")
    public Object isNavigate(@Valid @RequestBody HomeNavigateBody body) {
        String sceneId = body.getSceneId();
        String sceneType = body.getSceneType();
        if (Objects.equals(sceneId, "0")){
            return ResponseUtil.fail(800,"分享获取失败");
        }
        if (Objects.equals(sceneType,"goodsId")){
            LitemallGoods goods = goodsService.findById(sceneId);
            if (goods == null){
                return ResponseUtil.fail(800,"商品不存在");
            }
        }
        if (Objects.equals(sceneType,"grouponId")){
            Object groupon = grouponService.isGroupon(sceneId);
            if (groupon != null){
                return groupon;
            }
        }
        if (Objects.equals(sceneType,"rewardId")){
            LitemallReward reward = rewardService.findById(sceneId);
            if (reward == null) {
                return ResponseUtil.fail(800,"活动不存在");
            }
            Object serviceReward = rewardCoreService.isReward(reward.getTaskId());
            if (serviceReward != null){
                return serviceReward;
            }
        }
        return ResponseUtil.ok();
    }


    /**
     * 商城介绍信息
     * @return 商城介绍信息
     */
    @GetMapping("/about")
    public Object about() {
        HomeAboutResult result = new HomeAboutResult();
        result.setName(SystemConfig.getMallName());
        result.setPhone(SystemConfig.getMallPhone());
        result.setQq(SystemConfig.getMallQQ());
        result.setLongitude(SystemConfig.getMallLongitude());
        result.setLatitude(SystemConfig.getMallLatitude());
        String tenantId = TenantContextHolder.getLocalTenantId();
        LitemallTenant tenant = tenantService.findById(tenantId);
        if (tenant != null){
            result.setAddress(tenant.getAddress());
        }
        return ResponseUtil.ok(result);
    }


}