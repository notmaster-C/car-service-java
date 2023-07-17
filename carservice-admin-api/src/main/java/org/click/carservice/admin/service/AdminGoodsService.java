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
import org.click.carservice.db.domain.carserviceGoods;
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
    public List<carserviceGoods> queryByBrand(String brandId) {
        QueryWrapper<carserviceGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGoods.BRAND_ID, brandId);
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        return queryAll(wrapper);
    }


    /**
     * 管理后台查询商品信息
     */

    @Cacheable(sync = true)
    public List<carserviceGoods> querySelective(GoodsListBody body) {
        QueryWrapper<carserviceGoods> wrapper = startPage(body);
        if (body.getStatus() != null) {
            wrapper.eq(carserviceGoods.STATUS, body.getStatus());
        }
        if (body.getGoodsId() != null) {
            wrapper.eq(carserviceGoods.ID, body.getGoodsId());
        }
        if (body.getBrandId() != null) {
            wrapper.eq(carserviceGoods.BRAND_ID, body.getBrandId());
        }
        if (StringUtils.hasText(body.getName())) {
            wrapper.like(carserviceGoods.NAME, body.getName());
        }
        if (StringUtils.hasText(body.getGoodsSn())) {
            wrapper.like(carserviceGoods.GOODS_SN, body.getGoodsSn());
        }
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        return queryAll(wrapper);
    }


    /**
     * 更具商品ID列表查询商品
     */
    @Cacheable(sync = true)
    public List<carserviceGoods> queryByIds(String[] ids) {
        QueryWrapper<carserviceGoods> wrapper = new QueryWrapper<>();
        wrapper.in(carserviceGoods.ID, Arrays.asList(ids));
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        return queryAll(wrapper);
    }


}
