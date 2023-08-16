package org.click.carservice.wx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.db.service.impl.CarServiceCarServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 车牌业务
 */
@Service
@CacheConfig(cacheNames = "car_service_car")
public class WxCarService extends CarServiceCarServiceImpl {

    /**
     * 用户id查询用户所拥有的车辆拍照信息
     * @param userId
     * @return
     */
//    @Cacheable(sync = true)
    public List<CarServiceCar> queryByUid(String userId) {
        LambdaQueryWrapper<CarServiceCar> queryWrapper = Wrappers.lambdaQuery(CarServiceCar.class).eq(CarServiceCar::getUserId, userId).orderByDesc(CarServiceCar::getAddTime);
        return list(queryWrapper);
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
        List<CarServiceCar> carServiceCars = queryByUid(userId);
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
     * 修改
     * @param userId
     * @param carServiceCar
     */
    public void edit(String userId, CarServiceCar carServiceCar) {
        updateById(carServiceCar);
    }

    /**
     * id查询详情
     * @param userId
     * @param id
     * @return
     */
    public CarServiceCar detail(String userId, String id) {
        return selectCarServiceCarById(id);
    }
}
