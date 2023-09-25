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
import org.apache.commons.lang3.ObjectUtils;
import org.click.carservice.core.utils.RegexUtil;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceBrand;
import org.click.carservice.db.enums.BrandStatus;
import org.click.carservice.db.service.impl.BrandServiceImpl;
import org.click.carservice.wx.model.brand.body.BrandListBody;
import org.springframework.cache.annotation.CacheConfig;
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
public class WxBrandService extends BrandServiceImpl {


    public Object validate(CarServiceBrand brand) {
        if (brand == null) {
            return ResponseUtil.badArgument();
        }
        String name = brand.getName();
        String picUrl = brand.getPicUrl();
        String depict = brand.getDepict();
        String mail = brand.getMail();
        Float latitude=brand.getLatitude();
        Float longitude = brand.getLongitude();
        BigDecimal price = brand.getFloorPrice();
        if (!ObjectUtils.allNotNull(name, depict, picUrl, mail, price,latitude,longitude)) {
            return ResponseUtil.fail("请完整填写信息");
        }
        if (RegexUtil.isQQMail(mail)) {
            return ResponseUtil.fail("QQ邮箱不正确");
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
    public List<CarServiceBrand> queryList(BrandListBody body) {
        QueryWrapper<CarServiceBrand> wrapper = startPage(body);
        wrapper.eq(CarServiceBrand.STATUS, BrandStatus.STATUS_NORMAL.getStatus());
        if (StringUtils.hasText(body.getBrandName())) {
            wrapper.like(CarServiceBrand.NAME, body.getBrandName());
        }
        wrapper.orderByDesc(CarServiceBrand.WEIGHT);
        return queryAll(wrapper);
    }

    //@Cacheable(sync = true)
    public List<CarServiceBrand> queryByUserId(String userId) {
        QueryWrapper<CarServiceBrand> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceBrand.USER_ID, userId);
        return queryAll(wrapper);
    }

    //@Cacheable(sync = true)
    public List<CarServiceBrand> queryByIds(List<String> brandIds) {
        QueryWrapper<CarServiceBrand> wrapper = new QueryWrapper<>();
        wrapper.in(CarServiceBrand.ID, brandIds);
        return queryAll(wrapper);
    }

    //@Cacheable(sync = true)
    public CarServiceBrand findByUserId(String userId) {
        QueryWrapper<CarServiceBrand> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceBrand.USER_ID, userId);
        return getOne(wrapper);
    }

    //@Cacheable(sync = true)
    public CarServiceBrand findByUserId(String userId , String brandId) {
        QueryWrapper<CarServiceBrand> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceBrand.USER_ID , userId);
        wrapper.eq(CarServiceBrand.ID , brandId);
        return getOne(wrapper);
    }

    //@Cacheable(sync = true)
    public CarServiceBrand findByBrandName(String name) {
        QueryWrapper<CarServiceBrand> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceBrand.NAME, name);
        return getOne(wrapper, false);
    }


}
