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
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.collect.body.CollectListBody;
import org.click.carservice.wx.model.collect.body.CollectUpdateBody;
import org.click.carservice.wx.web.impl.WxWebCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 * 用户收藏服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/collect")
@Validated
public class WxCollectController {

    @Autowired
    private WxWebCollectService collectService;

    /**
     * 用户收藏列表
     */
    @GetMapping("list")
    public Object list(@LoginUser String userId, CollectListBody body) {
        return collectService.list(userId , body);
    }

    /**
     * 用户收藏添加和取消
     * @param userId 用户ID
     * @param body   请求内容，{ type: xxx, valueId: xxx }
     * @return 操作结果 update
     */
    @PostMapping("update")
    public Object update(@LoginUser String userId, @Valid @RequestBody CollectUpdateBody body) {
        return collectService.update(userId , body);
    }


}