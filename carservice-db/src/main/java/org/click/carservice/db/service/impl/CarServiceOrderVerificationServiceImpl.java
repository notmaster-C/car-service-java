package org.click.carservice.db.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.click.carservice.db.domain.CarServiceOrderVerification;
import org.click.carservice.db.domain.dto.OrderVerificationExportDto;
import org.click.carservice.db.domain.query.OrderVerificationQuery;
import org.click.carservice.db.mapper.CarServiceOrderVerificationMapper;
import org.click.carservice.db.service.ICarServiceOrderVerificationService;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CarServiceOrderVerificationMapper orderVerificationMapper;

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

    /**
     * 查询核销列表
     *
     * @param carServiceOrderVerification 核销
     * @return 核销
     */
    @Override
    public List<CarServiceOrderVerification> selectCarServiceOrderVerificationList(CarServiceOrderVerification carServiceOrderVerification) {
        PageHelper.startPage(carServiceOrderVerification.getPage(), carServiceOrderVerification.getLimit());
        LambdaQueryWrapper<CarServiceOrderVerification> lqw = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(carServiceOrderVerification.getUserId())){
            lqw.eq(CarServiceOrderVerification::getUserId ,carServiceOrderVerification.getUserId());
        }
        if (StrUtil.isNotBlank(carServiceOrderVerification.getGoodsId())){
            lqw.eq(CarServiceOrderVerification::getGoodsId ,carServiceOrderVerification.getGoodsId());
        }
        if (StrUtil.isNotBlank(carServiceOrderVerification.getOrderSn())){
            lqw.eq(CarServiceOrderVerification::getOrderSn ,carServiceOrderVerification.getOrderSn());
        }
        if (StrUtil.isNotBlank(carServiceOrderVerification.getStorageId())){
            lqw.eq(CarServiceOrderVerification::getStorageId ,carServiceOrderVerification.getStorageId());
        }
        if (StrUtil.isNotBlank(carServiceOrderVerification.getToubaoSn())){
            lqw.eq(CarServiceOrderVerification::getToubaoSn ,carServiceOrderVerification.getToubaoSn());
        }
        if (StrUtil.isNotBlank(carServiceOrderVerification.getAddress())){
            lqw.eq(CarServiceOrderVerification::getAddress ,carServiceOrderVerification.getAddress());
        }
        if (carServiceOrderVerification.getVerificationTime() != null){
            lqw.eq(CarServiceOrderVerification::getVerificationTime ,carServiceOrderVerification.getVerificationTime());
        }
        if (carServiceOrderVerification.getAddTime() != null){
            lqw.eq(CarServiceOrderVerification::getAddTime ,carServiceOrderVerification.getAddTime());
        }
        if (carServiceOrderVerification.getDeleted() != null){
            lqw.eq(CarServiceOrderVerification::getDeleted ,carServiceOrderVerification.getDeleted());
        }
        if (StrUtil.isNotBlank(carServiceOrderVerification.getTenantId())){
            lqw.eq(CarServiceOrderVerification::getTenantId ,carServiceOrderVerification.getTenantId());
        }
        if (carServiceOrderVerification.getVersion() != null){
            lqw.eq(CarServiceOrderVerification::getVersion ,carServiceOrderVerification.getVersion());
        }
        return this.list(lqw);
    }

    @Override
    public List<OrderVerificationExportDto> exportOrderVerification(OrderVerificationQuery query) {
        List<OrderVerificationExportDto> dto = orderVerificationMapper.exportOrderVerification(query);
        return dto;
    }
}
