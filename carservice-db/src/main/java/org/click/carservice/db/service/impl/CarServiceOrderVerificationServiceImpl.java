package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.click.carservice.db.domain.CarServiceOrderVerification;
import org.click.carservice.db.mapper.CarServiceOrderVerificationMapper;
import org.click.carservice.db.service.ICarServiceOrderVerificationService;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author click
 * @since 2023-08-13
 */
@Service
public class CarServiceOrderVerificationServiceImpl extends ServiceImpl<CarServiceOrderVerificationMapper, CarServiceOrderVerification> implements ICarServiceOrderVerificationService {

    @Override
    public CarServiceOrderVerification findById(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    public List<CarServiceOrderVerification> queryAll(Wrapper<CarServiceOrderVerification> queryWrapper) {
        return  getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public boolean exists(Wrapper<CarServiceOrderVerification> queryWrapper) {
        return  getBaseMapper().exists(queryWrapper);
    }

    @Override
    public int add(CarServiceOrderVerification record) {
        return  getBaseMapper().insert(record);
    }

    @Override
    public boolean batchAdd(List<CarServiceOrderVerification> list) {
        return  saveBatch(list);
    }

    @Override
    public int updateSelective(CarServiceOrderVerification record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    public int updateVersionSelective(CarServiceOrderVerification record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    public int deleteById(String id) {
        return getBaseMapper().deleteById(id);
    }
}
