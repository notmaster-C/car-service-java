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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.ysling.litemall.core.annotation.JsonBody;
import org.ysling.litemall.core.service.CouponVerifyService;
import org.ysling.litemall.core.service.DealingSlipCoreService;
import org.ysling.litemall.core.system.SystemConfig;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.enums.GoodsStatus;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.db.domain.*;
import org.ysling.litemall.wx.service.*;
import org.ysling.litemall.wx.model.cart.body.CartCheckedBody;
import org.ysling.litemall.wx.model.cart.body.CartCheckoutBody;
import org.ysling.litemall.wx.model.cart.result.CartCheckoutResult;
import org.ysling.litemall.wx.model.cart.result.CartIndexResult;
import org.ysling.litemall.wx.model.cart.result.CartTotalResult;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用户购物车服务
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/wx/cart")
@Validated
public class WxCartController {

    @Autowired
    private WxUserService userService;
    @Autowired
    private DealingSlipCoreService slipCoreService;
    @Autowired
    private WxCartService cartService;
    @Autowired
    private WxGoodsService goodsService;
    @Autowired
    private WxGoodsProductService productService;
    @Autowired
    private WxAddressService addressService;
    @Autowired
    private WxGrouponRulesService grouponRulesService;
    @Autowired
    private WxBrandService brandService;
    @Autowired
    private WxCouponUserService couponUserService;
    @Autowired
    private CouponVerifyService couponVerifyService;

