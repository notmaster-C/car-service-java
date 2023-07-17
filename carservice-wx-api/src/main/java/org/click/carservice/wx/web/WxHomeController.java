package org.click.carservice.wx.web;
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

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.annotation.JsonBody;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.home.body.HomeNavigateBody;
import org.click.carservice.wx.web.impl.WxWebHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * 首页服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/home")
@Validated
public class WxHomeController {

    @Autowired
    private WxWebHomeService homeService;


    /**
     * 首页数据
     * @param userId 当用户已经登录时，非空。未登录状态为null
     * @return 首页数据
     */
    @GetMapping("/index")
    public Object index(@LoginUser(require = false) String userId, @JsonBody String appid) {
        return homeService.index(userId , appid);
    }

    /**
     * 判断首页初始化参数是否支持跳转
     */
    @PostMapping("/navigate")
    public Object isNavigate(@Valid @RequestBody HomeNavigateBody body) {
        return homeService.isNavigate(body);
    }

    /**
     * 商城介绍信息
     * @return 商城介绍信息
     */
    @GetMapping("/about")
    public Object about() {
        return homeService.about();
    }


}