package org.click.carservice.wx.service;
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.CarServiceCart;
import org.click.carservice.db.domain.CarServiceGoods;
import org.click.carservice.db.domain.CarServiceGoodsProduct;
import org.click.carservice.db.service.impl.CartServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 购物车服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_cart")
public class WxCartService extends CartServiceImpl {


    @Cacheable(sync = true)
    public CarServiceCart queryExist(String goodsId, String productId, String userId) {
        QueryWrapper<CarServiceCart> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCart.GOODS_ID, goodsId);
        wrapper.eq(CarServiceCart.PRODUCT_ID, productId);
        wrapper.eq(CarServiceCart.USER_ID, userId);
        return getOne(wrapper);
    }


    /**
     * 添加到购物车
     */
    @CacheEvict(allEntries = true)
    public boolean addCart(String userId, CarServiceCart cart, Integer number, CarServiceGoods goods, CarServiceGoodsProduct product) {
        if (product == null || number > product.getNumber()) {
            return true;
        }
        cart.setId(null);
        cart.setBrandId(goods.getBrandId());
        cart.setGoodsSn(goods.getGoodsSn());
        cart.setGoodsName((goods.getName()));
        if (Objects.isNull(product.getUrl())) {
            cart.setPicUrl(goods.getPicUrl());
        } else {
            cart.setPicUrl(product.getUrl());
        }
        cart.setPrice(product.getPrice());
        cart.setSpecifications(product.getSpecifications());
        cart.setUserId(userId);
        cart.setChecked(true);
        this.add(cart);
        return false;
    }


    @Cacheable(sync = true)
    public List<CarServiceCart> queryByUid(String userId) {
        QueryWrapper<CarServiceCart> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCart.USER_ID, userId);
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<CarServiceCart> queryByUidAndChecked(String userId) {
        QueryWrapper<CarServiceCart> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCart.USER_ID, userId);
        wrapper.eq(CarServiceCart.CHECKED, true);
        return queryAll(wrapper);
    }


    @CacheEvict(allEntries = true)
    public boolean delete(List<String> productIdList, String userId) {
        QueryWrapper<CarServiceCart> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCart.USER_ID, userId);
        wrapper.in(CarServiceCart.PRODUCT_ID, productIdList);
        return remove(wrapper);
    }

    @Cacheable(sync = true)
    public CarServiceCart findById(String userId, String id) {
        QueryWrapper<CarServiceCart> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCart.USER_ID, userId);
        wrapper.in(CarServiceCart.ID, id);
        return getOne(wrapper);
    }


    @CacheEvict(allEntries = true)
    public void updateCheck(String userId, List<String> idsList, Boolean checked) {
        QueryWrapper<CarServiceCart> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCart.USER_ID, userId);
        wrapper.in(CarServiceCart.PRODUCT_ID, idsList);
        CarServiceCart cart = new CarServiceCart();
        cart.setChecked(checked);
        update(cart, wrapper);
    }


    /**
     * 获取选择商品
     */
    @Cacheable(sync = true)
    public List<CarServiceCart> getCheckedGoods(String userId, String cartId) {
        if (cartId == null || cartId.equals("0")) {
            return queryByUidAndChecked(userId);
        } else {
            CarServiceCart cart = findById(userId, cartId);
            if (cart == null) {
                return null;
            }
            return Collections.singletonList(cart);
        }
    }

}
