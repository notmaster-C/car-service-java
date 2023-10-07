package org.click.carservice.wx.web.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.db.service.impl.CarServiceCarServiceImpl;
import org.click.carservice.wx.service.WxCarService;
import org.click.carservice.wx.service.WxTenantService;
import org.click.carservice.wx.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 车牌业务-小程序端
 */
@Slf4j
@Service
public class WxWebCarService extends CarServiceCarServiceImpl {
    @Autowired
    private WxCarService carService;
    @Autowired
    private WxTenantService tenantService;
    @Autowired
    private WxUserService userService;
    /**
     * 添加或更新收车辆信息
     *
     * @param userId  用户ID
     * @param car 车辆信息
     * @return 添加或更新操作结果
     */
    public Object save(String userId, CarServiceCar car) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }
        Object error =validate(car);
        if (error != null) {
            return error;
        }
        if (car.getId() == null || "0".equals(car.getId())) {
            if(carService.queryByCarNumber(car.getCarNumber()) != null){
                return ResponseUtil.fail("车牌已存在!");
            }
            if (car.getIsDefault()==1) {
                carService.resetDefault(userId);
            }
            car.setId(null);
            car.setUserId(userId);
            carService.add(car);
        } else {
            CarServiceCar car_t = carService.query(userId, car.getId());
            if (car_t == null) {
                return ResponseUtil.badArgumentValue();
            }
            if (car.getIsDefault()==1) {
                carService.resetDefault(userId);
            }
            if (carService.updateSelective(car) <= 0){
                throw new RuntimeException("网络繁忙，请刷新重试");
            }
        }
        return ResponseUtil.ok(car.getId());
    }
    private Object validate(CarServiceCar car) {
        if (!StringUtils.hasText(car.getCarNumber())) {
            return ResponseUtil.badArgument();
        }
        if (Objects.isNull(car.getCarProperties())) {
            return ResponseUtil.fail("车辆性质必填!");
        }
        if (Objects.isNull(car.getIsDefault())) {
            return ResponseUtil.badArgument();
        }
        return null;

    }
    /**
     * 车辆信息详情
     *
     * @param userId 用户ID
     * @param id     车辆信息ID
     * @return 车辆信息详情
     */
    public Object detail( String userId, String id) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }
        CarServiceCar car = carService.query(userId, id);
        if (car == null) {
            return ResponseUtil.fail("车辆信息获取失败");
        }
        return ResponseUtil.ok(car);
    }
    /**
     * 车辆信息列表
     *
     * @param userId 用户ID
     * @return 车辆信息详情
     */
    public Object list(String userId) {
        return ResponseUtil.okList(carService.queryByUid(userId));
    }
    /**
     * 设置默认车牌
     * @param userId
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer setDefaultCar(String userId, String id) {
        // 获取设置的默认拍照
        List<CarServiceCar> carServiceCars = carService.queryByUid(userId);
        Optional<CarServiceCar> defaultCar = carServiceCars.stream().filter(c -> Integer.valueOf(1).compareTo(c.getIsDefault()) == 0).findFirst();
        // 存在则删除默认状态
        if (defaultCar.isPresent()){
            CarServiceCar carServiceCar = defaultCar.get();
            carServiceCar.setIsDefault(0);
            updateById(carServiceCar);
        }
        // 设置默认牌照
        LambdaUpdateWrapper<CarServiceCar> updateWrapper = Wrappers.lambdaUpdate(CarServiceCar.class).set(CarServiceCar::getIsDefault, "1" )
                .eq(CarServiceCar::getUserId, userId)
                .eq(CarServiceCar::getId, id);
        return update(updateWrapper) ? 1 : 0;
    }


    /**
     * 删除车辆信息
     *
     * @param userId  用户ID
     * @param id 	  车辆ID
     */
    public Object delete(String userId, String id) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }
        carService.delete(userId,id);
        return ResponseUtil.ok();
    }

}
