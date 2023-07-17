package org.click.carservice.core.tasks.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.carserviceCoupon;
import org.click.carservice.db.domain.carserviceCouponUser;
import org.click.carservice.db.domain.carserviceGrouponRules;
import org.click.carservice.db.domain.carserviceOrder;
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


    public List<carserviceOrder> queryUnconfirmed() {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.ORDER_STATUS, OrderStatus.STATUS_SHIP.getStatus());
        return orderService.queryAll(wrapper);
    }

    public List<carserviceOrder> queryComment() {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.ORDER_STATUS, OrderStatus.STATUS_AUTO_CONFIRM.getStatus());
        wrapper.gt(carserviceOrder.COMMENTS, 0);
        return orderService.queryAll(wrapper);
    }

    public List<carserviceOrder> queryUnpaid() {
        QueryWrapper<carserviceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceOrder.ORDER_STATUS, OrderStatus.STATUS_CREATE.getStatus());
        return orderService.queryAll(wrapper);
    }

    public List<carserviceGrouponRules> queryGrouponRulesExpired() {
        QueryWrapper<carserviceGrouponRules> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGrouponRules.STATUS, GrouponRuleStatus.RULE_STATUS_ON.getStatus());
        return grouponRulesService.queryAll(wrapper);
    }

    /**
     * 查询过期的优惠券:
     * 注意：如果timeType=0, 即基于领取时间有效期的优惠券，则优惠券不会过期
     */
    public List<carserviceCoupon> queryCouponExpired() {
        QueryWrapper<carserviceCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCoupon.TIME_TYPE, CouponTimeType.TIME_TYPE_TIME);
        wrapper.eq(carserviceCoupon.STATUS, CouponStatus.STATUS_NORMAL);
        wrapper.lt(carserviceCoupon.END_TIME, LocalDateTime.now());
        return couponService.queryAll(wrapper);
    }

    public List<carserviceCouponUser> queryCouponUserExpired() {
        QueryWrapper<carserviceCouponUser> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceCouponUser.STATUS, CouponUserStatus.STATUS_USABLE);
        wrapper.lt(carserviceCouponUser.END_TIME, LocalDateTime.now());
        return couponUserService.queryAll(wrapper);
    }

}
