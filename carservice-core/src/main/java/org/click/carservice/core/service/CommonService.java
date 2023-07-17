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


    public carserviceOrder findOrderById(String userId, String orderId) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.USER_ID, userId);
        wrapper.eq(carserviceOrder.ID, orderId);
        return orderService.getOne(wrapper);
    }

    public List<carserviceAdmin> findAdmin(String username) {
        QueryWrapper<carserviceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceAdmin.USERNAME, username);
        return adminService.list(wrapper);
    }

    public Set<String> queryByIds(String[] roleIds) {
        Set<String> roles = new HashSet<>();
        if (roleIds.length == 0) {
            return roles;
        }
        QueryWrapper<carserviceRole> wrapper = new QueryWrapper<>();
        wrapper.in(carserviceRole.ID, Arrays.asList(roleIds));
        wrapper.eq(carserviceRole.ENABLED, true);
        for (carserviceRole role : roleService.queryAll(wrapper)) {
            roles.add(role.getName());
        }
        return roles;
    }

    public Set<String> queryByRoleIds(String[] roleIds) {
        Set<String> permissionSet = new HashSet<>();
        if (roleIds.length == 0) {
            return permissionSet;
        }
        QueryWrapper<carservicePermission> wrapper = new QueryWrapper<>();
        wrapper.in(carservicePermission.ROLE_ID, Arrays.asList(roleIds));
        for (carservicePermission permission : permissionService.queryAll(wrapper)) {
            permissionSet.add(permission.getPermission());
        }
        return permissionSet;
    }


    public carserviceOrder findBySn(String userId, String orderSn) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.USER_ID, userId);
        wrapper.eq(carserviceOrder.ORDER_SN, orderSn);
        return orderService.getOne(wrapper, false);
    }

    public List<carserviceGroupon> queryByRuleId(String grouponRuleId) {
        QueryWrapper<carserviceGroupon> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGroupon.RULES_ID, grouponRuleId);
        return grouponService.queryAll(wrapper);
    }

    /**
     * 查询新用户注册优惠券
     */
    public List<carserviceCoupon> queryRegister() {
        QueryWrapper<carserviceCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCoupon.TYPE, CouponType.TYPE_REGISTER.getStatus());
        wrapper.eq(carserviceCoupon.STATUS, CouponStatus.STATUS_NORMAL.getStatus());
        return couponService.queryAll(wrapper);
    }


    public Integer countUserAndCoupon(String userId, String couponId) {
        QueryWrapper<carserviceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCouponUser.USER_ID, userId);
        wrapper.eq(carserviceCouponUser.COUPON_ID, couponId);
        return Math.toIntExact(couponUserService.count(wrapper));
    }


    public void addCouponUser(String userId, carserviceCoupon coupon, String couponId) {
        carserviceCouponUser couponUser = new carserviceCouponUser();
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
            carserviceCouponUser couponUser = couponUserService.findById(userCouponId);
            couponUser.setStatus(CouponUserStatus.STATUS_USED.getStatus());
            couponUser.setUsedTime(LocalDateTime.now());
            couponUser.setOrderId(orderId);
            if (couponUserService.updateVersionSelective(couponUser) == 0) {
                throw new RuntimeException("优惠券使用失败");
            }
        }
    }


    public carserviceCouponUser queryOne(String userId, String couponId) {
        QueryWrapper<carserviceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCouponUser.USER_ID, userId);
        wrapper.eq(carserviceCouponUser.COUPON_ID, couponId);
        wrapper.eq(carserviceCouponUser.STATUS, CouponUserStatus.STATUS_USABLE.getStatus());
        return couponUserService.getOne(wrapper, false);
    }

    /**
     * 查找最近十条交易记录
     *
     * @param userId 用户id
     * @param openId 用户openId
     * @return 最近十条交易记录
     */
    public List<carserviceDealingSlip> querySelective(String userId, String openId) {
        QueryWrapper<carserviceDealingSlip> wrapper = dealingSlipService.startPage(new PageBody(10));
        wrapper.eq(carserviceDealingSlip.USER_ID, userId);
        wrapper.eq(carserviceDealingSlip.OPENID, openId);
        return dealingSlipService.list(wrapper);
    }

    /**
     * 判断订单号是否存在
     */
    public Boolean countByOrderSn(String orderSn) {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.ORDER_SN, orderSn);
        return orderService.exists(wrapper);
    }

    public carserviceOrderGoods findByGoodsOrderId(String orderId) {
        QueryWrapper<carserviceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrderGoods.ORDER_ID, orderId);
        return orderGoodsService.getOne(wrapper, false);
    }

    public carserviceReward findByRewardOrderId(String orderId) {
        QueryWrapper<carserviceReward> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceReward.ORDER_ID, orderId);
        return rewardService.getOne(wrapper, false);
    }

    public carserviceShare findByShareOrderId(String orderId) {
        QueryWrapper<carserviceShare> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceShare.ORDER_ID, orderId);
        return shareService.getOne(wrapper, false);
    }

    public boolean checkExistByName(String name) {
        QueryWrapper<carserviceGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGoods.NAME, name);
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        return goodsService.exists(wrapper);
    }


    public carserviceRewardTask findByRewardTaskGid(String goodsId) {
        QueryWrapper<carserviceRewardTask> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceRewardTask.GOODS_ID, goodsId);
        return rewardTaskService.getOne(wrapper, false);
    }


    public void deleteByRewardTaskGid(String goodsId) {
        QueryWrapper<carserviceRewardTask> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceRewardTask.GOODS_ID, goodsId);
        rewardTaskService.remove(wrapper);
    }

    public void deleteBySpecificationGid(String goodsId) {
        QueryWrapper<carserviceGoodsSpecification> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGoodsSpecification.GOODS_ID, goodsId);
        specificationService.remove(wrapper);
    }

    public void deleteByAttributeGid(String goodsId) {
        QueryWrapper<carserviceGoodsAttribute> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGoodsAttribute.GOODS_ID, goodsId);
        attributeService.remove(wrapper);
    }

    public void deleteByProductGid(String goodsId) {
        QueryWrapper<carserviceGoodsProduct> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGoodsProduct.GOODS_ID, goodsId);
        productService.remove(wrapper);
    }

    public void updateProduct(String productId, String goodsSn, String goodsName, BigDecimal price, String url) {
        carserviceCart cart = new carserviceCart();
        cart.setPrice(price);
        cart.setPicUrl(url);
        cart.setGoodsSn(goodsSn);
        cart.setGoodsName(goodsName);
        QueryWrapper<carserviceCart> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCart.PRODUCT_ID, productId);
        cartService.update(cart, wrapper);
    }

    public carserviceGrouponRules findByGrouponRulesGid(String goodsId) {
        QueryWrapper<carserviceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGrouponRules.GOODS_ID, goodsId);
        return grouponRulesService.getOne(wrapper, false);
    }

    public Integer countByGoodsId(String goodsId) {
        QueryWrapper<carserviceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGrouponRules.GOODS_ID, goodsId);
        wrapper.eq(carserviceGrouponRules.STATUS, GrouponRuleStatus.RULE_STATUS_ON.getStatus());
        return Math.toIntExact(grouponRulesService.count(wrapper));
    }

    public carserviceGroupon queryById(String grouponId) {
        QueryWrapper<carserviceGroupon> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGroupon.GROUPON_ID, grouponId);
        return grouponService.getOne(wrapper, false);
    }

    public List<carserviceGroupon> queryJoinRecord(String grouponId) {
        QueryWrapper<carserviceGroupon> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGroupon.GROUPON_ID, grouponId);
        return grouponService.queryAll(wrapper);
    }

    public List<carserviceOrderGoods> queryByOid(String orderId) {
        QueryWrapper<carserviceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrderGoods.ORDER_ID, orderId);
        return orderGoodsService.queryAll(wrapper);
    }

    public carserviceOrderGoods findByOrderId(String orderId) {
        QueryWrapper<carserviceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrderGoods.ORDER_ID, orderId);
        return orderGoodsService.getOne(wrapper, false);
    }

    public carserviceGroupon findByGrouponOrderId(String orderId) {
        QueryWrapper<carserviceGroupon> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGroupon.ORDER_ID, orderId);
        return grouponService.getOne(wrapper, false);
    }

    public List<carserviceOrderGoods> queryByOrderGoodsOid(String orderId) {
        QueryWrapper<carserviceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrderGoods.ORDER_ID, orderId);
        return orderGoodsService.queryAll(wrapper);
    }

    public List<carserviceCouponUser> queryByCouponUserOid(String orderId) {
        QueryWrapper<carserviceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCouponUser.ORDER_ID, orderId);
        return couponUserService.queryAll(wrapper);
    }

    /**
     * 更新商品状态
     *
     * @param goodsId 商品ID
     * @param status  状态
     */
    public Integer updateGoodsStatus(String goodsId, Short status) {
        carserviceGoods goods = new carserviceGoods();
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
        QueryWrapper<carserviceDealingSlip> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceDealingSlip.USER_ID, userId);
        wrapper.eq(carserviceDealingSlip.ORDER_SN, orderSn);
        wrapper.eq(carserviceDealingSlip.DEAL_TYPE, dealType.getType());
        return dealingSlipService.exists(wrapper);
    }

    /**
     * 判断商品是否库存不足
     *
     * @param goodsId 商品ID
     */
    public Boolean isGoodsNoStock(String goodsId) {
        QueryWrapper<carserviceGoodsProduct> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGoodsProduct.GOODS_ID, goodsId);
        List<carserviceGoodsProduct> goodsProducts = goodsProductService.queryAll(wrapper);
        Integer number = 0;
        for (carserviceGoodsProduct product : goodsProducts) {
            number += product.getNumber();
        }
        return number <= 0;
    }

    public Integer countReward(String taskId) {
        QueryWrapper<carserviceReward> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceReward.TASK_ID, taskId);
        return Math.toIntExact(rewardService.count(wrapper));
    }

}
