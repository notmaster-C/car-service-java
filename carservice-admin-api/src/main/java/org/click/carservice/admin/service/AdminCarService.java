package org.click.carservice.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.db.entity.car.UserCarBody;
import org.click.carservice.db.entity.car.UserCarResult;
import org.click.carservice.db.mapper.CarServiceCarMapper;
import org.click.carservice.db.service.impl.CarServiceCarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "car_service_car")
public class AdminCarService extends CarServiceCarServiceImpl {

    @Autowired
    private CarServiceCarMapper carServiceCarMapper;

    public List<UserCarResult> selectUserCarPage(UserCarBody body) {
        QueryWrapper<CarServiceCar> queryWrapper = startPage(body);
        List<UserCarResult> list = carServiceCarMapper.selectUserCarPage(body, queryWrapper);
        return list;
    }
}
