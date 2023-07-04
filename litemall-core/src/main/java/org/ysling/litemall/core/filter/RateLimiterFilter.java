package org.ysling.litemall.core.filter;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.ysling.litemall.core.utils.http.GlobalWebUtil;
import org.ysling.litemall.core.utils.ip.IpUtil;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Objects;

/**
 * 接口防刷限流器 每秒限10次
 */
@Slf4j
@Order(0)
@WebFilter(urlPatterns = {"/wx/*" , "/admin/*"}, filterName = "rateLimiterFilter")
public class RateLimiterFilter extends OncePerRequestFilter {

    @Autowired
    private RedissonClient redisson;

    /**
     * KEY前缀
     */
    private static final String KEY_PREFIX = "RATE_LIMITER_FILTER:";

    /**
     * 限流模式,默认单机
     */
    private final RateType type = RateType.PER_CLIENT;

    /**
     * 限流速率，生成令牌数量
     */
    private final long rate = 1000;

    /**
     * 限流速率 多少秒生成一次令牌
     */
    private final long rateInterval = 60;

    /**
     * 限流速率单位 默认单位秒
     */
    private final RateIntervalUnit timeUnit = RateIntervalUnit.SECONDS;


    /**
     * 过滤器
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        String ip = IpUtil.getIpAddr(request);
        RRateLimiter rRateLimiter = getRateLimiter(ip);
        if (rRateLimiter.tryAcquire(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("[{}] - [{}] 访问频率上限[{}]次/[{}]秒", url, ip, rate, rateInterval);
            GlobalWebUtil.sendMessage(response, "please wait some times!");
        }
    }


    /**
     * 获取限流拦截器
     */
    private RRateLimiter getRateLimiter(String ip){
        RRateLimiter rRateLimiter = redisson.getRateLimiter(KEY_PREFIX + ip);
        // 设置限流
        if(rRateLimiter.isExists()) {
            RateLimiterConfig rateLimiterConfig = rRateLimiter.getConfig();
            // 判断配置是否更新，如果更新，重新加载限流器配置
            if (!Objects.equals(rate, rateLimiterConfig.getRate())
                    || !Objects.equals(timeUnit.toMillis(rateInterval), rateLimiterConfig.getRateInterval())
                    || !Objects.equals(type, rateLimiterConfig.getRateType())) {
                rRateLimiter.delete();
                rRateLimiter.trySetRate(type, rate, rateInterval, timeUnit);
            }
        } else {
            rRateLimiter.trySetRate(type, rate, rateInterval, timeUnit);
        }
        return rRateLimiter;
    }

}

