package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.click.carservice.db.domain.CarServiceInsuranceServiceOrder;
import org.click.carservice.db.mapper.CarServiceInsuranceServiceOrderMapper;
import org.click.carservice.db.service.ICarServiceInsuranceServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 保险服务项-订单Service业务层处理
 * 
 * @author huangYan
 * @date 2023-08-07
 */
@Service
public class CarServiceInsuranceServiceOrderServiceImpl extends ServiceImpl<CarServiceInsuranceServiceOrderMapper, CarServiceInsuranceServiceOrder> implements ICarServiceInsuranceServiceOrderService
{
    @Autowired
    private CarServiceInsuranceServiceOrderMapper carServiceInsuranceServiceOrderMapper;
    /**
     * 查询保险服务项-订单
     * 
     * @param id 保险服务项-订单主键
     * @return 保险服务项-订单
     */
    @Override
    public CarServiceInsuranceServiceOrder selectCarServiceInsuranceServiceOrderById(String id)
    {
        CarServiceInsuranceServiceOrder carServiceInsuranceServiceOrder = this.getBaseMapper().selectById(id);
        return carServiceInsuranceServiceOrder;
    }

    /**
     * 查询保险服务项-订单列表
     * 
     * @param carServiceInsuranceServiceOrder 保险服务项-订单
     * @return 保险服务项-订单
     */
    @Override
    public List<CarServiceInsuranceServiceOrder> selectCarServiceInsuranceServiceOrderList(CarServiceInsuranceServiceOrder carServiceInsuranceServiceOrder) {
        LambdaQueryWrapper<CarServiceInsuranceServiceOrder> lqw = Wrappers.lambdaQuery();
            if (StringUtils.isNotBlank(carServiceInsuranceServiceOrder.getInsuranceServiceId())){
            lqw.eq(CarServiceInsuranceServiceOrder::getInsuranceServiceId ,carServiceInsuranceServiceOrder.getInsuranceServiceId());
        }
            if (StringUtils.isNotBlank(carServiceInsuranceServiceOrder.getOrderId())){
            lqw.eq(CarServiceInsuranceServiceOrder::getOrderId ,carServiceInsuranceServiceOrder.getOrderId());
        }
        return this.list(lqw);
    }

    /**
     * 新增保险服务项-订单
     * 
     * @param carServiceInsuranceServiceOrder 保险服务项-订单
     * @return 结果
     */
    @Override
    public int insertCarServiceInsuranceServiceOrder(CarServiceInsuranceServiceOrder carServiceInsuranceServiceOrder)
    {
        return this.getBaseMapper().insert(carServiceInsuranceServiceOrder);
    }

    /**
     * 修改保险服务项-订单
     * 
     * @param carServiceInsuranceServiceOrder 保险服务项-订单
     * @return 结果
     */
    @Override
    public int updateCarServiceInsuranceServiceOrder(CarServiceInsuranceServiceOrder carServiceInsuranceServiceOrder)
    {
        return this.getBaseMapper().updateById(carServiceInsuranceServiceOrder);
    }

    /**
     * 批量删除保险服务项-订单
     * 
     * @param ids 需要删除的保险服务项-订单主键
     * @return 结果
     */
    @Override
    public int deleteCarServiceInsuranceServiceOrderByIds(List<String> ids)
    {
        return this.getBaseMapper().deleteBatchIds(ids);
    }

    /**
     * 删除保险服务项-订单信息
     * 
     * @param id 保险服务项-订单主键
     * @return 结果
     */
    @Override
    public int deleteCarServiceInsuranceServiceOrderById(String id)
    {
        return this.getBaseMapper().deleteById(id);
    }
}
