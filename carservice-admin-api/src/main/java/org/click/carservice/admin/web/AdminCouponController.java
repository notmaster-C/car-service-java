package org.click.carservice.admin.web;
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

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.admin.annotation.RequiresPermissionsDesc;
import org.click.carservice.admin.model.coupon.body.CouponListBody;
import org.click.carservice.admin.model.coupon.body.CouponUserListBody;
import org.click.carservice.admin.service.AdminCouponService;
import org.click.carservice.admin.service.AdminCouponUserService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceCoupon;
import org.click.carservice.db.enums.CouponType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * 优惠券管理
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/coupon")
@Validated
public class AdminCouponController {

    @Autowired
    private AdminCouponService couponService;
    @Autowired
    private AdminCouponUserService couponUserService;


    /**
     * 查询
     */
    @SaCheckPermission("admin:coupon:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(CouponListBody body) {
        return ResponseUtil.okList(couponService.querySelective(body));
    }


    /**
     * 查询用户
     */
    @SaCheckPermission("admin:coupon:join")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "查询用户")
    @GetMapping("/join")
    public Object listUser(CouponUserListBody body) {
        return ResponseUtil.okList(couponUserService.querySelective(body));
    }


    /**
     * 删除
     */
    @SaCheckPermission("admin:coupon:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@NotNull String id) {
        couponService.deleteById(id);
        return ResponseUtil.ok();
    }


    /**
     * 详情
     */
    @SaCheckPermission("admin:coupon:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull String id) {
        return ResponseUtil.ok(couponService.findById(id));
    }

    /**
     * 添加
     */
    @SaCheckPermission("admin:coupon:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody carserviceCoupon coupon) {
        Object error = couponService.validate(coupon);
        if (error != null) {
            return error;
        }
        // 如果是兑换码类型，则这里需要生存一个兑换码
        if (coupon.getType().equals(CouponType.TYPE_CODE.getStatus())) {
            coupon.setCode(couponService.generateCode());
        }
        couponService.add(coupon);
        return ResponseUtil.ok(coupon);
    }

    /**
     * 编辑
     */
    @SaCheckPermission("admin:coupon:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@Valid @RequestBody carserviceCoupon coupon) {
        Object error = couponService.validate(coupon);
        if (error != null) {
            return error;
        }
        // 如果是兑换码类型，则这里需要生存一个兑换码
        if (coupon.getType().equals(CouponType.TYPE_CODE.getStatus())) {
            coupon.setCode(couponService.generateCode());
        }
        if (couponService.updateVersionSelective(coupon) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(coupon);
    }


}
