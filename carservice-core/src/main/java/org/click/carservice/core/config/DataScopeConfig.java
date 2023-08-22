package org.click.carservice.core.config;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.*;
import org.click.carservice.db.service.IBrandService;
import org.click.carservice.db.service.IGoodsService;
import org.click.carservice.db.service.IOrderGoodsService;
import org.click.carservice.db.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: DataScopeConfig
 * @Author HuangYan
 * @Date: 2023/8/22 1:02
 * @Version 1.0
 * @Description: 商户数据权限配置
 */
@Component
public class DataScopeConfig {


    private static IUserService userService;


    private static IBrandService brandService;

    private static IOrderGoodsService orderGoodsService;

    private static IGoodsService goodsService;

    @Autowired
    public void setUserService(IUserService userService) {
        DataScopeConfig.userService = userService;
    }

    @Autowired
    public void setBrandService(IBrandService brandService) {
        DataScopeConfig.brandService = brandService;
    }

    @Autowired
    public void setOrderGoodsService(IOrderGoodsService orderGoodsService) {
        DataScopeConfig.orderGoodsService = orderGoodsService;
    }

    @Autowired
    public void setGoodsService(IGoodsService goodsService) {
        DataScopeConfig.goodsService = goodsService;
    }

    public static boolean dataScope(QueryWrapper<CarServiceOrder> wrapper) {
        // 根据当前管理系统用户id获取到小程序商户id,从而查询到用户的所有订单
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String userId = String.valueOf(tokenInfo.getLoginId());
        if (!"1".equals(userId)) {
            // 设置为小程序端用户id
            userId = StpUtil.getSession().getString("carServiceUserId");
            CarServiceUser carServiceUser = userService.findById(userId);
            // 如果不存在商户账号，并且也不是管理员，则查询为空
            if (ObjectUtil.isEmpty(carServiceUser)) {
                return true;
            } else {
                // 获取当前用户的商品
                List<CarServiceBrand> brandList = brandService.selectByUserId(userId);
                if (CollUtil.isNotEmpty(brandList)) {
                    List<String> ids = brandList.stream().map(CarServiceBrand::getId).collect(Collectors.toList());
                    wrapper.in(CarServiceOrder.BRAND_ID, ids);
                }
            }

        }
        return false;
    }

    /**
     * 获取本商铺的订单商品表id
     * @param brandIds
     * @return
     */
    public static boolean getBrandIds(List<String> brandIds) {
        // 根据当前管理系统用户id获取到小程序商户id,从而查询到用户的所有订单
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String userId = String.valueOf(tokenInfo.getLoginId());
        if (!"1".equals(userId)) {
            // 设置为小程序端用户id
            userId = StpUtil.getSession().getString("carServiceUserId");
            CarServiceUser carServiceUser = userService.findById(userId);
            // 如果不存在商户账号，并且也不是管理员，则查询为空
            if (ObjectUtil.isEmpty(carServiceUser)) {
                return false;
            } else {
                // 获取当前用户的商品
                List<CarServiceBrand> brandList = brandService.selectByUserId(userId);
                if (CollUtil.isNotEmpty(brandList)) {
                    List<String> ids = brandList.stream().map(CarServiceBrand::getId).collect(Collectors.toList());
                    brandIds.addAll(ids);
                }
            }
        }
        return true;
    }

    /**
     * 获取本商铺的订单商品表id
     * @param goodIds
     * @return
     */
    public static boolean getOrderGoodsIds(List<String> goodIds) {
        // 根据当前管理系统用户id获取到小程序商户id,从而查询到用户的所有订单
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String userId = String.valueOf(tokenInfo.getLoginId());
        if (!"1".equals(userId)) {
            // 设置为小程序端用户id
            userId = StpUtil.getSession().getString("carServiceUserId");
            CarServiceUser carServiceUser = userService.findById(userId);
            // 如果不存在商户账号，并且也不是管理员，则查询为空
            if (ObjectUtil.isEmpty(carServiceUser)) {
                return false;
            } else {
                // 获取当前用户的商品
                List<CarServiceBrand> brandList = brandService.selectByUserId(userId);
                List<String> ids = new ArrayList<>();
                if (CollUtil.isNotEmpty(brandList)) {
                    ids = brandList.stream().map(CarServiceBrand::getId).collect(Collectors.toList());
                }
                if (CollUtil.isNotEmpty(ids)) {
                    List<CarServiceOrderGoods> goodsList = orderGoodsService.selectByBrandIds(ids);
                    if (CollUtil.isNotEmpty(goodsList)) {
                        goodIds.addAll(goodsList.stream().map(CarServiceOrderGoods::getId).collect(Collectors.toList()));
                    }
                }
            }
        }
        return true;
    }
}
