package org.click.carservice.core.tenant.annotation.aspect;

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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.click.carservice.core.tenant.annotation.TenantIgnore;
import org.click.carservice.core.tenant.handler.TenantContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author click
 */
@Aspect
@Component
public class TenantIgnoreAspect {

    @Around("@annotation(tenant)")
    public Object tenantAspect(ProceedingJoinPoint joinPoint, TenantIgnore tenant) throws Throwable {
        //添加当前租户
        Object result;
        try {
            TenantContextHolder.setDefaultId();
            result = joinPoint.proceed();
        } finally {
            TenantContextHolder.removeLocalTenantId();
        }
        return result;
    }

}
