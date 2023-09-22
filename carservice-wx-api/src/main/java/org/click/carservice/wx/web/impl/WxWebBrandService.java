package org.click.carservice.wx.web.impl;
/**
 *  Copyright (c) [ysling] [927069313@qq.com]
 *  [CarService-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.service.DealingSlipCoreService;
import org.click.carservice.core.system.SystemConfig;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.*;
import org.click.carservice.db.entity.BaseOption;
import org.click.carservice.db.enums.BrandStatus;
import org.click.carservice.db.enums.GrouponStatus;
import org.click.carservice.db.enums.LikeType;
import org.click.carservice.db.enums.OrderStatus;
import org.click.carservice.wx.model.brand.body.BrandListBody;
import org.click.carservice.wx.model.brand.body.BrandOrderListBody;
import org.click.carservice.wx.model.brand.body.BrandSaveBody;
import org.click.carservice.wx.model.brand.result.*;
import org.click.carservice.wx.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺信息
 * @author Ysling
 */
@Slf4j
@Service
public class WxWebBrandService {

    @Autowired
    private WxBrandService brandService;
    @Autowired
    private WxGoodsService goodsService;
    @Autowired
    private WxGoodsSpecificationService specificationService;
    @Autowired
    private WxGoodsAttributeService attributeService;
    @Autowired
    private WxGoodsProductService productService;
    @Autowired
    private WxCategoryService categoryService;
    @Autowired
    private WxGrouponRulesService rulesService;
    @Autowired
    private WxUserService userService;
    @Autowired
    private WxOrderService orderService;
    @Autowired
    private WxOrderGoodsService orderGoodsService;
    @Autowired
    private WxGrouponService grouponService;
    @Autowired
    private WxLikeService likeService;
    @Autowired
    private DealingSlipCoreService slipCoreService;


    /**
     * 店铺列表
     */
    public Object list(BrandListBody body) {
        return ResponseUtil.okList(brandService.queryList(body));
    }


    /**
     * 店铺详情
     * @param brandId 品牌ID
     * @return 品牌详情
     */
    public Object read(String userId, String brandId) {
        return ResponseUtil.ok(brandService.findByUserId(userId ,brandId));
    }

    /**
     * 店铺详情
     * @param brandId 品牌ID
     * @return 品牌详情
     */
    public Object brandDetail(String userId ,String brandId) {
        CarServiceBrand brand = brandService.findById(brandId);
        if (brand == null) {
            return ResponseUtil.badArgumentValue();
        }
        //添加浏览量
        brand.setLookCount(brand.getLookCount() + 1);
        brandService.updateSelective(brand);
        BrandDetailResult result = new BrandDetailResult();
        result.setBrand(brand);
        result.setBrandLike(likeService.count(LikeType.TYPE_BRAND, brand.getId(), userId));
        if (brand.getUserId() != null){
            result.setBrandUser(userService.findUserVoById(brand.getUserId()));
        }
        return ResponseUtil.ok(result);
    }


    /**
     * 添加或修改店铺
     * @param userId    用户ID
     * @param body     店铺信息
     * @return 成功
     */
    public Object brandSave(String userId, BrandSaveBody body) {
        CarServiceBrand brand = new CarServiceBrand();
        BeanUtil.copyProperties(body , brand);
        Object error = brandService.validate(brand);
        if (error != null) {
            return error;
        }
        CarServiceUser user = userService.findById(userId);
        if (user == null) {
            return ResponseUtil.unlogin();
        }

        if (brand.getId() != null && !brand.getId().equals("0")){
            CarServiceBrand CarServiceBrand = brandService.findById(brand.getId());
            if (CarServiceBrand == null){
                return ResponseUtil.fail("店铺更新失败");
            }

            if (!brand.getUserId().equals(userId)){
                return ResponseUtil.fail("店铺更新失败");
            }

            if (CarServiceBrand.getStatus().equals(BrandStatus.STATUS_DISABLED.getStatus())){
                return ResponseUtil.fail("店铺被禁用");
            }

            if (CarServiceBrand.getStatus().equals(BrandStatus.STATUS_OUT.getStatus())){
                return ResponseUtil.fail("店铺被注销");
            }

            //更新店铺信息
            brand.setId(CarServiceBrand.getId());

            if (brandService.updateVersionSelective(brand) == 0) {
                return ResponseUtil.updatedDataFailed();
            }
        }else {
            brand.setUserId(userId);
            if (brandService.add(brand) == 0){
                throw new RuntimeException("店铺添加失败请重试");
            }
            //奖励5毛
//            slipCoreService.addIntegral(user, BigDecimal.valueOf(0.52), DealType.TYPE_BRAND);
        }
        if (StringUtils.hasText(body.getTrueName())){
            CarServiceUser service = userService.findById(userId);
            service.setUserType(1);
            service.setTrueName(body.getTrueName());
            if (userService.updateVersionSelective(service) == 0){
                throw new RuntimeException("用户更新失败请重试");
            }
        }
        return ResponseUtil.ok(brand);
    }

