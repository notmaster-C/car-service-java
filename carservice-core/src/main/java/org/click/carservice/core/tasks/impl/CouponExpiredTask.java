package org.click.carservice.core.tasks.impl;
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

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.tasks.service.TaskRunnable;
import org.click.carservice.core.utils.BeanUtil;
import org.click.carservice.db.domain.CarServiceCoupon;
import org.click.carservice.db.enums.CouponStatus;
import org.click.carservice.db.service.ICouponService;

/**
 * 优惠券过期队列
 * @author click
 */
@Slf4j
public class CouponExpiredTask extends TaskRunnable {

    /**优惠券ID*/
    private final String couponId;
    /**id前缀*/
    private static final String idPrefix = "CouponExpiredTask-";
    /**任务名称*/
    private static final String taskName = "优惠券过期";


    public CouponExpiredTask(CarServiceCoupon coupon, long delayInMilliseconds) {
        super(idPrefix + coupon.getId(), delayInMilliseconds, coupon.getTenantId(), taskName);
        this.couponId = coupon.getId();
    }

    @Override
    public void runTask() {
        ICouponService couponService = BeanUtil.getBean(ICouponService.class);
        CarServiceCoupon coupon = couponService.findById(this.couponId);
        if (coupon == null || !CouponStatus.STATUS_NORMAL.equals(coupon.getStatus())) {
            return;
        }
        coupon.setStatus(CouponStatus.STATUS_EXPIRED.getStatus());
        if (couponService.updateVersionSelective(coupon) == 0) {
            throw new RuntimeException("优惠券过期设置失败");
        }
    }
}
