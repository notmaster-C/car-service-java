package org.click.carservice.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.*;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.enums.*;
import org.click.carservice.db.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 通用查询
 */
@Service
public class CommonService {


    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IGoodsSpecificationService specificationService;
    @Autowired
    private IGoodsAttributeService attributeService;
    @Autowired
    private IGoodsProductService productService;
    @Autowired
    private IGrouponRulesService grouponRulesService;
    @Autowired
    private IRewardTaskService rewardTaskService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ICouponService couponService;
    @Autowired
    private ICouponUserService couponUserService;
    @Autowired
    private IDealingSlipService dealingSlipService;
    @Autowired
    private IGoodsProductService goodsProductService;
    @Autowired
    private IRewardService rewardService;
    @Autowired
    private IShareService shareService;
    @Autowired
    private IOrderGoodsService orderGoodsService;
    @Autowired
    private IGrouponService grouponService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IAdminService adminService;
    @Autowired
    private IPermissionService permissionService;


    public CarServiceOrder findOrderById(String userId, String orderId) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.USER_ID, userId);
        wrapper.eq(CarServiceOrder.ID, orderId);
        return orderService.getOne(wrapper);
    }

    public List<CarServiceAdmin> findAdmin(String username) {
        QueryWrapper<CarServiceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAdmin.USERNAME, username);
        return adminService.list(wrapper);
    }

    public Set<String> queryByIds(String[] roleIds) {
        Set<String> roles = new HashSet<>();
        if (roleIds.length == 0) {
            return roles;
        }
        QueryWrapper<CarServiceRole> wrapper = new QueryWrapper<>();
        wrapper.in(CarServiceRole.ID, Arrays.asList(roleIds));
        wrapper.eq(CarServiceRole.ENABLED, true);
        for (CarServiceRole role : roleService.queryAll(wrapper)) {
            roles.add(role.getName());
        }
        return roles;
    }

    public Set<String> queryByRoleIds(String[] roleIds) {
        Set<String> permissionSet = new HashSet<>();
        if (roleIds.length == 0) {
            return permissionSet;
        }
        QueryWrapper<CarServicePermission> wrapper = new QueryWrapper<>();
        wrapper.in(CarServicePermission.ROLE_ID, Arrays.asList(roleIds));
        for (CarServicePermission permission : permissionService.queryAll(wrapper)) {
            permissionSet.add(permission.getPermission());
        }
        return permissionSet;
    }


    public CarServiceOrder findBySn(String userId, String orderSn) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.USER_ID, userId);
        wrapper.eq(CarServiceOrder.ORDER_SN, orderSn);
        return orderService.getOne(wrapper, false);
    }

    public List<CarServiceGroupon> queryByRuleId(String grouponRuleId) {
        QueryWrapper<CarServiceGroupon> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGroupon.RULES_ID, grouponRuleId);
        return grouponService.queryAll(wrapper);
    }

    /**
     * 查询新用户注册优惠券
     */
    public List<CarServiceCoupon> queryRegister() {
        QueryWrapper<CarServiceCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCoupon.TYPE, CouponType.TYPE_REGISTER.getStatus());
        wrapper.eq(CarServiceCoupon.STATUS, CouponStatus.STATUS_NORMAL.getStatus());
        return couponService.queryAll(wrapper);
    }


    public Integer countUserAndCoupon(String userId, String couponId) {
        QueryWrapper<CarServiceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCouponUser.USER_ID, userId);
        wrapper.eq(CarServiceCouponUser.COUPON_ID, couponId);
        return Math.toIntExact(couponUserService.count(wrapper));
    }


    public void addCouponUser(String userId, CarServiceCoupon coupon, String couponId) {
        CarServiceCouponUser couponUser = new CarServiceCouponUser();
        couponUser.setUserId(userId);
        couponUser.setCouponId(couponId);
        Short timeType = coupon.getTimeType();
        if (CouponTimeType.TIME_TYPE_TIME.equals(timeType)) {
            couponUser.setStartTime(coupon.getStartTime());
            couponUser.setEndTime(coupon.getEndTime());
        } else {
            LocalDateTime now = LocalDateTime.now();
            couponUser.setStartTime(now);
            couponUser.setEndTime(now.plusDays(coupon.getDays()));
        }
        couponUser.setAddTime(LocalDateTime.now());
        couponUser.setUpdateTime(LocalDateTime.now());
        couponUserService.add(couponUser);
    }

    /**
     * 添加优惠券使用信息
     *
     * @param couponId     优惠券ID
     * @param userCouponId 用户优惠券ID
     * @param orderId      订单ID
     */
    public void usedCoupon(String couponId, String userCouponId, String orderId) {
        // 如果couponId=0则没有优惠券，couponId=-1则不使用优惠券
        if (!"0".equals(couponId) && !"-1".equals(couponId)) {
            // 如果使用了优惠券，设置优惠券使用状态
            CarServiceCouponUser couponUser = couponUserService.findById(userCouponId);
            couponUser.setStatus(CouponUserStatus.STATUS_USED.getStatus());
            couponUser.setUsedTime(LocalDateTime.now());
            couponUser.setOrderId(orderId);
            if (couponUserService.updateVersionSelective(couponUser) == 0) {
                throw new RuntimeException("优惠券使用失败");
            }
        }
    }


    public CarServiceCouponUser queryOne(String userId, String couponId) {
        QueryWrapper<CarServiceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCouponUser.USER_ID, userId);
        wrapper.eq(CarServiceCouponUser.COUPON_ID, couponId);
        wrapper.eq(CarServiceCouponUser.STATUS, CouponUserStatus.STATUS_USABLE.getStatus());
        return couponUserService.getOne(wrapper, false);
    }

    /**
     * 查找最近十条交易记录
     *
     * @param userId 用户id
     * @param openId 用户openId
     * @return 最近十条交易记录
     */
    public List<CarServiceDealingSlip> querySelective(String userId, String openId) {
        QueryWrapper<CarServiceDealingSlip> wrapper = dealingSlipService.startPage(new PageBody(10));
        wrapper.eq(CarServiceDealingSlip.USER_ID, userId);
        wrapper.eq(CarServiceDealingSlip.OPENID, openId);
        return dealingSlipService.list(wrapper);
    }

    /**
     * 判断订单号是否存在
     */
    public Boolean countByOrderSn(String orderSn) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.ORDER_SN, orderSn);
        return orderService.exists(wrapper);
    }

    public CarServiceOrderGoods findByGoodsOrderId(String orderId) {
        QueryWrapper<CarServiceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrderGoods.ORDER_ID, orderId);
        return orderGoodsService.getOne(wrapper, false);
    }

    public CarServiceReward findByRewardOrderId(String orderId) {
        QueryWrapper<CarServiceReward> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceReward.ORDER_ID, orderId);
        return rewardService.getOne(wrapper, false);
    }

    public CarServiceShare findByShareOrderId(String orderId) {
        QueryWrapper<CarServiceShare> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceShare.ORDER_ID, orderId);
        return shareService.getOne(wrapper, false);
    }

    public boolean checkExistByName(String name) {
        QueryWrapper<CarServiceGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGoods.NAME, name);
        wrapper.eq(CarServiceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        return goodsService.exists(wrapper);
    }


    public CarServiceRewardTask findByRewardTaskGid(String goodsId) {
        QueryWrapper<CarServiceRewardTask> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceRewardTask.GOODS_ID, goodsId);
        return rewardTaskService.getOne(wrapper, false);
    }


    public void deleteByRewardTaskGid(String goodsId) {
        QueryWrapper<CarServiceRewardTask> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceRewardTask.GOODS_ID, goodsId);
        rewardTaskService.remove(wrapper);
    }

    public void deleteBySpecificationGid(String goodsId) {
        QueryWrapper<CarServiceGoodsSpecification> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGoodsSpecification.GOODS_ID, goodsId);
        specificationService.remove(wrapper);
    }

    public void deleteByAttributeGid(String goodsId) {
        QueryWrapper<CarServiceGoodsAttribute> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGoodsAttribute.GOODS_ID, goodsId);
        attributeService.remove(wrapper);
    }

    public void deleteByProductGid(String goodsId) {
        QueryWrapper<CarServiceGoodsProduct> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGoodsProduct.GOODS_ID, goodsId);
        productService.remove(wrapper);
    }

    public void updateProduct(String productId, String goodsSn, String goodsName, BigDecimal price, String url) {
        CarServiceCart cart = new CarServiceCart();
        cart.setPrice(price);
        cart.setPicUrl(url);
        cart.setGoodsSn(goodsSn);
        cart.setGoodsName(goodsName);
        QueryWrapper<CarServiceCart> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCart.PRODUCT_ID, productId);
        cartService.update(cart, wrapper);
    }

    public CarServiceGrouponRules findByGrouponRulesGid(String goodsId) {
        QueryWrapper<CarServiceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGrouponRules.GOODS_ID, goodsId);
        return grouponRulesService.getOne(wrapper, false);
    }

    public Integer countByGoodsId(String goodsId) {
        QueryWrapper<CarServiceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGrouponRules.GOODS_ID, goodsId);
        wrapper.eq(CarServiceGrouponRules.STATUS, GrouponRuleStatus.RULE_STATUS_ON.getStatus());
        return Math.toIntExact(grouponRulesService.count(wrapper));
    }

    public CarServiceGroupon queryById(String grouponId) {
        QueryWrapper<CarServiceGroupon> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGroupon.GROUPON_ID, grouponId);
        return grouponService.getOne(wrapper, false);
    }

    public List<CarServiceGroupon> queryJoinRecord(String grouponId) {
        QueryWrapper<CarServiceGroupon> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGroupon.GROUPON_ID, grouponId);
        return grouponService.queryAll(wrapper);
    }

    public List<CarServiceOrderGoods> queryByOid(String orderId) {
        QueryWrapper<CarServiceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrderGoods.ORDER_ID, orderId);
        return orderGoodsService.queryAll(wrapper);
    }

    public CarServiceOrderGoods findByOrderId(String orderId) {
        QueryWrapper<CarServiceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrderGoods.ORDER_ID, orderId);
        return orderGoodsService.getOne(wrapper, false);
    }

    public CarServiceGroupon findByGrouponOrderId(String orderId) {
        QueryWrapper<CarServiceGroupon> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGroupon.ORDER_ID, orderId);
        return grouponService.getOne(wrapper, false);
    }

    public List<CarServiceOrderGoods> queryByOrderGoodsOid(String orderId) {
        QueryWrapper<CarServiceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrderGoods.ORDER_ID, orderId);
        return orderGoodsService.queryAll(wrapper);
    }

    public List<CarServiceCouponUser> queryByCouponUserOid(String orderId) {
        QueryWrapper<CarServiceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCouponUser.ORDER_ID, orderId);
        return couponUserService.queryAll(wrapper);
    }

    /**
     * 更新商品状态
     *
     * @param goodsId 商品ID
     * @param status  状态
     */
    public Integer updateGoodsStatus(String goodsId, Short status) {
        CarServiceGoods goods = new CarServiceGoods();
        goods.setId(goodsId);
        goods.setStatus(status);
        goods.setUpdateTime(LocalDateTime.now());
        return goodsService.updateSelective(goods);
    }

    /**
     * 判断outBatchNo是否存在
     *
     * @param userId   用户id
     * @param orderSn  订单编号
     * @param dealType 交易类型
     * @return true
     */
    public Boolean isDealingSlip(String userId, String orderSn, DealType dealType) {
        QueryWrapper<CarServiceDealingSlip> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceDealingSlip.USER_ID, userId);
        wrapper.eq(CarServiceDealingSlip.ORDER_SN, orderSn);
        wrapper.eq(CarServiceDealingSlip.DEAL_TYPE, dealType.getType());
        return dealingSlipService.exists(wrapper);
    }

    /**
     * 判断商品是否库存不足
     *
     * @param goodsId 商品ID
     */
    public Boolean isGoodsNoStock(String goodsId) {
        QueryWrapper<CarServiceGoodsProduct> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGoodsProduct.GOODS_ID, goodsId);
        List<CarServiceGoodsProduct> goodsProducts = goodsProductService.queryAll(wrapper);
        Integer number = 0;
        for (CarServiceGoodsProduct product : goodsProducts) {
            number += product.getNumber();
        }
        return number <= 0;
    }

    public Integer countReward(String taskId) {
        QueryWrapper<CarServiceReward> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceReward.TASK_ID, taskId);
        return Math.toIntExact(rewardService.count(wrapper));
    }

}
