package org.click.carservice.core.tenant.filter;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.tenant.handler.TenantContextHolder;
import org.click.carservice.core.utils.http.GlobalWebUtil;
import org.click.carservice.db.domain.carserviceTenant;
import org.click.carservice.db.service.ITenantService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 多租户过滤器
 */
@Slf4j
@Order(1)
@WebFilter(urlPatterns = {"/wx/*", "/admin/*"}, filterName = "tenantFilter")
public class TenantFilter extends OncePerRequestFilter {


    @Autowired
    private WxMaService wxService;
    @Autowired
    private ITenantService tenantService;

    /**
     * 过滤器
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException {
        try {
            if (this.beforeRequest()) {
                chain.doFilter(request, response);
            } else {
                GlobalWebUtil.sendMessage(response, "未获取到租户授权");
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalWebUtil.sendMessage(response, e.getMessage());
        } finally {
            this.afterRequest();
        }
    }


    /**
     * 请求前调用逻辑
     */
    protected Boolean beforeRequest() {
        //获取当前请求租户
        String tenantId = TenantContextHolder.returnTenantId(false);
        if (tenantId == null) {
            throw new RuntimeException("未找到授权租户");
        }

        //设置当前租户为默认租户
        TenantContextHolder.setDefaultId();
        //如果没有租户直接放行
        if (tenantService.count() == 0) {
            return true;
        }

        //判断是否是默认租户，是则直接放行
        if (TenantContextHolder.isDefaultId(tenantId)) {
            return true;
        }

        //获取当前租户
        carserviceTenant tenant = tenantService.findById(tenantId);
        if (tenant == null) {
            throw new RuntimeException("未找到授权租户");
        }

        // 切换微信配置
        wxService.switchoverTo(tenant.getAppId());
        //切换租户
        TenantContextHolder.setLocalTenantId(tenantId);
        return true;
    }


    /**
     * 请求后调用逻辑
     */
    protected void afterRequest() {
        //清除多租户
        TenantContextHolder.removeLocalTenantId();
        //清除微信配置
        WxMaConfigHolder.remove();
        //清除多数据源
        DynamicDataSourceContextHolder.clear();
        //清除线程缓存
        RequestContextHolder.resetRequestAttributes();
    }


}

