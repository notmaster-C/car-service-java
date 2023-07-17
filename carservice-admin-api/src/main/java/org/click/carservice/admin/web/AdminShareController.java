package org.click.carservice.admin.web;
// Copyright (c) [click] [927069313@qq.com]
// [carservice-plus] is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//             http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.admin.annotation.RequiresPermissionsDesc;
import org.click.carservice.admin.model.share.body.ShareListBody;
import org.click.carservice.admin.service.AdminShareService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分享管理
 *
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/share")
@Validated
public class AdminShareController {

    @Autowired
    private AdminShareService shareService;

    /**
     * 分享记录查询
     */
    @SaCheckPermission("admin:share:list")
    @RequiresPermissionsDesc(menu = {"分享管理", "分享记录"}, button = "查询")
    @GetMapping("/list")
    public Object list(ShareListBody body) {
        return ResponseUtil.okList(shareService.querySelective(body));
    }


}
