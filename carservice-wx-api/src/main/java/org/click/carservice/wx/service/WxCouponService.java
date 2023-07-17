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
import org.click.carservice.db.domain.CarServiceCoupon;
import org.click.carservice.db.domain.CarServiceCouponUser;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.enums.CouponStatus;
import org.click.carservice.db.enums.CouponType;
import org.click.carservice.db.service.ICouponUserService;
import org.click.carservice.db.service.impl.CouponServiceImpl;
import org.click.carservice.wx.model.coupon.result.CouponResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 优惠券服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_coupon")
public class WxCouponService extends CouponServiceImpl {

    @Autowired
    private ICouponUserService couponUserService;


    public List<CouponResult> change(List<CarServiceCouponUser> couponList) {
        List<CouponResult> couponVoList = new ArrayList<>(couponList.size());
        for (CarServiceCouponUser couponUser : couponList) {
            String couponId = couponUser.getCouponId();
            CarServiceCoupon coupon = findById(couponId);
            CouponResult couponVo = new CouponResult();
            couponVo.setId(couponUser.getId());
            couponVo.setCid(coupon.getId());
            couponVo.setName(coupon.getName());
            couponVo.setDesc(coupon.getDepict());
            couponVo.setTag(coupon.getTag());
            couponVo.setMin(coupon.getMin());
            couponVo.setDiscount(coupon.getDiscount());
            couponVo.setStartTime(couponUser.getStartTime());
            couponVo.setEndTime(couponUser.getEndTime());
            couponVoList.add(couponVo);
        }
        return couponVoList;
    }

    /**
     * 查询
     */
    @Cacheable(sync = true)
    public List<CarServiceCoupon> queryList(PageBody body) {
        QueryWrapper<CarServiceCoupon> wrapper = startPage(body);
        wrapper.eq(CarServiceCoupon.TYPE, CouponType.TYPE_COMMON.getStatus());
        wrapper.eq(CarServiceCoupon.STATUS, CouponStatus.STATUS_NORMAL.getStatus());
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<CarServiceCoupon> queryAvailableList(String userId, Integer limit) {
        //分页
        PageBody body = new PageBody(limit);
        if (userId == null) {
            return queryList(body);
        }

        QueryWrapper<CarServiceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCouponUser.USER_ID, userId);
        List<CarServiceCouponUser> userList = couponUserService.queryAll(wrapper);

        //查询优惠券
        QueryWrapper<CarServiceCoupon> startPage = startPage(body);
        // 过滤掉登录账号已经领取过的coupon
        if (userList != null && !userList.isEmpty()) {
            startPage.notIn(CarServiceCoupon.ID, userList.stream().map(CarServiceCouponUser::getCouponId).collect(Collectors.toList()));
        }
        //查询条件
        startPage.eq(CarServiceCoupon.TYPE, CouponType.TYPE_COMMON.getStatus());
        startPage.eq(CarServiceCoupon.STATUS, CouponStatus.STATUS_NORMAL.getStatus());
        return queryAll(startPage);
    }


    @Cacheable(sync = true)
    public CarServiceCoupon findByCode(String code) {
        QueryWrapper<CarServiceCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCoupon.CODE, code);
        wrapper.eq(CarServiceCoupon.TYPE, CouponType.TYPE_COMMON.getStatus());
        wrapper.eq(CarServiceCoupon.STATUS, CouponStatus.STATUS_NORMAL.getStatus());
        List<CarServiceCoupon> couponList = queryAll(wrapper);
        if (couponList.size() > 1) {
            return null;
        } else if (couponList.size() == 0) {
            return null;
        } else {
            return couponList.get(0);
        }
    }

}
