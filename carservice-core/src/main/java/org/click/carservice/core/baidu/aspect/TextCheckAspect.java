package org.click.carservice.core.baidu.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.click.carservice.core.baidu.service.BaibuAipSecCheckService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * post请求参数审核
 *
 * @author click
 */
@Slf4j
@Aspect
@Component
public class TextCheckAspect {


    @Around("@annotation(mapping)")
    public Object tenantAspect(ProceedingJoinPoint joinPoint, PostMapping mapping) throws Throwable {
        // 请求方法参数值
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            StringBuilder params = new StringBuilder();
            for (Object arg : args) {
                if (arg != null) {
                    params.append(arg);
                }
            }
            BaibuAipSecCheckService.textSecCheck(params.toString());
        }
        return joinPoint.proceed();
    }


}