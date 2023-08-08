package org.click.carservice.db.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.click.carservice.db.domain.CarServiceInsuranceService;
import org.click.carservice.db.domain.CarServiceInsuranceServiceOrder;
import org.click.carservice.db.mapper.CarServiceInsuranceServiceMapper;
import org.click.carservice.db.service.ICarServiceInsuranceServiceOrderService;
import org.click.carservice.db.service.ICarServiceInsuranceServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 保险信息服务项Service业务层处理
 * 
 * @author huangYan
 * @date 2023-08-07
 */
@Service
public class CarServiceInsuranceServiceServiceImpl extends ServiceImpl<CarServiceInsuranceServiceMapper, CarServiceInsuranceService> implements ICarServiceInsuranceServiceService
{
    @Autowired
    private CarServiceInsuranceServiceMapper carServiceInsuranceServiceMapper;
    @Autowired
    private ICarServiceInsuranceServiceOrderService carServiceInsuranceServiceOrderService;
    /**
     * 查询保险信息服务项
     *
     * @param id 保险信息服务项主键
     * @return 保险信息服务项
     */
    @Override
    public CarServiceInsuranceService selectCarServiceInsuranceServiceById(String id)
    {
        CarServiceInsuranceService carServiceInsuranceService = this.getBaseMapper().selectById(id);
        List<CarServiceInsuranceServiceOrder> CarServiceInsuranceServiceOrderList = carServiceInsuranceServiceOrderService.getBaseMapper().selectList(Wrappers.<CarServiceInsuranceServiceOrder>lambdaQuery().eq(CarServiceInsuranceServiceOrder::getInsuranceServiceId, id));
        carServiceInsuranceService.setCarServiceInsuranceServiceOrderList(CollectionUtil.isEmpty(CarServiceInsuranceServiceOrderList) ? new ArrayList<>() : CarServiceInsuranceServiceOrderList);
        return carServiceInsuranceService;
    }

    /**
     * 查询保险信息服务项列表
     *
     * @param carServiceInsuranceService 保险信息服务项
     * @return 保险信息服务项
     */
    @Override
    public List<CarServiceInsuranceService> selectCarServiceInsuranceServiceList(CarServiceInsuranceService carServiceInsuranceService) {
        LambdaQueryWrapper<CarServiceInsuranceService> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(carServiceInsuranceService.getInsuranceInfoId())){
            lqw.eq(CarServiceInsuranceService::getInsuranceInfoId ,carServiceInsuranceService.getInsuranceInfoId());
        }
        if (StringUtils.isNotBlank(carServiceInsuranceService.getServiceName())){
            lqw.like(CarServiceInsuranceService::getServiceName ,carServiceInsuranceService.getServiceName());
        }
        if (StringUtils.isNotBlank(carServiceInsuranceService.getServiceCode())){
            lqw.eq(CarServiceInsuranceService::getServiceCode ,carServiceInsuranceService.getServiceCode());
        }
        if (carServiceInsuranceService.getServiceTotal() != null){
            lqw.eq(CarServiceInsuranceService::getServiceTotal ,carServiceInsuranceService.getServiceTotal());
        }
        if (carServiceInsuranceService.getServiceUsed() != null){
            lqw.eq(CarServiceInsuranceService::getServiceUsed ,carServiceInsuranceService.getServiceUsed());
        }
        if (carServiceInsuranceService.getAddTime() != null){
            lqw.eq(CarServiceInsuranceService::getAddTime ,carServiceInsuranceService.getAddTime());
        }
        if (carServiceInsuranceService.getDeleted() != null){
            lqw.eq(CarServiceInsuranceService::getDeleted ,carServiceInsuranceService.getDeleted());
        }
        if (StringUtils.isNotBlank(carServiceInsuranceService.getTenantId())){
            lqw.eq(CarServiceInsuranceService::getTenantId ,carServiceInsuranceService.getTenantId());
        }
        if (carServiceInsuranceService.getVersion() != null){
            lqw.eq(CarServiceInsuranceService::getVersion ,carServiceInsuranceService.getVersion());
        }
        return this.list(lqw);
    }

    /**
     * 新增保险信息服务项
     *
     * @param carServiceInsuranceService 保险信息服务项
     * @return 结果
     */
    @Transactional
    @Override
    public int insertCarServiceInsuranceService(CarServiceInsuranceService carServiceInsuranceService)
    {
        int rows = this.getBaseMapper().insert(carServiceInsuranceService);
        insertCarServiceInsuranceServiceOrder(carServiceInsuranceService);
        return rows;
    }

    /**
     * 修改保险信息服务项
     *
     * @param carServiceInsuranceService 保险信息服务项
     * @return 结果
     */
    @Transactional
    @Override
    public int updateCarServiceInsuranceService(CarServiceInsuranceService carServiceInsuranceService)
    {
        carServiceInsuranceService.setUpdateTime(new Date());
        // 删除子表数据
        carServiceInsuranceServiceOrderService.getBaseMapper().delete(Wrappers.<CarServiceInsuranceServiceOrder>lambdaQuery().eq(CarServiceInsuranceServiceOrder::getInsuranceServiceId, carServiceInsuranceService.getId()));
        // 新增子表数据
        insertCarServiceInsuranceServiceOrder(carServiceInsuranceService);
        return this.getBaseMapper().updateById(carServiceInsuranceService);
    }


    /**
     * 新增保险服务项-订单信息
     *
     * @param carServiceInsuranceService 保险信息服务项对象
     */
    public void insertCarServiceInsuranceServiceOrder(CarServiceInsuranceService carServiceInsuranceService)
    {
        List<CarServiceInsuranceServiceOrder> carServiceInsuranceServiceOrderList = carServiceInsuranceService.getCarServiceInsuranceServiceOrderList();
        String id = carServiceInsuranceService.getId();
        if (CollectionUtil.isNotEmpty(carServiceInsuranceServiceOrderList))
        {
            List<CarServiceInsuranceServiceOrder> list = new ArrayList<CarServiceInsuranceServiceOrder>();
            for (CarServiceInsuranceServiceOrder carServiceInsuranceServiceOrder : carServiceInsuranceServiceOrderList)
            {
                // 子表中设置父表id
                carServiceInsuranceServiceOrder.setInsuranceServiceId(id);
                list.add(carServiceInsuranceServiceOrder);
            }
            if (list.size() > 0)
            {
                carServiceInsuranceServiceOrderService.saveBatch(list);
            }
        }
    }
}
