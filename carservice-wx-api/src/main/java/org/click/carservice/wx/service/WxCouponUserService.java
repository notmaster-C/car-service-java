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
import org.click.carservice.db.domain.CarServiceCouponUser;
import org.click.carservice.db.service.impl.CouponUserServiceImpl;
import org.click.carservice.wx.model.coupon.body.CouponListBody;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券使用服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_coupon_user")
public class WxCouponUserService extends CouponUserServiceImpl {


    @Cacheable(sync = true)
    public Integer countUserAndCoupon(String userId, String couponId) {
        QueryWrapper<CarServiceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCouponUser.USER_ID, userId);
        wrapper.eq(CarServiceCouponUser.COUPON_ID, couponId);
        return Math.toIntExact(count(wrapper));
    }


    @Cacheable(sync = true)
    public Integer countCoupon(String couponId) {
        QueryWrapper<CarServiceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCouponUser.COUPON_ID, couponId);
        return Math.toIntExact(count(wrapper));
    }


    @Cacheable(sync = true)
    public List<CarServiceCouponUser> queryList(String userId, CouponListBody body) {
        QueryWrapper<CarServiceCouponUser> wrapper = startPage(body);
        if (userId != null) {
            wrapper.eq(CarServiceCouponUser.USER_ID, userId);
        }
        if (body.getStatus() != null) {
            wrapper.eq(CarServiceCouponUser.STATUS, body.getStatus());
        }
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<CarServiceCouponUser> queryAll(String userId) {
        QueryWrapper<CarServiceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCouponUser.USER_ID, userId);
        return queryAll(wrapper);
    }


}
