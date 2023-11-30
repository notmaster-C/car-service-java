package org.click.carservice.wx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.db.service.impl.CarServiceCarServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 车牌业务
 */
@Service
@CacheConfig(cacheNames = "car_service_car")
public class WxCarService extends CarServiceCarServiceImpl {

    /**
     * 用户id查询用户所拥有的车辆拍照信息
     *
     * @param userId
     * @return
     */
//    //@Cacheable(sync = true)
    public List<CarServiceCar> queryByUid(String userId) {
        QueryWrapper<CarServiceCar> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCar.USER_ID, userId);
        return queryAll(wrapper);
    }

    /**
     * 车牌查信息
     *
     * @param CarNumber
     * @return
     */
//    //@Cacheable(sync = true)
    public CarServiceCar queryByCarNumber(String CarNumber) {
        QueryWrapper<CarServiceCar> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCar.CAR_NUMBER, CarNumber);
        wrapper.eq(CarServiceCar.DELETED, 0);
        return getOne(wrapper, false);
    }

    //    @CacheEvict(allEntries = true)
    public void resetDefault(String userId) {
        CarServiceCar car = new CarServiceCar();
        car.setIsDefault(0);
        car.setUpdateTime(LocalDateTime.now());
        QueryWrapper<CarServiceCar> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCar.USER_ID, userId);
        update(car, wrapper);
    }

    public CarServiceCar query(String userId, String id) {
        QueryWrapper<CarServiceCar> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCar.USER_ID, userId);
        wrapper.eq(CarServiceCar.ID, id);
        return getOne(wrapper);
    }

    public CarServiceCar query(String userId) {
        QueryWrapper<CarServiceCar> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCar.USER_ID, userId);
        wrapper.eq(CarServiceCar.IS_DEFAULT, 1);
        return getOne(wrapper);
    }

    /**
     * 删除车辆信息
     *
     * @param userId 用户ID
     * @param id     车辆ID
     */
    public void delete(String userId, String id) {
        QueryWrapper<CarServiceCar> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCar.USER_ID, userId);
        wrapper.eq(CarServiceCar.ID, id);
        remove(wrapper);
    }

}
