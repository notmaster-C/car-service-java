package org.click.carservice.db.service;

import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.db.mybatis.IBaseService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author click
 */
public interface IUserService extends IBaseService<CarServiceUser> {

    /**
     * 手机号查询用户
     * @param mobil
     * @return
     */
    CarServiceUser selectUserByMobil(String mobil);

}
