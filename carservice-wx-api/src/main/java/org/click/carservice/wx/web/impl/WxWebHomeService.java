package org.click.carservice.wx.web.impl;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.service.RewardCoreService;
import org.click.carservice.core.system.SystemConfig;
import org.click.carservice.core.tenant.handler.TenantContextHolder;
import org.click.carservice.core.utils.Inheritable.InheritableCallable;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.*;
import org.click.carservice.wx.model.goods.body.GoodsListBodyB;
import org.click.carservice.wx.model.home.body.HomeNavigateBody;
import org.click.carservice.wx.model.home.result.HomeAboutResult;
import org.click.carservice.wx.model.home.result.HomeIndexResult;
import org.click.carservice.wx.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 首页服务
 *
 * @author click
 */
@Slf4j
@Service
public class WxWebHomeService {


    @Autowired
    private WxAdService adService;
    @Autowired
    private WxGoodsService goodsService;
    @Autowired
    private WxCategoryService categoryService;
    @Autowired
    private WxGrouponService grouponService;
    //    @Autowired
//    private WxGrouponRulesService grouponRulesService;
    @Autowired
    private WxCouponService couponService;
    @Autowired
    private WxBrandService brandService;
    @Autowired
    private WxRewardService rewardService;
    @Autowired
    private RewardCoreService rewardCoreService;
    @Autowired
    private WxTenantService tenantService;
    //    @Autowired
//    private WxRewardTaskService rewardTaskService;
    @Autowired
    private ThreadPoolExecutor executorService;

