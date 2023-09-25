package org.click.carservice.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.db.entity.car.UserCarBody;
import org.click.carservice.db.service.impl.CarServiceCarServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "car_service_car")
public class AdminCarService extends CarServiceCarServiceImpl {

    public Object queryByUid(String userId) {
        LambdaQueryWrapper<CarServiceCar> queryWrapper = Wrappers.lambdaQuery(CarServiceCar.class).eq(CarServiceCar::getUserId, userId).orderByDesc(CarServiceCar::getAddTime);
        return ResponseUtil.ok(list(queryWrapper));
    }

    //@Cacheable(sync = true)
    public List<CarServiceCar> querySelective(UserCarBody body) {
        QueryWrapper<CarServiceCar> wrapper = startPage(body);
        if (body.getUserId() != null) {
            wrapper.eq(CarServiceCar.USER_ID, body.getUserId());
        }
        if (body.getCarType() != null) {
            wrapper.eq(CarServiceCar.CAR_TYPE, body.getCarType());
        }
        if (body.getEngineType() != null) {
            wrapper.eq(CarServiceCar.ENGINE_TYPE, body.getEngineType());
        }
        if (body.getCarAge() != null) {
            wrapper.eq(CarServiceCar.CAR_AGE, body.getCarAge());
        }
        if (body.getCarModel() != null) {
            wrapper.eq(CarServiceCar.CAR_MODEL, body.getCarModel());
        }
        if (body.getCarProperties() != null) {
            wrapper.eq(CarServiceCar.CAR_PROPERTIES, body.getCarProperties());
        }
        return queryAll(wrapper);
    }

}
