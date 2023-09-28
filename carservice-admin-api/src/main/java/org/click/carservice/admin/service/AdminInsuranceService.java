package org.click.carservice.admin.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.notify.service.NotifyMobileService;
import org.click.carservice.db.domain.CarServiceCouponUser;
import org.click.carservice.db.domain.CarServiceInsuranceInfo;
import org.click.carservice.db.domain.CarServiceInsuranceService;
import org.click.carservice.db.enums.ServiceCode;
import org.click.carservice.db.service.impl.CarServiceInsuranceServiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: AdminInsuranceService
 * @Author HuangYan
 * @Date: 2023/8/12 20:09
 * @Version 1.0
 * @Description: 保单服务项
 */
@Service
@Slf4j
public class AdminInsuranceService extends CarServiceInsuranceServiceServiceImpl {

    @Autowired
    private AdminCouponUserService adminCouponUserService;

    @Autowired
    private NotifyMobileService mobileService;

    /**
     * 根据保险信息新增优惠券
     * @param info
     */
    public void insertCouponByInsuranceInfo(CarServiceInsuranceInfo info) {
        CarServiceCouponUser carServiceCouponUser = new CarServiceCouponUser();
        List<CarServiceInsuranceService> serviceList = info.getCarServiceInsuranceServiceList();
        for (CarServiceInsuranceService service : serviceList) {
            // 配置指定的优惠券
            String serviceCode = service.getServiceCode();
            String couponId = ServiceCode.parseCouponId(serviceCode);
            carServiceCouponUser.setCouponId(couponId);
            carServiceCouponUser.setUserId(info.getUserId());
            carServiceCouponUser.setCarId(info.getCarId());
            carServiceCouponUser.setServiceId(service.getId());
            carServiceCouponUser.setStartTime(LocalDateTimeUtil.of(info.getInsureTime()));
            carServiceCouponUser.setEndTime(LocalDateTimeUtil.of(info.getInsureEndTime()));
            Long serviceTotal = service.getServiceTotal();
            for (Long i = 0l; i < serviceTotal; i++) {
                adminCouponUserService.save(carServiceCouponUser);
            }
        }
        // 发送短信
//        mobileService.notifySmsTemplate();
    }

}
