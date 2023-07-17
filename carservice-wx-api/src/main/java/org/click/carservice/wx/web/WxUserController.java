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
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.web.impl.WxWebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/user")
@Validated
public class WxUserController {

    @Autowired
    private WxWebUserService userService;

    /**
     * 用户个人页面数据
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("index")
    public Object index(@LoginUser String userId) {
        return userService.index(userId);
    }

    /**
     * 用户基本信息
     */
    @GetMapping("info")
    public Object info(@LoginUser String userId) {
        return userService.info(userId);
    }

    /**
     * 用户个人分享记录
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("share")
    public Object share(@LoginUser String userId) {
        return userService.share(userId);
    }

    /**
     *  获取用户余额
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("integral")
    public Object integral(@LoginUser String userId) {
        return userService.integral(userId);
    }

    /**
     * 获取用户交易记录
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("trading-record")
    public Object tradingRecord(@LoginUser String userId, PageBody body) {
        return userService.tradingRecord(userId , body);
    }

    /**
     * 余额提现
     * @param userId 用户id
     * @return 成功or失败
     */
    @PostMapping("withdraw-deposit")
    public Object withdrawDeposit(@LoginUser String userId ,@JsonBody String cashAmount){
        return userService.withdrawDeposit(userId , cashAmount);
    }

}