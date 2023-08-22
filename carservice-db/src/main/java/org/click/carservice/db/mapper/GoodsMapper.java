package org.click.carservice.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.click.carservice.db.domain.CarServiceGoods;

import java.util.List;

/**
 * <p>
 * 商品基本信息表 Mapper 接口
 * </p>
 *
 * @author click
 */
public interface GoodsMapper extends BaseMapper<CarServiceGoods> {

    /**
     * 根据小程序商铺用户id获取所有商品
     * @param userId
     * @return
     */
    List<CarServiceGoods> selectByUserId(@Param("userId") String userId, @Param(Constants.WRAPPER) QueryWrapper<CarServiceGoods> eq);
}
