package org.click.carservice.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.db.entity.car.UserCarBody;
import org.click.carservice.db.entity.car.UserCarResult;

import java.util.List;

/**
 * <p>
 * 用户车牌信息表 Mapper 接口
 * </p>
 *
 * @author click
 * @since 2023-08-01
 */
@Mapper
public interface CarServiceCarMapper extends BaseMapper<CarServiceCar> {

    /**
     * 查询用户的车牌
     * @param body
     * @param wrapper
     * @return
     */
    List<UserCarResult> selectUserCarPage(@Param("body") UserCarBody body, @Param(Constants.WRAPPER) Wrapper<CarServiceCar> wrapper);

}
