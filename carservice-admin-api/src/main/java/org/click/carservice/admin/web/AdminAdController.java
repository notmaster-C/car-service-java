package org.click.carservice.admin.web;
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

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.admin.annotation.RequiresPermissionsDesc;
import org.click.carservice.admin.model.ad.body.AdListBody;
import org.click.carservice.admin.service.AdminAdService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceAd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * 广告管理
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/ad")
@Validated
public class AdminAdController {

    @Autowired
    private AdminAdService adService;

    /**
     * 查询
     */
    @SaCheckPermission("admin:ad:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "广告管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(AdListBody body) {
        return ResponseUtil.okList(adService.querySelective(body));
    }

    /**
     * 添加
     */
    @SaCheckPermission("admin:ad:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "广告管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody carserviceAd ad) {
        Object error = adService.validate(ad);
        if (error != null) {
            return error;
        }
        adService.add(ad);
        return ResponseUtil.ok(ad);
    }

    /**
     * 详情
     */
    @SaCheckPermission("admin:ad:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "广告管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull String id) {
        return ResponseUtil.ok(adService.findById(id));
    }

    /**
     * 编辑
     */
    @SaCheckPermission("admin:ad:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "广告管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@Valid @RequestBody carserviceAd ad) {
        Object error = adService.validate(ad);
        if (error != null) {
            return error;
        }
        if (adService.updateVersionSelective(ad) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(ad);
    }

    /**
     * 删除
     */
    @SaCheckPermission("admin:ad:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "广告管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@NotNull String id) {
        adService.deleteById(id);
        return ResponseUtil.ok();
    }

}
