package org.click.carservice.db.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import org.click.carservice.db.domain.CarServiceInsuranceInfo;
import org.click.carservice.db.entity.car.CarServiceInsuranceInfoParam;

/**
 * 保单信息Service接口
 *
 * @author huangYan
 * @date 2023-08-07
 */
public interface ICarServiceInsuranceInfoService extends IService<CarServiceInsuranceInfo> {
    /**
     * 查询保单信息
     *
     * @param id 保单信息主键
     * @return 保单信息
     */
    public CarServiceInsuranceInfo selectCarServiceInsuranceInfoById(String id);

    /**
     * 查询保单信息列表
     *
     * @param carServiceInsuranceInfo 保单信息
     * @return 保单信息集合
     */
    public List<CarServiceInsuranceInfo> selectCarServiceInsuranceInfoList(CarServiceInsuranceInfoParam carServiceInsuranceInfo);

    /**
     * 新增保单信息
     *
     * @param carServiceInsuranceInfo 保单信息
     * @return 结果
     */
    public int insertCarServiceInsuranceInfo(CarServiceInsuranceInfo carServiceInsuranceInfo);

    /**
     * 修改保单信息
     *
     * @param carServiceInsuranceInfo 保单信息
     * @return 结果
     */
    public int updateCarServiceInsuranceInfo(CarServiceInsuranceInfo carServiceInsuranceInfo);

}
