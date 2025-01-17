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
import org.click.carservice.admin.model.brand.body.BrandListBody;
import org.click.carservice.core.utils.RegexUtil;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceBrand;
import org.click.carservice.db.enums.BrandStatus;
import org.click.carservice.db.service.impl.BrandServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 店铺服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_brand")
public class AdminBrandService extends BrandServiceImpl {


    public Object validate(CarServiceBrand brand) {
        if (brand == null) {
            return ResponseUtil.badArgument();
        }
        String mail = brand.getMail();
        BigDecimal price = brand.getFloorPrice();
        if (StringUtils.hasText(mail)) {
            if (RegexUtil.isQQMail(mail)) {
                return ResponseUtil.fail("QQ邮箱不正确");
            }
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseUtil.fail("店铺最低金额不能小于零");
        }
        CarServiceBrand byBrandName = findByBrandName(brand.getName());
        if (byBrandName != null && !Objects.equals(brand.getUserId(), byBrandName.getUserId())) {
            return ResponseUtil.fail("店铺名称已存在");
        }
        return null;
    }


    //@Cacheable(sync = true)
    public List<CarServiceBrand> all() {
        QueryWrapper<CarServiceBrand> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceBrand.STATUS, BrandStatus.STATUS_NORMAL.getStatus());
        wrapper.orderByDesc(CarServiceBrand.WEIGHT);
        return list(wrapper);
    }


    //@Cacheable(sync = true)
    public List<CarServiceBrand> querySelective(BrandListBody body) {
        QueryWrapper<CarServiceBrand> wrapper = startPage(body);
        if (!Objects.isNull(body.getId())) {
            wrapper.eq(CarServiceBrand.ID, body.getId());
        }
        if (!Objects.isNull(body.getName())) {
            wrapper.like(CarServiceBrand.NAME, body.getName());
        }
        wrapper.orderByDesc(CarServiceBrand.WEIGHT);
        return queryAll(wrapper);
    }

    //@Cacheable(sync = true)
    public CarServiceBrand findByBrandName(String name) {
        QueryWrapper<CarServiceBrand> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceBrand.NAME, name);
        return getOne(wrapper, false);
    }


}