    /**
     * 店铺订单列表
     */
    public Object orderList(String userId, BrandOrderListBody body) {
        List<CarServiceBrand> brandList = brandService.queryByUserId(userId);
        if (brandList.size() != 1){
            return ResponseUtil.fail(800,"未找到店铺");
        }
        CarServiceBrand brand = brandList.get(0);
        List<Short> orderStatus = OrderStatus.brandOrderStatus(body.getShowType());
        List<CarServiceOrder> orderList = orderService.queryByBrandOrderStatus(brand.getId(), orderStatus, body);
        List<BrandOrderListResult> orderVoList = new ArrayList<>(orderList.size());
        for (CarServiceOrder order : orderList) {
            //拼装订单信息
            BrandOrderListResult result = new BrandOrderListResult();
            result.setGoods(orderGoodsService.findByOrderId(order.getId()));
            //拼装订单信息
            BeanUtil.copyProperties(order , result);
            result.setOrderStatusText(OrderStatus.orderStatusText(order));
            //添加团购信息
            CarServiceGroupon groupon = grouponService.findByOrderId(order.getId());
            result.setGroupon(groupon);
            if (groupon != null) {
                result.setIsGroupon(true);
                result.setRefund(GrouponStatus.isFail(groupon));
                result.setShip(GrouponStatus.isSucceed(groupon));
                result.setGrouponStatus(GrouponStatus.parseValue(groupon.getStatus()));
            } else {
                result.setIsGroupon(false);
                result.setRefund(OrderStatus.isRefundStatus(order));
                result.setShip(OrderStatus.isPayStatus(order) || OrderStatus.isBtlPayStatus(order));
            }
            orderVoList.add(result);
        }
        return ResponseUtil.okList(orderVoList,orderList);
    }

    /**
     * 商品上传参数初始化
     * @return 分类
     */
    public Object goodsInit() {
        BrandGoodsInitResult result = new BrandGoodsInitResult();
        result.setBrokerage(SystemConfig.getOrderBrokerage());
        result.setMinAmount(SystemConfig.getGoodsMinAmount());
        result.setMaxAmount(SystemConfig.getGoodsMaxAmount());
        return ResponseUtil.ok(result);
    }

    /**
     * 分类列表
     * @return 分类
     */
    public Object catList() {
        // http://element-cn.eleme.io/#/zh-CN/component/cascader
        // 管理员设置“所属分类”
        List<CarServiceCategory> l1CatList = categoryService.queryL1();
        List<BaseOption> categoryList = new ArrayList<>(l1CatList.size());
        for (CarServiceCategory l1 : l1CatList) {
            BaseOption l1CatVo = new BaseOption();
            l1CatVo.setValue(l1.getId());
            l1CatVo.setLabel(l1.getName());
            List<CarServiceCategory> l2CatList = categoryService.queryByPid(l1.getId());
            List<BaseOption> children = new ArrayList<>(l2CatList.size());
            for (CarServiceCategory l2 : l2CatList) {
                BaseOption l2CatVo = new BaseOption();
                l2CatVo.setValue(l2.getId());
                l2CatVo.setLabel(l2.getName());
                children.add(l2CatVo);
            }
            l1CatVo.setChildren(children);
            categoryList.add(l1CatVo);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("categoryList", categoryList);
        return ResponseUtil.ok(data);
    }

    /**
     * 店铺商品列表
     */
    public Object goodsList(BrandGoodsListBody body) {
        return ResponseUtil.okList(goodsService.queryByBrand(body));
    }

    /**
     * 店铺商品详情
     * @param id 商品ID
     * @return 商品信息
     */
    public Object goodsDetail(@NotNull String id) {
        CarServiceGoods goods = goodsService.findById(id);
        if (goods == null){
            return ResponseUtil.fail(600,"商品不存在");
        }
        List<CarServiceGoodsProduct> products = productService.queryByGid(id);
        List<CarServiceGrouponRules> grouponRules = rulesService.queryByGoodsId(id);
        List<CarServiceGoodsSpecification> specifications = specificationService.queryByGid(id);
        List<CarServiceGoodsAttribute> attributes = attributeService.queryByGid(id);

        String categoryId = goods.getCategoryId();
        CarServiceCategory category = categoryService.findById(categoryId);
        String[] categoryIds = new String[]{};
        if (category != null) {
            categoryIds = new String[]{category.getPid(), categoryId};
        }

        BrandGoodsDetailResult result = new BrandGoodsDetailResult();
        result.setGoods(goods);
        result.setCategoryIds(categoryIds);
        result.setAttributes(attributes);
        result.setProducts(products);
        result.setSpecifications(specifications);
        if (goods.getIsGroupon() && grouponRules.size() > 0){
            result.setGrouponRules(grouponRules.get(0));
        }
        return ResponseUtil.ok(result);
    }

}
