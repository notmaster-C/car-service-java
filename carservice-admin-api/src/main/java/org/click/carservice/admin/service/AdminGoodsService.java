package org.click.carservice.admin.service;
/**
 * Copyright (c) [click] [927069313@qq.com]
 * [carservice-plus] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.admin.model.goods.body.GoodsListBody;
import org.click.carservice.db.domain.CarServiceGoods;
import org.click.carservice.db.enums.GoodsStatus;
import org.click.carservice.db.service.impl.GoodsServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;


/**
 * 商品服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_goods")
public class AdminGoodsService extends GoodsServiceImpl {


    /**
     * 获取店铺下的所有商品
     */
    @Cacheable(sync = true)
    public List<CarServiceGoods> queryByBrand(String brandId) {
        QueryWrapper<CarServiceGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGoods.BRAND_ID, brandId);
        wrapper.orderByDesc(CarServiceGoods.WEIGHT);
        return queryAll(wrapper);
    }


    /**
     * 管理后台查询商品信息
     */

    @Cacheable(sync = true)
    public List<CarServiceGoods> querySelective(GoodsListBody body) {
        QueryWrapper<CarServiceGoods> wrapper = startPage(body);
        if (body.getStatus() != null) {
            wrapper.eq(CarServiceGoods.STATUS, body.getStatus());
        }
        if (body.getGoodsId() != null) {
            wrapper.eq(CarServiceGoods.ID, body.getGoodsId());
        }
        if (body.getBrandId() != null) {
            wrapper.eq(CarServiceGoods.BRAND_ID, body.getBrandId());
        }
        if (StringUtils.hasText(body.getName())) {
            wrapper.like(CarServiceGoods.NAME, body.getName());
        }
        if (StringUtils.hasText(body.getGoodsSn())) {
            wrapper.like(CarServiceGoods.GOODS_SN, body.getGoodsSn());
        }
        wrapper.orderByDesc(CarServiceGoods.WEIGHT);
        return queryAll(wrapper);
    }


    /**
     * 更具商品ID列表查询商品
     */
    @Cacheable(sync = true)
    public List<CarServiceGoods> queryByIds(String[] ids) {
        QueryWrapper<CarServiceGoods> wrapper = new QueryWrapper<>();
        wrapper.in(CarServiceGoods.ID, Arrays.asList(ids));
        wrapper.eq(CarServiceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        wrapper.orderByDesc(CarServiceGoods.WEIGHT);
        return queryAll(wrapper);
    }


}
