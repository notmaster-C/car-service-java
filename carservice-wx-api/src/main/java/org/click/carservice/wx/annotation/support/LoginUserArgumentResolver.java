package org.click.carservice.wx.annotation.support;
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

import org.click.carservice.core.handler.NotLoginException;
import org.click.carservice.core.utils.BeanUtil;
import org.click.carservice.core.utils.token.TokenManager;
import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.db.enums.UserStatus;
import org.click.carservice.db.service.IUserService;
import org.click.carservice.wx.annotation.LoginUser;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Nonnull;

/**
 * 参数注解 @LoginUser 实现类
 * @author click
 */
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 用户token请求头
     */
    public static final String LOGIN_TOKEN_KEY = "X-carservice-User-Token";


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }


    @Override
    public Object resolveArgument(@Nonnull MethodParameter parameter,
                                  ModelAndViewContainer container,
                                  @Nonnull NativeWebRequest request,
                                  WebDataBinderFactory factory) throws Exception {

        //获取自定义注解
        LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
        if (loginUser == null) {
            return null;
        }
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (!StringUtils.hasText(token)) {
            if (loginUser.require()) {
                throw new NotLoginException("未登录");
            }
            return null;
        }
        String userId = TokenManager.getUserId(token);
        if (userId == null) {
            if (loginUser.require()) {
                throw new NotLoginException("未登录");
            }
            return null;
        }

        IUserService userService = BeanUtil.getBean(IUserService.class);
        CarServiceUser user = userService.findById(userId);
        if (user == null || !UserStatus.isNormal(user)) {
            if (loginUser.require()) {
                throw new NotLoginException("未登录");
            }
            return null;
        }

        //参数类型
        Class<?> classType = parameter.getParameterType();
        if (classType.isAssignableFrom(CarServiceUser.class)) {
            return user;
        }
        if (classType.isAssignableFrom(Integer.class)) {
            return Integer.parseInt(userId);
        }
        return userId;
    }


}
