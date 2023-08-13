package org.click.carservice.db.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.click.carservice.db.domain.CarServiceInsuranceInfo;
import org.click.carservice.db.domain.CarServiceInsuranceService;
import org.click.carservice.db.entity.car.CarServiceInsuranceInfoParam;
import org.click.carservice.db.mapper.CarServiceInsuranceInfoMapper;
import org.click.carservice.db.service.ICarServiceInsuranceInfoService;
import org.click.carservice.db.service.ICarServiceInsuranceServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 保单信息Service业务层处理
 * 
 * @author huangYan
 * @date 2023-08-04
 */
@Service
public class CarServiceInsuranceInfoServiceImpl extends ServiceImpl<CarServiceInsuranceInfoMapper, CarServiceInsuranceInfo> implements ICarServiceInsuranceInfoService {

    @Autowired
    private CarServiceInsuranceInfoMapper carServiceInsuranceInfoMapper;

    @Resource
    @Qualifier("carServiceInsuranceServiceServiceImpl")
    private ICarServiceInsuranceServiceService carServiceInsuranceServiceService;
    /**
     * 查询保单信息
     *
     * @param id 保单信息主键
     * @return 保单信息
     */
    @Override
    public CarServiceInsuranceInfo selectCarServiceInsuranceInfoById(String id)
    {
        CarServiceInsuranceInfo carServiceInsuranceInfo = this.getBaseMapper().selectById(id);
        List<CarServiceInsuranceService> CarServiceInsuranceServiceList = carServiceInsuranceServiceService.getBaseMapper().selectList(Wrappers.<CarServiceInsuranceService>lambdaQuery().eq(CarServiceInsuranceService::getInsuranceInfoId, id));
        carServiceInsuranceInfo.setCarServiceInsuranceServiceList(CollectionUtil.isEmpty(CarServiceInsuranceServiceList) ? new ArrayList<>() : CarServiceInsuranceServiceList);
        return carServiceInsuranceInfo;
    }

    /**
     * 查询保单信息列表
     *
     * @param carServiceInsuranceInfo 保单信息
     * @return 保单信息
     */
    @Override
    public List<CarServiceInsuranceInfo> selectCarServiceInsuranceInfoList(CarServiceInsuranceInfoParam carServiceInsuranceInfo) {
        LambdaQueryWrapper<CarServiceInsuranceInfo> lqw = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(carServiceInsuranceInfo.getUserId())){
            lqw.eq(CarServiceInsuranceInfo::getUserId ,carServiceInsuranceInfo.getUserId());
        }
        if (StrUtil.isNotBlank(carServiceInsuranceInfo.getCarId())){
            lqw.eq(CarServiceInsuranceInfo::getCarId ,carServiceInsuranceInfo.getCarId());
        }
        if (StrUtil.isNotBlank(carServiceInsuranceInfo.getInsureUser())){
            lqw.eq(CarServiceInsuranceInfo::getInsureUser ,carServiceInsuranceInfo.getInsureUser());
        }
        if (StrUtil.isNotBlank(carServiceInsuranceInfo.getInsureUserPhone())){
            lqw.eq(CarServiceInsuranceInfo::getInsureUserPhone ,carServiceInsuranceInfo.getInsureUserPhone());
        }
        if (StrUtil.isNotBlank(carServiceInsuranceInfo.getInsureNum())){
            lqw.eq(CarServiceInsuranceInfo::getInsureNum ,carServiceInsuranceInfo.getInsureNum());
        }
        if (StrUtil.isNotBlank(carServiceInsuranceInfo.getInsureCompany())){
            lqw.eq(CarServiceInsuranceInfo::getInsureCompany ,carServiceInsuranceInfo.getInsureCompany());
        }
        if (carServiceInsuranceInfo.getInsureTime() != null){
            lqw.eq(CarServiceInsuranceInfo::getInsureTime ,carServiceInsuranceInfo.getInsureTime());
        }
        if (carServiceInsuranceInfo.getInsureEndTime() != null){
            lqw.eq(CarServiceInsuranceInfo::getInsureEndTime ,carServiceInsuranceInfo.getInsureEndTime());
        }
        return carServiceInsuranceInfoMapper.selectCarServiceInsuranceInfoList(lqw, carServiceInsuranceInfo);
    }

    /**
     * 新增保单信息
     *
     * @param carServiceInsuranceInfo 保单信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertCarServiceInsuranceInfo(CarServiceInsuranceInfo carServiceInsuranceInfo)
    {
        int rows = this.getBaseMapper().insert(carServiceInsuranceInfo);
        insertCarServiceInsuranceService(carServiceInsuranceInfo);
        return rows;
    }

    /**
     * 修改保单信息
     *
     * @param carServiceInsuranceInfo 保单信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateCarServiceInsuranceInfo(CarServiceInsuranceInfo carServiceInsuranceInfo)
    {
        carServiceInsuranceInfo.setUpdateTime(new Date());
        return this.getBaseMapper().updateById(carServiceInsuranceInfo);
    }

    /**
     * 新增保险信息服务项信息
     *
     * @param carServiceInsuranceInfo 保单信息对象
     */
    public void insertCarServiceInsuranceService(CarServiceInsuranceInfo carServiceInsuranceInfo)
    {
        List<CarServiceInsuranceService> carServiceInsuranceServiceList = carServiceInsuranceInfo.getCarServiceInsuranceServiceList();
        String id = carServiceInsuranceInfo.getId();
        if (CollectionUtil.isNotEmpty(carServiceInsuranceServiceList))
        {
            List<CarServiceInsuranceService> list = new ArrayList<CarServiceInsuranceService>();
            for (CarServiceInsuranceService carServiceInsuranceService : carServiceInsuranceServiceList)
            {
                // 子表中设置父表id
                carServiceInsuranceService.setInsuranceInfoId(id);
                list.add(carServiceInsuranceService);
            }
            if (list.size() > 0)
            {
                carServiceInsuranceServiceService.saveBatch(list);
            }
        }
    }
}
