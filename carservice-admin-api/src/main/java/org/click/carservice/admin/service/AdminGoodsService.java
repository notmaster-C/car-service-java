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

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.admin.model.goods.body.GoodsListBody;
import org.click.carservice.db.domain.CarServiceGoods;
import org.click.carservice.db.enums.GoodsStatus;
import org.click.carservice.db.mapper.GoodsMapper;
import org.click.carservice.db.service.impl.GoodsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 获取店铺下的所有商品
     */
    //@Cacheable(sync = true)
    public List<CarServiceGoods> queryByBrand(String brandId) {
        QueryWrapper<CarServiceGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGoods.BRAND_ID, brandId);
        wrapper.orderByDesc(CarServiceGoods.WEIGHT);
        return queryAll(wrapper);
    }


    /**
     * 管理后台查询商品信息
     */

//    //@Cacheable(sync = true)
    public List<CarServiceGoods> querySelective(GoodsListBody body) {
        QueryWrapper<CarServiceGoods> wrapper = startPage(body);
        wrapper.orderByDesc(CarServiceGoods.WEIGHT);
        // 数据权限：根据当前管理系统用户id获取到小程序商户id,从而查询到用户的所有订单
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String userId = String.valueOf(tokenInfo.getLoginId());
        // 管理员查询所有
        if ("1".equals(userId)) {
            if (body.getStatus() != null) {
                wrapper.eq(CarServiceGoods.STATUS, body.getStatus());
            }
            if (StrUtil.isNotBlank(body.getGoodsId())) {
                wrapper.eq(CarServiceGoods.ID, body.getGoodsId());
            }
            if (StrUtil.isNotBlank(body.getBrandId())) {
                wrapper.eq(CarServiceGoods.BRAND_ID, body.getBrandId());
            }
            if (StrUtil.isNotBlank(body.getName())) {
                wrapper.like(CarServiceGoods.NAME, body.getName());
            }
            if (StrUtil.isNotBlank(body.getGoodsSn())) {
                wrapper.like(CarServiceGoods.GOODS_SN, body.getGoodsSn());
            }
            return queryAll(wrapper);
        } else {
            // 设置为小程序端用户id
            userId = StpUtil.getSession().getString("carServiceUserId");
            if (body.getStatus() != null) {
                wrapper.eq("csg." + CarServiceGoods.STATUS, body.getStatus());
            }
            if (StrUtil.isNotBlank(body.getGoodsId())) {
                wrapper.eq("csg." + CarServiceGoods.ID, body.getGoodsId());
            }
            if (StrUtil.isNotBlank(body.getBrandId())) {
                wrapper.eq("csg." + CarServiceGoods.BRAND_ID, body.getBrandId());
            }
            if (StrUtil.isNotBlank(body.getName())) {
                wrapper.like("csg." + CarServiceGoods.NAME, body.getName());
            }
            if (StrUtil.isNotBlank(body.getGoodsSn())) {
                wrapper.like("csg." + CarServiceGoods.GOODS_SN, body.getGoodsSn());
            }
            // 不是管理员查询自己的数据
            return goodsMapper.selectByUserId(userId, wrapper);
        }
    }


    /**
     * 更具商品ID列表查询商品
     */
    //@Cacheable(sync = true)
    public List<CarServiceGoods> queryByIds(String[] ids) {
        QueryWrapper<CarServiceGoods> wrapper = new QueryWrapper<>();
        wrapper.in(CarServiceGoods.ID, Arrays.asList(ids));
        wrapper.eq(CarServiceGoods.STATUS, GoodsStatus.GOODS_ON_SALE.getStatus());
        wrapper.orderByDesc(CarServiceGoods.WEIGHT);
        return queryAll(wrapper);
    }


}
