package org.click.carservice.db.mapper;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.click.carservice.db.domain.CarServiceInsuranceInfo;
import org.click.carservice.db.entity.car.CarServiceInsuranceInfoParam;

import java.util.List;

/**
 * 保单信息Mapper接口
 * 
 * @author huangYan
 * @date 2023-08-04
 */
public interface CarServiceInsuranceInfoMapper extends BaseMapper<CarServiceInsuranceInfo> {

    List<CarServiceInsuranceInfo> selectCarServiceInsuranceInfoList(@Param(Constants.WRAPPER) LambdaQueryWrapper<CarServiceInsuranceInfo> lqw, @Param("c") CarServiceInsuranceInfoParam carServiceInsuranceInfo);
}
