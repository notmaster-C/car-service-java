package org.click.carservice.db.mapper;

import org.click.carservice.db.domain.CarServiceOrderVerification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.click.carservice.db.domain.dto.OrderVerificationExportDto;
import org.click.carservice.db.domain.query.OrderVerificationQuery;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author click
 * @since 2023-08-13
 */
public interface CarServiceOrderVerificationMapper extends BaseMapper<CarServiceOrderVerification> {

    List<OrderVerificationExportDto> exportOrderVerification(OrderVerificationQuery query);
}
