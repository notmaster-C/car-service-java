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
import org.click.carservice.admin.model.keyword.body.KeywordListBody;
import org.click.carservice.admin.service.AdminKeywordService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * 关键词
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/keyword")
@Validated
public class AdminKeywordController {

    @Autowired
    private AdminKeywordService keywordService;

    /**
     * 查询
     */
    @SaCheckPermission("admin:keyword:list")
    @RequiresPermissionsDesc(menu = {"商场管理", "关键词"}, button = "查询")
    @GetMapping("/list")
    public Object list(KeywordListBody body) {
        return ResponseUtil.okList(keywordService.querySelective(body));
    }

    /**
     * 详情
     */
    @SaCheckPermission("admin:keyword:read")
    @RequiresPermissionsDesc(menu = {"商场管理", "关键词"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull String id) {
        return ResponseUtil.ok(keywordService.findById(id));
    }

    /**
     * 添加
     */
    @SaCheckPermission("admin:keyword:create")
    @RequiresPermissionsDesc(menu = {"商场管理", "关键词"}, button = "添加")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody CarServiceKeyword keyword) {
        Object error = keywordService.validate(keyword);
        if (error != null) {
            return error;
        }
        keywordService.add(keyword);
        return ResponseUtil.ok(keyword);
    }

    /**
     * 编辑
     */
    @SaCheckPermission("admin:keyword:update")
    @RequiresPermissionsDesc(menu = {"商场管理", "关键词"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@Valid @RequestBody CarServiceKeyword keyword) {
        Object error = keywordService.validate(keyword);
        if (error != null) {
            return error;
        }
        if (keywordService.updateVersionSelective(keyword) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(keyword);
    }

    /**
     * 删除
     */
    @SaCheckPermission("admin:keyword:delete")
    @RequiresPermissionsDesc(menu = {"商场管理", "关键词"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@NotNull String id) {
        keywordService.deleteById(id);
        return ResponseUtil.ok();
    }

}