    /**
     * 用户购物车信息
     *
     * @param userId 用户ID
     * @return 用户购物车信息
     */
    @GetMapping("index")
    public Object index(@LoginUser String userId) {

        List<LitemallCart> list = cartService.queryByUid(userId);
        List<LitemallCart> cartList = new ArrayList<>();

        // TODO
        // 如果系统检查商品已删除或已下架，则系统自动删除。
        // 更好的效果应该是告知用户商品失效，允许用户点击按钮来清除失效商品。
        for (LitemallCart cart : list) {
            LitemallGoods goods = goodsService.findById(cart.getGoodsId());
            if (goods == null || !GoodsStatus.getIsOnSale(goods)) {
                if (cartService.deleteById(cart.getId()) == 0){
                    throw new RuntimeException("购物车商品删除失败");
                }
                log.debug("系统自动删除失效购物车商品 goodsId=" + cart.getGoodsId() + " productId=" + cart.getProductId());
            }
            else{
                cartList.add(cart);
            }
        }

        //商品数量
        Integer goodsCount = 0;
        //购物车商品总价
        BigDecimal goodsAmount = new BigDecimal("0.00");
        //选中商品数量
        Integer checkedGoodsCount = 0;
        //选中商品总价
        BigDecimal checkedGoodsAmount = new BigDecimal("0.00");
        for (LitemallCart cart : cartList) {
            goodsCount += cart.getNumber();
            goodsAmount = goodsAmount.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNumber())));
            if (cart.getChecked()) {
                checkedGoodsCount += cart.getNumber();
                checkedGoodsAmount = checkedGoodsAmount.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNumber())));
            }
        }

        //添加购物车信息
        CartTotalResult cartTotal = new CartTotalResult();
        cartTotal.setGoodsCount(goodsCount);
        cartTotal.setGoodsAmount(goodsAmount);
        cartTotal.setCheckedGoodsCount(checkedGoodsCount);
        cartTotal.setCheckedGoodsAmount(checkedGoodsAmount);
        //结果
        CartIndexResult result = new CartIndexResult();
        result.setCartTotal(cartTotal);
        result.setCartList(cartList);
        return ResponseUtil.ok(result);
    }

    /**
     * 加入商品到购物车
     * <p>
     * 如果已经存在购物车货品，则增加数量；
     * 否则添加新的购物车货品项。
     *
     * @param userId 用户ID
     * @param cart   购物车商品信息， { goodsId: xxx, productId: xxx, number: xxx }
     * @return 加入购物车操作结果
     */
    @PostMapping("add")
    public Object add(@LoginUser String userId, @Valid @RequestBody LitemallCart cart) {
        String goodsId = cart.getGoodsId();
        String productId = cart.getProductId();
        Integer number = cart.getNumber().intValue();
        if (!ObjectUtils.allNotNull(productId, number, goodsId)) {
            return ResponseUtil.badArgument();
        }
        if(number <= 0){
            return ResponseUtil.badArgument();
        }

        //判断商品是否可以购买
        LitemallGoods goods = goodsService.findById(goodsId);
        if (goods == null || !GoodsStatus.getIsOnSale(goods)) {
            return ResponseUtil.fail("商品已下架");
        }

        //添加店铺名称
        LitemallBrand brand = brandService.findById(goods.getBrandId());
        if (brand != null) {
            cart.setBrandName(brand.getName());
        }
        LitemallGoodsProduct product = productService.findById(productId);
        //判断购物车中是否存在此规格商品
        LitemallCart existCart = cartService.queryExist(goodsId, productId, userId);
        if (existCart == null) {
            //取得规格的信息,判断规格库存
            if (cartService.addCart(userId, cart, number, goods, product)) {
                return ResponseUtil.fail( "库存不足");
            }
        } else {
            //取得规格的信息,判断规格库存
            int num = existCart.getNumber() + number;
            if (num > product.getNumber()) {
                return ResponseUtil.fail( "库存不足");
            }
            existCart.setNumber((short) num);
            if (cartService.updateVersionSelective(existCart) == 0) {
                return ResponseUtil.updatedDataFailed();
            }
        }

        return goodsCount(userId);
    }

    /**
     * 立即购买
     * <p>
     * 和add方法的区别在于：
     * 1. 如果购物车内已经存在购物车货品，前者的逻辑是数量添加，这里的逻辑是数量覆盖
     * 2. 添加成功以后，前者的逻辑是返回当前购物车商品数量，这里的逻辑是返回对应购物车项的ID
     *
     * @param userId 用户ID
     * @param cart   购物车商品信息， { goodsId: xxx, productId: xxx, number: xxx }
     * @return 立即购买操作结果
     */
    @PostMapping("fast/add")
    public Object fastAdd(@LoginUser String userId, @Valid @RequestBody LitemallCart cart) {
        String goodsId = cart.getGoodsId();
        String productId = cart.getProductId();
        Integer number = cart.getNumber().intValue();
        if (!ObjectUtils.allNotNull(productId, number, goodsId)) {
            return ResponseUtil.badArgument();
        }
        if(number <= 0){
            return ResponseUtil.badArgument();
        }

        //判断商品是否可以购买
        LitemallGoods goods = goodsService.findById(goodsId);
        if (goods == null || !GoodsStatus.getIsOnSale(goods)) {
            return ResponseUtil.fail( "商品已下架");
        }

        //添加店铺名称
        LitemallBrand brand = brandService.findById(goods.getBrandId());
        if (brand != null) {
            cart.setBrandName(brand.getName());
        }

        LitemallGoodsProduct product = productService.findById(productId);
        //判断购物车中是否存在此规格商品
        LitemallCart existCart = cartService.queryExist(goodsId, productId, userId);
        if (existCart == null) {
            //取得规格的信息,判断规格库存
            if (cartService.addCart(userId, cart, number, goods, product)) {
                return ResponseUtil.fail("库存不足");
            }
        } else {
            //取得规格的信息,判断规格库存
            int num = number;
            if (num > product.getNumber()) {
                return ResponseUtil.fail( "库存不足");
            }
            existCart.setNumber((short) num);
            if (cartService.updateVersionSelective(existCart) == 0) {
                return ResponseUtil.updatedDataFailed();
            }
        }

        return ResponseUtil.ok(existCart != null ? existCart.getId() : cart.getId());
    }

    /**
     * 修改购物车商品货品数量
     *
     * @param userId 用户ID
     * @param cartItem   购物车商品信息， { id: xxx, goodsId: xxx, productId: xxx, number: xxx }
     * @return 修改结果
     */
    @PostMapping("update")
    public Object update(@LoginUser String userId, @Valid @RequestBody LitemallCart cartItem) {
        String id = cartItem.getId();
        String goodsId = cartItem.getGoodsId();
        String productId = cartItem.getProductId();
        Integer number = cartItem.getNumber().intValue();
        if (!ObjectUtils.allNotNull(id, productId, number, goodsId)) {
            return ResponseUtil.badArgument();
        }
        if(number <= 0){
            return ResponseUtil.badArgument();
        }

        //判断是否存在该订单
        // 如果不存在，直接返回错误
        LitemallCart existCart = cartService.findById(userId, id);
        if (existCart == null) {
            return ResponseUtil.badArgumentValue();
        }

        // 判断goodsId是否与当前cart里的值一致
        if (!existCart.getGoodsId().equals(goodsId)) {
            return ResponseUtil.badArgumentValue();
        }

        //判断商品是否可以购买
        LitemallGoods goods = goodsService.findById(goodsId);
        if (goods == null || !GoodsStatus.getIsOnSale(goods)) {
            return ResponseUtil.fail( "商品已下架");
        }

        //取得规格的信息,判断规格库存
        LitemallGoodsProduct product = productService.findById(productId);
        if (product == null || product.getNumber() < number) {
            return ResponseUtil.fail( "库存不足");
        }

        existCart.setNumber(number.shortValue());
        //判断规格id是否一致不一致则更改规格
        if (!existCart.getProductId().equals(productId)) {
            existCart.setSpecifications(product.getSpecifications());
            existCart.setPrice(product.getPrice());
            existCart.setPicUrl(product.getUrl());
        }

        if (cartService.updateVersionSelective(existCart) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }

    /**
     * 购物车商品货品勾选状态
     * <p>
     * 如果原来没有勾选，则设置勾选状态；如果商品已经勾选，则设置非勾选状态。
     *
     * @param userId 用户ID
     * @param body   购物车商品信息， { productIds: xxx, isChecked: 1/0 }
     * @return 购物车信息
     */
    @PostMapping("checked")
    public Object checked(@LoginUser String userId, @Valid @RequestBody CartCheckedBody body) {
        List<String> productIds = body.getProductIds();
        Boolean isChecked = (body.getIsChecked() == 1);
        cartService.updateCheck(userId, productIds, isChecked);
        return index(userId);
    }

    /**
     * 购物车商品删除
     * @param userId 用户ID
     * @param productIds   购物车商品信息， { productIds: xxx }
     * @return 购物车信息
     */
    @PostMapping("delete")
    public Object delete(@LoginUser String userId, @JsonBody List<String> productIds) {
        cartService.delete(productIds, userId);
        return index(userId);
    }

    /**
     * 购物车商品货品数量
     * <p>
     * 如果用户没有登录，则返回空数据。
     *
     * @param userId 用户ID
     * @return 购物车商品货品数量
     */
    @GetMapping("count")
    public Object goodsCount(@LoginUser(require = false) String userId) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.ok(0);
        }
        int goodsCount = 0;
        List<LitemallCart> cartList = cartService.queryByUid(userId);
        for (LitemallCart cart : cartList) {
            goodsCount += cart.getNumber();
        }
        return ResponseUtil.ok(goodsCount);
    }


    /**
     * 购物车下单
     */
    @GetMapping("checkout")
    public Object checkout(@LoginUser String userId, CartCheckoutBody body) {
        String cartId = body.getCartId();
        String couponId = body.getCouponId();
        String addressId = body.getAddressId();
        String userCouponId = body.getUserCouponId();
        String grouponRulesId = body.getGrouponRulesId();

        // 收货地址
        LitemallAddress checkedAddress = null;
        if (addressId != null && !addressId.equals("0")) {
            checkedAddress = addressService.query(userId, addressId);
        }
        if (checkedAddress == null) {
            checkedAddress = addressService.findDefault(userId);
            // 如果仍然没有地址，则是没有收货地址
            // 返回一个空的地址id=0，这样前端则会提醒添加地址
            if (checkedAddress == null) {
                checkedAddress = new LitemallAddress();
                checkedAddress.setId("0");
                addressId = "0";
            } else {
                addressId = checkedAddress.getId();
            }
        }

        //选中的商品
        List<LitemallCart> checkedGoodsList  = cartService.getCheckedGoods(userId, cartId);
        if (checkedGoodsList == null) {
            return ResponseUtil.badArgument();
        }

        // 团购优惠
        BigDecimal grouponPrice = new BigDecimal("0.00");
        // 商品总价
        BigDecimal checkedGoodsPrice = new BigDecimal("0.00");
        for (LitemallCart cart :checkedGoodsList) {
            checkedGoodsPrice = checkedGoodsPrice.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNumber())));
            LitemallGrouponRules grouponRules = grouponRulesService.findById(grouponRulesId);
            if (grouponRules != null) {
                grouponPrice = grouponPrice.add(grouponRules.getDiscount().multiply(BigDecimal.valueOf(cart.getNumber())));
            }
        }

        // 计算优惠券可用情况
        BigDecimal tmpCouponPrice = new BigDecimal("0.00");
        String tmpCouponId = "0";
        String tmpUserCouponId = "0";
        int tmpCouponLength = 0;
        List<LitemallCouponUser> couponUserList = couponUserService.queryAll(userId);
        for(LitemallCouponUser couponUser : couponUserList){
            LitemallCoupon coupon = couponVerifyService.checkCoupon(userId, couponUser.getCouponId(), couponUser.getId(), checkedGoodsList);
            if(coupon == null){
                continue;
            }
            tmpCouponLength++;
            if(tmpCouponPrice.compareTo(coupon.getDiscount()) < 0){
                tmpCouponPrice = coupon.getDiscount();
                tmpCouponId = coupon.getId();
                tmpUserCouponId = couponUser.getId();
            }
        }
        // 获取优惠券减免金额，优惠券可用数量
        Integer availableCouponLength = tmpCouponLength;
        BigDecimal couponPrice = BigDecimal.valueOf(0);
        // 这里存在三种情况
        // 1. 用户不想使用优惠券，则不处理
        // 2. 用户想自动使用优惠券，则选择合适优惠券
        // 3. 用户已选择优惠券，则测试优惠券是否合适
        if (couponId == null || couponId.equals("-1")){
            couponId = "-1";
            userCouponId = "-1";
        } else if (couponId.equals("0")) {
            couponPrice = tmpCouponPrice;
            couponId = tmpCouponId;
            userCouponId = tmpUserCouponId;
        } else {
            LitemallCoupon coupon = couponVerifyService.checkCoupon(userId, couponId, userCouponId, checkedGoodsList);
            // 用户选择的优惠券有问题，则选择合适优惠券，否则使用用户选择的优惠券
            if (coupon == null){
                couponPrice = tmpCouponPrice;
                couponId = tmpCouponId;
                userCouponId = tmpUserCouponId;
            } else {
                couponPrice = coupon.getDiscount();
            }
        }

        // 根据订单商品总价计算运费，满88则免运费，否则8元；
        BigDecimal freightPrice = new BigDecimal("0.00");
        if (checkedGoodsPrice.compareTo(SystemConfig.getFreightMin()) < 0) {
            freightPrice = SystemConfig.getFreightValue();
        }

        // 订单金额 = (选中商品价格 + 运费 )
        BigDecimal orderTotalPrice = checkedGoodsPrice.add(freightPrice).max(BigDecimal.valueOf(0));

        // 最终支付费用 = (订单金额 - 优惠券减免 - 团购优惠 - 余额)
        BigDecimal actualPrice = orderTotalPrice.subtract(couponPrice).subtract(grouponPrice).max(BigDecimal.valueOf(0));

        // 余额减免
        BigDecimal integralPrice = new BigDecimal("0.00");
        LitemallUser user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (slipCoreService.isDeduction(user) == null){
            BigDecimal userIntegral = user.getIntegral();
            if (actualPrice.compareTo(userIntegral) >= 0){
                actualPrice = actualPrice.subtract(userIntegral);
                integralPrice = userIntegral;
            }else {
                integralPrice = actualPrice;
                actualPrice = BigDecimal.valueOf(0);
            }
        }

        CartCheckoutResult result = new CartCheckoutResult();
        result.setAddressId(addressId);
        result.setCouponId(couponId);
        result.setUserCouponId(userCouponId);
        result.setCartId(cartId);
        result.setGrouponRulesId(grouponRulesId);
        result.setGrouponPrice(grouponPrice);
        result.setIntegralPrice(integralPrice);
        result.setCheckedAddress(checkedAddress);
        result.setAvailableCouponLength(availableCouponLength);
        result.setGoodsTotalPrice(checkedGoodsPrice);
        result.setFreightPrice(freightPrice);
        result.setCouponPrice(couponPrice);
        result.setOrderTotalPrice(orderTotalPrice);
        result.setActualPrice(actualPrice);
        result.setCheckedGoodsList(checkedGoodsList);
        return ResponseUtil.ok(result);
    }
}
