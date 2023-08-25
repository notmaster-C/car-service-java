package org.click.carservice.db.service;

import org.click.carservice.db.domain.CarServiceOrderVerification;
import org.click.carservice.db.domain.dto.OrderVerificationExportDto;
import org.click.carservice.db.domain.query.OrderVerificationQuery;
import org.click.carservice.db.mybatis.IBaseService;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author click
 * @since 2023-08-13
 */
public interface ICarServiceOrderVerificationService extends IBaseService<CarServiceOrderVerification> {
    /**
     * 查询核销列表
     *
     * @param carServiceOrderVerification 核销
     * @return 核销集合
     */
    public List<CarServiceOrderVerification> selectCarServiceOrderVerificationList(CarServiceOrderVerification carServiceOrderVerification);

    /**
     * 导出对账单
     * @param query
     * @return
     */
    List<OrderVerificationExportDto> exportOrderVerification(OrderVerificationQuery query);
}
