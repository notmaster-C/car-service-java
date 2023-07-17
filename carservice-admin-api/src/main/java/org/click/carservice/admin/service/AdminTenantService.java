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

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.admin.model.tenant.body.TenantListBody;
import org.click.carservice.core.tenant.handler.TenantContextHolder;
import org.click.carservice.core.utils.bcrypt.CryptoUtil;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceTenant;
import org.click.carservice.db.service.impl.TenantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * 租户管理
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_tenant")
public class AdminTenantService extends TenantServiceImpl {


    @Autowired
    private WxMaService wxService;

    /**
     * 判断租户是否满足条件并加密数据库密码
     * @param tenant 租户
     * @return null
     */
    public Object validate(CarServiceTenant tenant) {
        if (tenant == null) {
            return ResponseUtil.badArgument();
        }
        if (!StringUtils.hasText(tenant.getAddress())) {
            return ResponseUtil.fail("小程序地址不能为空");
        }
        if (!StringUtils.hasText(tenant.getAppId())) {
            return ResponseUtil.fail("小程序appid不能为空");
        }
        if (!StringUtils.hasText(tenant.getAppSecret())) {
            return ResponseUtil.fail("小程序appSecret不能为空");
        }
        //数据库用户名不等于null则加密存储
        if (StringUtils.hasText(tenant.getUsername())) {
            String appId = tenant.getAppId();
            String username = tenant.getUsername();
            String encrypt = CryptoUtil.encrypt(username, appId);
            tenant.setUsername(encrypt);
        }
        //数据库密码不等于null则加密存储
        if (StringUtils.hasText(tenant.getPassword())) {
            String appId = tenant.getAppId();
            String password = tenant.getPassword();
            String encrypt = CryptoUtil.encrypt(password, appId);
            tenant.setPassword(encrypt);
        }
        return null;
    }

    /**
     * 根据appid查询租户并设置上下文
     * @param appid 租户appid
     */
    public void setTenant(String appid) {
        if (StringUtils.hasText(appid)) {
            CarServiceTenant tenant = findAppid(appid);
            if (tenant != null) {
                // 切换微信配置
                wxService.switchoverTo(tenant.getAppId());
                TenantContextHolder.setLocalTenantId(tenant.getId());
            }
        }
    }

    @Cacheable(sync = true)
    public CarServiceTenant findAppid(String appid) {
        QueryWrapper<CarServiceTenant> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceTenant.APP_ID, appid);
        return getOne(wrapper);
    }


    @Cacheable(sync = true)
    public CarServiceTenant findAddress(String address) {
        QueryWrapper<CarServiceTenant> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceTenant.ADDRESS, address);
        return getOne(wrapper);
    }

    @Cacheable(sync = true)
    public List<CarServiceTenant> querySelective(TenantListBody body) {
        QueryWrapper<CarServiceTenant> wrapper = startPage(body);
        if (body.getAddress() != null) {
            wrapper.eq(CarServiceTenant.ADDRESS, body.getAddress());
        }
        return queryAll(wrapper);
    }


}
