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
import org.click.carservice.db.domain.CarServiceGoodsAttribute;
import org.click.carservice.db.service.impl.GoodsAttributeServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品参数服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_goods_attribute")
public class WxGoodsAttributeService extends GoodsAttributeServiceImpl {


    @Cacheable(sync = true)
    public List<CarServiceGoodsAttribute> queryByGid(String goodsId) {
        QueryWrapper<CarServiceGoodsAttribute> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGoodsAttribute.GOODS_ID, goodsId);
        return queryAll(wrapper);
    }

}
