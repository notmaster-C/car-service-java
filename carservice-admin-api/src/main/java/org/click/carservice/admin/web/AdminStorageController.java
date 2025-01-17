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
import org.click.carservice.admin.model.storage.body.StorageListBody;
import org.click.carservice.admin.model.storage.result.StorageUploadFileResult;
import org.click.carservice.admin.service.AdminStorageService;
import org.click.carservice.core.annotation.JsonBody;
import org.click.carservice.core.storage.service.StorageService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * 对象存储管理
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/storage")
@Validated
public class AdminStorageController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private AdminStorageService adminStorageService;

    /**
     * 对象列表查询
     */
    @SaCheckPermission("admin:storage:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "对象列表")
    @GetMapping("/list")
    public Object list(StorageListBody body) {
        return ResponseUtil.okList(adminStorageService.querySelective(body));
    }

    /**
     * 对象上传
     * @param file 对象文件
     */
    @SaCheckPermission("admin:storage:create")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "对象上传")
    @PostMapping("/create")
    public Object create(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseUtil.ok(storageService.store(file));
    }

    /**
     * 对象详情
     * @param id 对象ID
     */
    @SaCheckPermission("admin:storage:read")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "对象详情")
    @PostMapping("/read")
    public Object read(@NotNull String id) {
        return ResponseUtil.ok(adminStorageService.findById(id));
    }

    /**
     * 对象编辑
     * @param storage 对象信息
     */
    @SaCheckPermission("admin:storage:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "对象编辑")
    @PostMapping("/update")
    public Object update(@Valid @RequestBody CarServiceStorage storage) {
        if (adminStorageService.updateVersionSelective(storage) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(storage);
    }

    /**
     * 删除对象
     * @param key 对象信息
     */
    @SaCheckPermission("admin:storage:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "对象删除")
    @PostMapping("/delete")
    public Object delete(@JsonBody String key) {
        adminStorageService.deleteByKey(key);
        storageService.delete(key);
        return ResponseUtil.ok();
    }

    /**
     * 百度富文本编辑器文件上传
     * @param file 文件
     * @return json
     */
    @SaCheckPermission("admin:storage:uploadFile")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "富文本")
    @PostMapping("/uploadFile")
    public Object uploadFile(@RequestParam("upfile") MultipartFile file) {
        StorageUploadFileResult result = new StorageUploadFileResult();
        try {
            CarServiceStorage storage = storageService.store(file);
            result.setState("SUCCESS");
            result.setOriginal(file.getName());
            result.setSize(storage.getSize());
            result.setTitle(file.getOriginalFilename());
            result.setType(storage.getType());
            result.setUrl(storage.getUrl());
        } catch (Exception e) {
            result.setState("图片上传出错");
        }
        return result;
    }
}