    /**
     * 首页数据
     *
     * @param userId 当用户已经登录时，非空。未登录状态为null
     * @return 首页数据
     */
    public Object index(String userId, String appid) {
        String tenant = tenantService.getTenant(appid);
        // 广告列表
        FutureTask<List<CarServiceAd>> bannerTask = new FutureTask<>(
                new InheritableCallable<List<CarServiceAd>>() {
                    @Override
                    public List<CarServiceAd> runTask() {
                        return adService.queryIndex();
                    }
                }
        );

        // 分类列表
        FutureTask<List<CarServiceCategory>> channelTask = new FutureTask<>(
                new InheritableCallable<List<CarServiceCategory>>() {
                    @Override
                    public List<CarServiceCategory> runTask() {
                        return categoryService.queryChannel();
                    }
                }
        );

        // 优惠券列表
        FutureTask<List<CarServiceCoupon>> couponListTask = new FutureTask<>(
                new InheritableCallable<List<CarServiceCoupon>>() {
                    @Override
                    public List<CarServiceCoupon> runTask() {
                        return couponService.queryAvailableList(userId, SystemConfig.getCouponLimit());
                    }
                }
        );
//
//        // 商铺列表
//        FutureTask<List<CarServiceBrand>> brandListTask = new FutureTask<>(
//                new InheritableCallable<List<CarServiceBrand>>(){
//                    @Override
//                    public List<CarServiceBrand> runTask() {
//                        return brandService.queryList(CarServiceBrand);
//                    }
//                }
//        );
        // 新品首发
        FutureTask<List<GoodsListBodyB>> newGoodsListTask = new FutureTask<>(
                new InheritableCallable<List<GoodsListBodyB>>() {
                    @Override
                    public List<GoodsListBodyB> runTask() {
                        List<CarServiceGoods> newGoods = goodsService.queryByNew(SystemConfig.getNewLimit());
                        List<GoodsListBodyB> goods = new ArrayList<>();
                        for (CarServiceGoods item : newGoods) {
                            GoodsListBodyB t = new GoodsListBodyB();
                            t.setGoods(item);
                            CarServiceBrand brand = brandService.findByBrandId(item.getBrandId());
                            t.setLatitude(brand.getLatitude());
                            t.setLongitude(brand.getLongitude());
                            goods.add(t);
                        }
                        return goods;
                    }
                }
        );

        // 热卖专区
        FutureTask<List<GoodsListBodyB>> hotGoodsListTask = new FutureTask<>(
                new InheritableCallable<List<GoodsListBodyB>>() {
                    @Override
                    public List<GoodsListBodyB> runTask() {
                        List<CarServiceGoods> hotGoods = goodsService.queryByHot(SystemConfig.getHotLimit());
                        List<GoodsListBodyB> goods = new ArrayList<>();
                        for (CarServiceGoods item : hotGoods) {
                            GoodsListBodyB t = new GoodsListBodyB();
                            t.setGoods(item);
                            CarServiceBrand brand = brandService.findByBrandId(item.getBrandId());
                            t.setLatitude(brand.getLatitude());
                            t.setLongitude(brand.getLongitude());
                            goods.add(t);
                        }
                        return goods;
                    }
                }
        );
        // 首页商品分页
        FutureTask<List<GoodsListBodyB>> allGoodsListTask = new FutureTask<>(
                new InheritableCallable<List<GoodsListBodyB>>() {
                    @Override
                    public List<GoodsListBodyB> runTask() {
                        List<CarServiceGoods> allGoods = goodsService.queryByAll(SystemConfig.getAllLimit());
                        List<GoodsListBodyB> goods = new ArrayList<>();
                        for (CarServiceGoods item : allGoods) {
                            GoodsListBodyB t = new GoodsListBodyB();
                            t.setGoods(item);
                            CarServiceBrand brand = brandService.findByBrandId(item.getBrandId());
                            t.setLatitude(brand.getLatitude());
                            t.setLongitude(brand.getLongitude());
                            goods.add(t);
                        }
                        return goods;
                    }
                }
        );
        // 赏金任务
//        FutureTask<List<CarServiceRewardTask>> rewardTaskListTask = new FutureTask<>(
//            new InheritableCallable<List<CarServiceRewardTask>>(){
//                @Override
//                public List<CarServiceRewardTask> runTask() {
//                    return rewardTaskService.queryByReward(SystemConfig.getRewardLimit());
//                }
//            }
//        );

        // 团购专区
//        FutureTask<List<CarServiceGrouponRules>> grouponRuleListTask = new FutureTask<>(
//            new InheritableCallable<List<CarServiceGrouponRules>>(){
//                @Override
//                public List<CarServiceGrouponRules> runTask() {
//                    return grouponRulesService.queryByGroupon(SystemConfig.getGrouponLimit());
//                }
//            }
//        );


        executorService.submit(bannerTask);
        executorService.submit(channelTask);
        executorService.submit(couponListTask);
        executorService.submit(newGoodsListTask);
        executorService.submit(hotGoodsListTask);
        executorService.submit(allGoodsListTask);
//        executorService.submit(rewardTaskListTask);
//        executorService.submit(grouponRuleListTask);

        HomeIndexResult result = new HomeIndexResult();
        try {
            result.setTenantId(tenant);
            result.setBanner(bannerTask.get());
            result.setChannel(channelTask.get());
            result.setCouponList(couponListTask.get());
            result.setNewGoodsList(newGoodsListTask.get());
            result.setHotGoodsList(hotGoodsListTask.get());
            result.setAllGoodsList(allGoodsListTask.get());
//            result.setRewardTaskList(rewardTaskListTask.get());
//            result.setGrouponRuleList(grouponRuleListTask.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseUtil.ok(result);
    }


    /**
     * 判断首页初始化参数是否支持跳转
     */
    public Object isNavigate(HomeNavigateBody body) {
        String sceneId = body.getSceneId();
        String sceneType = body.getSceneType();
        if (Objects.equals(sceneId, "0")) {
            return ResponseUtil.fail(800, "分享获取失败");
        }
        if (Objects.equals(sceneType, "goodsId")) {
            CarServiceGoods goods = goodsService.findById(sceneId);
            if (goods == null) {
                return ResponseUtil.fail(800, "商品不存在");
            }
        }
        if (Objects.equals(sceneType, "grouponId")) {
            Object groupon = grouponService.isGroupon(sceneId);
            if (groupon != null) {
                return groupon;
            }
        }
        if (Objects.equals(sceneType, "rewardId")) {
            CarServiceReward reward = rewardService.findById(sceneId);
            if (reward == null) {
                return ResponseUtil.fail(800, "活动不存在");
            }
            Object serviceReward = rewardCoreService.isReward(reward.getTaskId());
            if (serviceReward != null) {
                return serviceReward;
            }
        }
        return ResponseUtil.ok();
    }


    /**
     * 商城介绍信息
     *
     * @return 商城介绍信息
     */
    public Object about() {
        HomeAboutResult result = new HomeAboutResult();
        result.setName(SystemConfig.getMallName());
        result.setPhone(SystemConfig.getMallPhone());
        result.setQq(SystemConfig.getMallQQ());
        result.setLongitude(SystemConfig.getMallLongitude());
        result.setLatitude(SystemConfig.getMallLatitude());
        String tenantId = TenantContextHolder.getLocalTenantId();
        CarServiceTenant tenant = tenantService.findById(tenantId);
        if (tenant != null) {
            result.setAddress(tenant.getAddress());
        }
        return ResponseUtil.ok(result);
    }


}
