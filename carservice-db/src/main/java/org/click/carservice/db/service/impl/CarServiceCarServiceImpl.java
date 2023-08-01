package org.click.carservice.db.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.db.mapper.CarServiceCarMapper;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户车牌信息表 服务实现类
 * </p>
 *
 * @author click
 * @since 2023-08-01
 */
@Service
public class CarServiceCarServiceImpl extends IBaseServiceImpl<CarServiceCarMapper, CarServiceCar> {

    /**
     * 查询用户车牌信息
     *
     * @param id 用户车牌信息主键
     * @return 用户车牌信息
     */
    public CarServiceCar selectCarServiceCarById(Long id)
    {
        CarServiceCar carServiceCar = this.getBaseMapper().selectById(id);
        return carServiceCar;
    }

    /**
     * 查询用户车牌信息列表
     *
     * @param carServiceCar 用户车牌信息
     * @return 用户车牌信息
     */
    public List<CarServiceCar> selectCarServiceCarList(CarServiceCar carServiceCar) {
        LambdaQueryWrapper<CarServiceCar> lqw = Wrappers.lambdaQuery();
        if (carServiceCar.getUserId() != null){
            lqw.eq(CarServiceCar::getUserId ,carServiceCar.getUserId());
        }
        if (StrUtil.isNotBlank(carServiceCar.getCarNumber())){
            lqw.eq(CarServiceCar::getCarNumber ,carServiceCar.getCarNumber());
        }
        if (carServiceCar.getIsDefault() != null){
            lqw.eq(CarServiceCar::getIsDefault ,carServiceCar.getIsDefault());
        }
        if (carServiceCar.getAddTime() != null){
            lqw.eq(CarServiceCar::getAddTime ,carServiceCar.getAddTime());
        }
        if (carServiceCar.getDeleted() != null){
            lqw.eq(CarServiceCar::getDeleted ,carServiceCar.getDeleted());
        }
        if (StrUtil.isNotBlank(carServiceCar.getTenantId())){
            lqw.eq(CarServiceCar::getTenantId ,carServiceCar.getTenantId());
        }
        if (carServiceCar.getVersion() != null){
            lqw.eq(CarServiceCar::getVersion ,carServiceCar.getVersion());
        }
        return this.list(lqw);
    }

    /**
     * 新增
     * @param carServiceCar
     */
    public void insertCarServiceCar(CarServiceCar carServiceCar) {
        String carNumber = carServiceCar.getCarNumber();
        List<CarServiceCar> carServiceCars = selectCarServiceCarList(new CarServiceCar().setCarNumber(carNumber));
        if (CollUtil.isNotEmpty(carServiceCars)){
            throw new RuntimeException("新增失败：系统已存在此牌照-" + carNumber);
        }
        this.getBaseMapper().insert(carServiceCar);
    }

    /**
     * 修改用户车牌信息
     *
     * @param carServiceCar 用户车牌信息
     * @return 结果
     */
    public int updateCarServiceCar(CarServiceCar carServiceCar)
    {
        String carNumber = carServiceCar.getCarNumber();
        List<CarServiceCar> carServiceCars = selectCarServiceCarList(new CarServiceCar().setCarNumber(carNumber));
        if (CollUtil.isNotEmpty(carServiceCars)){
            throw new RuntimeException("修改失败：系统已存在此牌照-" + carNumber);
        }
        carServiceCar.setUpdateTime(new Date());
        return this.getBaseMapper().updateById(carServiceCar);
    }

    /**
     * 删除用户车牌信息信息
     *
     * @param userId 用户id
     * @param id 用户车牌信息主键
     * @return 结果
     */
    public int deleteCarServiceCarById(String userId, String id)
    {
        LambdaUpdateWrapper<CarServiceCar> updateWrapper = Wrappers.lambdaUpdate(CarServiceCar.class)
                .set(CarServiceCar::getDeleted, "1" )
                .eq(CarServiceCar::getUserId, userId)
                .eq(CarServiceCar::getId, id);
        return update(updateWrapper) ? 1 : 0;
    }

}
