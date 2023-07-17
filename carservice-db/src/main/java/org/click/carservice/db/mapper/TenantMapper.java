package org.click.carservice.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.click.carservice.db.domain.CarServiceTenant;

/**
 * <p>
 * 租户表，逻辑分库 Mapper 接口
 * </p>
 *
 * @author click
 */
public interface TenantMapper extends BaseMapper<CarServiceTenant> {

}
