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
import org.click.carservice.db.domain.CarServiceCategory;
import org.click.carservice.db.service.impl.CategoryServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_category")
public class WxCategoryService extends CategoryServiceImpl {


    //@Cacheable(sync = true)
    public List<CarServiceCategory> queryL1() {
        QueryWrapper<CarServiceCategory> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCategory.LEVEL, "L1");
        wrapper.orderByDesc(CarServiceCategory.WEIGHT);
        return queryAll(wrapper);
    }


    //@Cacheable(sync = true)
    public List<CarServiceCategory> queryByPid(String pid) {
        QueryWrapper<CarServiceCategory> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCategory.PID, pid);
        wrapper.orderByDesc(CarServiceCategory.WEIGHT);
        return queryAll(wrapper);
    }


    //@Cacheable(sync = true)
    public List<CarServiceCategory> queryL2ByIds(List<String> ids) {
        QueryWrapper<CarServiceCategory> wrapper = new QueryWrapper<>();
        wrapper.in(CarServiceCategory.ID, ids);
        wrapper.eq(CarServiceCategory.LEVEL, "L2");
        wrapper.orderByDesc(CarServiceCategory.WEIGHT);
        return queryAll(wrapper);
    }


    //@Cacheable(sync = true)
    public List<CarServiceCategory> queryChannel() {
        QueryWrapper<CarServiceCategory> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceCategory.LEVEL, "L1");
        wrapper.orderByDesc(CarServiceCategory.WEIGHT);
        return queryAll(wrapper);
    }


}
