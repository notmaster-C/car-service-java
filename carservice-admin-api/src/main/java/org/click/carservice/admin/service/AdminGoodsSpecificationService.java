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
import org.click.carservice.db.domain.carserviceGoodsSpecification;
import org.click.carservice.db.service.impl.GoodsSpecificationServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品规格服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_goods_specification")
public class AdminGoodsSpecificationService extends GoodsSpecificationServiceImpl {


    @Cacheable(sync = true)
    public List<carserviceGoodsSpecification> queryByGid(String goodsId) {
        QueryWrapper<carserviceGoodsSpecification> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceGoodsSpecification.GOODS_ID, goodsId);
        return queryAll(wrapper);
    }

}
