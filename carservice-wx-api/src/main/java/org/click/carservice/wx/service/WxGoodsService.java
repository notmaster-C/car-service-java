package org.click.carservice.wx.service;
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
import org.click.carservice.db.domain.carserviceGoods;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.enums.GoodsStatus;
import org.click.carservice.db.service.impl.GoodsServiceImpl;
import org.click.carservice.wx.model.brand.result.BrandGoodsListBody;
import org.click.carservice.wx.model.goods.body.GoodsListBody;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 商品服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_goods")
public class WxGoodsService extends GoodsServiceImpl {

    /**
     * 获取热卖商品已上架
     */
    @Cacheable(sync = true)
    public List<carserviceGoods> queryByHot(Integer limit) {
        QueryWrapper<carserviceGoods> wrapper = startPage(new PageBody(limit));
        wrapper.eq(carserviceGoods.IS_HOT, true);
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        return queryAll(wrapper);
    }

    /**
     * 获取新品上市已上架
     */
    @Cacheable(sync = true)
    public List<carserviceGoods> queryByNew(Integer limit) {
        QueryWrapper<carserviceGoods> wrapper = startPage(new PageBody(limit));
        wrapper.eq(carserviceGoods.IS_NEW, true);
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        return queryAll(wrapper);
    }

    /**
     * 获取团购商品已上架
     */
    @Cacheable(sync = true)
    public List<carserviceGoods> queryByGroupon(Integer limit) {
        QueryWrapper<carserviceGoods> wrapper = startPage(new PageBody(limit));
        wrapper.eq(carserviceGoods.IS_GROUPON, true);
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        return queryAll(wrapper);
    }

    /**
     * 分页获取所有已上架商品
     */
    @Cacheable(sync = true)
    public List<carserviceGoods> queryByAll(Integer limit) {
        QueryWrapper<carserviceGoods> wrapper = startPage(new PageBody(limit));
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        return queryAll(wrapper);
    }


    /**
     * 获取店铺下已上架的商品
     */
    @Cacheable(sync = true)
    public List<carserviceGoods> queryByBrand(String brandId, Integer limit) {
        QueryWrapper<carserviceGoods> wrapper = startPage(new PageBody(limit));
        wrapper.eq(carserviceGoods.BRAND_ID, brandId);
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        return queryAll(wrapper);
    }


    /**
     * 获取店铺下的商品
     */
    @Cacheable(sync = true)
    public List<carserviceGoods> queryByBrand(BrandGoodsListBody body) {
        QueryWrapper<carserviceGoods> wrapper = startPage(body);
        wrapper.eq(carserviceGoods.BRAND_ID, body.getBrandId());
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        return queryAll(wrapper);
    }


    /**
     * 获取分类下已上架的商品
     */
    @Cacheable(sync = true)
    public List<carserviceGoods> queryByCategory(String catId, Integer limit) {
        QueryWrapper<carserviceGoods> wrapper = startPage(new PageBody(limit));
        wrapper.eq(carserviceGoods.CATEGORY_ID, catId);
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        return queryAll(wrapper);
    }


    /**
     * 小程序搜索商品信息
     */
    @Cacheable(sync = true)
    public List<carserviceGoods> querySelective(GoodsListBody body) {
        QueryWrapper<carserviceGoods> wrapper = startPage(body);
        if (body.getCategoryId() != null && !body.getCategoryId().equals("0")) {
            wrapper.eq(carserviceGoods.CATEGORY_ID, body.getCategoryId());
        }
        if (body.getBrandId() != null) {
            wrapper.eq(carserviceGoods.BRAND_ID, body.getBrandId());
        }
        if (body.getIsNew() != null) {
            wrapper.eq(carserviceGoods.IS_NEW, body.getIsNew());
        }
        if (body.getIsHot() != null) {
            wrapper.eq(carserviceGoods.IS_HOT, body.getIsHot());
        }
        if (body.getKeyword() != null) {
            wrapper.like(carserviceGoods.KEYWORDS, body.getKeyword())
                    .or().like(carserviceGoods.NAME, body.getKeyword());
        }
        wrapper.orderByDesc(carserviceGoods.WEIGHT);
        if (body.getIsAdmin() != null && body.getIsAdmin()) {
            return queryAll(wrapper);
        } else {
            wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        }
        return queryAll(wrapper);
    }

    /**
     * 获取某个商品信息，已上架商品，仅展示相关内容
     */
    @Cacheable(sync = true)
    public carserviceGoods findByIdVO(String id) {
        QueryWrapper<carserviceGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGoods.ID, id);
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        return getOne(wrapper);
    }


    /**
     * 获取所有在售物品总数
     */
    @Cacheable(sync = true)
    public Integer queryOnSale() {
        QueryWrapper<carserviceGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        return Math.toIntExact(count(wrapper));
    }


    /**
     * 按分类获取已上架商品
     */
    @Cacheable(sync = true)
    public List<String> getCatIds(String brandId, String keywords, Boolean isHot, Boolean isNew) {
        QueryWrapper<carserviceGoods> wrapper = new QueryWrapper<>();
        if (!Objects.isNull(brandId)) {
            wrapper.eq(carserviceGoods.BRAND_ID, brandId);
        }
        if (!Objects.isNull(isNew)) {
            wrapper.eq(carserviceGoods.IS_NEW, isNew);
        }
        if (!Objects.isNull(isHot)) {
            wrapper.eq(carserviceGoods.IS_HOT, isHot);
        }
        if (!Objects.isNull(keywords)) {
            wrapper.like(carserviceGoods.KEYWORDS, keywords);
            wrapper.like(carserviceGoods.NAME, keywords);
        }
        wrapper.eq(carserviceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        List<carserviceGoods> goodsList = queryAll(wrapper);
        List<String> cats = new ArrayList<>();
        for (carserviceGoods goods : goodsList) {
            cats.add(goods.getCategoryId());
        }
        return cats;
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
