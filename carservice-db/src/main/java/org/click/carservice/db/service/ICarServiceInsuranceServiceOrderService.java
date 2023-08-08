package org.click.carservice.db.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import org.click.carservice.db.domain.CarServiceInsuranceServiceOrder;

/**
 * 保险服务项-订单Service接口
 * 
 * @author huangYan
 * @date 2023-08-07
 */
public interface ICarServiceInsuranceServiceOrderService extends IService<CarServiceInsuranceServiceOrder> {
    /**
     * 查询保险服务项-订单
     * 
     * @param id 保险服务项-订单主键
     * @return 保险服务项-订单
     */
    public CarServiceInsuranceServiceOrder selectCarServiceInsuranceServiceOrderById(String id);

    /**
     * 查询保险服务项-订单列表
     * 
     * @param carServiceInsuranceServiceOrder 保险服务项-订单
     * @return 保险服务项-订单集合
     */
    public List<CarServiceInsuranceServiceOrder> selectCarServiceInsuranceServiceOrderList(CarServiceInsuranceServiceOrder carServiceInsuranceServiceOrder);

    /**
     * 新增保险服务项-订单
     * 
     * @param carServiceInsuranceServiceOrder 保险服务项-订单
     * @return 结果
     */
    public int insertCarServiceInsuranceServiceOrder(CarServiceInsuranceServiceOrder carServiceInsuranceServiceOrder);

    /**
     * 修改保险服务项-订单
     * 
     * @param carServiceInsuranceServiceOrder 保险服务项-订单
     * @return 结果
     */
    public int updateCarServiceInsuranceServiceOrder(CarServiceInsuranceServiceOrder carServiceInsuranceServiceOrder);

    /**
     * 批量删除保险服务项-订单
     * 
     * @param ids 需要删除的保险服务项-订单主键集合
     * @return 结果
     */
    public int deleteCarServiceInsuranceServiceOrderByIds(List<String> ids);

    /**
     * 删除保险服务项-订单信息
     * 
     * @param id 保险服务项-订单主键
     * @return 结果
     */
    public int deleteCarServiceInsuranceServiceOrderById(String id);
}
