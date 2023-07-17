package org.click.carservice.core.tasks.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.CarServiceCoupon;
import org.click.carservice.db.domain.CarServiceCouponUser;
import org.click.carservice.db.domain.CarServiceGrouponRules;
import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.db.enums.*;
import org.click.carservice.db.service.ICouponService;
import org.click.carservice.db.service.ICouponUserService;
import org.click.carservice.db.service.IGrouponRulesService;
import org.click.carservice.db.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author click
 */
@Service
public class TaskJobService {

    @Autowired
    private IGrouponRulesService grouponRulesService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ICouponService couponService;
    @Autowired
    private ICouponUserService couponUserService;


    public List<CarServiceOrder> queryUnconfirmed() {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.ORDER_STATUS, OrderStatus.STATUS_SHIP.getStatus());
        return orderService.queryAll(wrapper);
    }

    public List<CarServiceOrder> queryComment() {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.ORDER_STATUS, OrderStatus.STATUS_AUTO_CONFIRM.getStatus());
        wrapper.gt(CarServiceOrder.COMMENTS, 0);
        return orderService.queryAll(wrapper);
    }

    public List<CarServiceOrder> queryUnpaid() {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.ORDER_STATUS, OrderStatus.STATUS_CREATE.getStatus());
        return orderService.queryAll(wrapper);
    }

    public List<CarServiceGrouponRules> queryGrouponRulesExpired() {
        QueryWrapper<CarServiceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGrouponRules.STATUS, GrouponRuleStatus.RULE_STATUS_ON.getStatus());
        return grouponRulesService.queryAll(wrapper);
    }

    /**
     * 查询过期的优惠券:
     * 注意：如果timeType=0, 即基于领取时间有效期的优惠券，则优惠券不会过期
     */
    public List<CarServiceCoupon> queryCouponExpired() {
        QueryWrapper<CarServiceCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCoupon.TIME_TYPE, CouponTimeType.TIME_TYPE_TIME);
        wrapper.eq(CarServiceCoupon.STATUS, CouponStatus.STATUS_NORMAL);
        wrapper.lt(CarServiceCoupon.END_TIME, LocalDateTime.now());
        return couponService.queryAll(wrapper);
    }

    public List<CarServiceCouponUser> queryCouponUserExpired() {
        QueryWrapper<CarServiceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCouponUser.STATUS, CouponUserStatus.STATUS_USABLE);
        wrapper.lt(CarServiceCouponUser.END_TIME, LocalDateTime.now());
        return couponUserService.queryAll(wrapper);
    }

}
