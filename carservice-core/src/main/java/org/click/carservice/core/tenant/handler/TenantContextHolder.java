package org.click.carservice.core.tenant.handler;
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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.click.carservice.core.tenant.enums.IgnoreRequest;
import org.click.carservice.core.utils.http.GlobalWebUtil;
import org.click.carservice.core.utils.token.TokenManager;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 多租户上下文工具类
 * @author click
 */
public class TenantContextHolder {

    /**租户ID 线程独立，可解决并发问题*/
    private static final ThreadLocal<String> LOCAL_TENANT_ID = new ThreadLocal<>();
    /**租户请求头*/
    private static final String TENANT_ID_HEADER = "X-carservice-TenantId";
    /**默认租户ID*/
    private static final String TENANT_ID_DEFAULT = "0";

    /**默认租户ID*/
    public static String getDefaultId() {
        return TENANT_ID_DEFAULT;
    }

    /**判断是否是默认ID*/
    public static Boolean isDefaultId(String tenantId) {
        return TENANT_ID_DEFAULT.equals(tenantId);
    }

    /**判断是否是默认ID*/
    public static Boolean isDefaultId() {
        return TENANT_ID_DEFAULT.equals(LOCAL_TENANT_ID.get());
    }

    /**设置默认租户ID*/
    public static void setDefaultId() {
        setLocalTenantId(TENANT_ID_DEFAULT);
    }

    /**
     * 获取当前线程租户
     * @return 当前租户id
     */
    public static String getLocalTenantId() {
        if (LOCAL_TENANT_ID.get() == null) {
            if (returnTenantId(true) == null) {
                return setLocalTenantId(TENANT_ID_DEFAULT);
            }
        }
        if (LOCAL_TENANT_ID.get() == null) {
            return setLocalTenantId(TENANT_ID_DEFAULT);
        }
        return LOCAL_TENANT_ID.get();
    }

    /**
     * 清除当前租户
     */
    public static void removeLocalTenantId() {
        LOCAL_TENANT_ID.remove();
        //清除多数据源
        DynamicDataSourceContextHolder.clear();
    }

    /**
     * 设置当前线程租户
     * @param tenantId 当前租户id
     * @return 当前租户id
     */
    public static String setLocalTenantId(String tenantId) {
        LOCAL_TENANT_ID.remove();
        LOCAL_TENANT_ID.set(tenantId);
        if (tenantId != null) {
            DynamicDataSourceContextHolder.clear();
            DynamicDataSourceContextHolder.push(tenantId);
        }
        return tenantId;
    }

    /**
     * 从请求中获取到token，从请求头中解析出tenantId
     * @param isContext 是否需要将tenantId写如上下文 true写入 ， false只返回tenantId
     * @return 租户ID
     */
    public static String returnTenantId(Boolean isContext) {
        //获取当前请求如果没有请求则是内部调用返回默认租户
        HttpServletRequest request = GlobalWebUtil.getRequest();
        //获取当前请求如果没有请求则是内部调用返回默认租户
        if (Objects.isNull(request)) {
            if (isContext) {
                return setLocalTenantId(TENANT_ID_DEFAULT);
            } else {
                return TENANT_ID_DEFAULT;
            }
        }
        //获取请求地址如果为白名单地址则返回默认租户
        String requestUri = request.getRequestURI();
        if (IgnoreRequest.parseValue(requestUri)) {
            if (isContext) {
                return setLocalTenantId(TENANT_ID_DEFAULT);
            } else {
                return TENANT_ID_DEFAULT;
            }
        }
        //获取请求头如果未携带请求头则返回null
        String decode = request.getHeader(TENANT_ID_HEADER);
        String tenantId = TokenManager.getTenantId(decode);
        if (!StringUtils.hasText(tenantId)) {
            if (isContext) {
                return setLocalTenantId(null);
            } else {
                return null;
            }
        }
        //返回获取到的租户ID
        if (isContext) {
            return setLocalTenantId(tenantId);
        } else {
            return tenantId;
        }
    }

}
