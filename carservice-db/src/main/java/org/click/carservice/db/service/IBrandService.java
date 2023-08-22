package org.click.carservice.db.service;

import org.click.carservice.db.domain.CarServiceBrand;
import org.click.carservice.db.mybatis.IBaseService;

import java.util.List;

/**
 * <p>
 * 品牌商表 服务类
 * </p>
 *
 * @author click
 */
public interface IBrandService extends IBaseService<CarServiceBrand> {

    /**
     * 用户id查询商品
     * @param userId
     * @return
     */
    List<CarServiceBrand> selectByUserId(String userId);
}
