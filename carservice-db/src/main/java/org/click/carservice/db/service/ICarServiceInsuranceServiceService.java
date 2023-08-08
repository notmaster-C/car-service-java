package org.click.carservice.db.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import org.click.carservice.db.domain.CarServiceInsuranceService;

/**
 * 保险信息服务项Service接口
 * 
 * @author huangYan
 * @date 2023-08-07
 */
public interface ICarServiceInsuranceServiceService extends IService<CarServiceInsuranceService> {
    /**
     * 查询保险信息服务项
     *
     * @param id 保险信息服务项主键
     * @return 保险信息服务项
     */
    public CarServiceInsuranceService selectCarServiceInsuranceServiceById(String id);

    /**
     * 查询保险信息服务项列表
     *
     * @param carServiceInsuranceService 保险信息服务项
     * @return 保险信息服务项集合
     */
    public List<CarServiceInsuranceService> selectCarServiceInsuranceServiceList(CarServiceInsuranceService carServiceInsuranceService);

    /**
     * 新增保险信息服务项
     *
     * @param carServiceInsuranceService 保险信息服务项
     * @return 结果
     */
    public int insertCarServiceInsuranceService(CarServiceInsuranceService carServiceInsuranceService);

    /**
     * 修改保险信息服务项
     *
     * @param carServiceInsuranceService 保险信息服务项
     * @return 结果
     */
    public int updateCarServiceInsuranceService(CarServiceInsuranceService carServiceInsuranceService);

}
