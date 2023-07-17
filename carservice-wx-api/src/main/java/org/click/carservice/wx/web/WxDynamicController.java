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
import org.click.carservice.db.domain.CarServiceDynamic;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.dynamic.body.DynamicListBody;
import org.click.carservice.wx.web.impl.WxWebDynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 动态信息
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/dynamic")
@Validated
public class WxDynamicController {

    @Autowired
    private WxWebDynamicService dynamicService;

    /**
     * 动态列表
     */
    @GetMapping("list")
    public Object list(@LoginUser(require = false) String userId, DynamicListBody body) {
        return dynamicService.list(userId , body);
    }

    /**
     * 发布日常
     * @param userId   用户ID
     * @param dynamic 动态发布信息
     * @return 操作结果
     */
    @PostMapping("submit")
    public Object submit(@LoginUser String userId, @Valid @RequestBody CarServiceDynamic dynamic) {
        return dynamicService.submit(userId , dynamic);
    }

    /**
     * 删除日常
     * @param userId   用户ID
     * @param timeVoId 动态发布信息的id
     * @return 操作结果
     */
    @PostMapping("delete")
    public Object delete(@LoginUser String userId, @JsonBody String timeVoId) {
        return dynamicService.delete(userId , timeVoId);
    }


}
