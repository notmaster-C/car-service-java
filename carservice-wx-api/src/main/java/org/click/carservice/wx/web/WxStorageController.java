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
import org.click.carservice.core.storage.service.StorageService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 对象存储服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/storage")
@Validated
public class WxStorageController {

    @Autowired
    private StorageService storageService;

    /**
     * 获取微信小程序上传的图片
     */
    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseUtil.ok(storageService.store(file));
    }

    /**
     * 访问存储对象
     * @param key 存储对象key
     */
    @GetMapping("/fetch/{key:.+}")
    public ResponseEntity<Resource> fetch(@PathVariable String key) {
        Resource file = storageService.generateFile(key);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        MediaType mediaType = storageService.getMediaType(key);
        return ResponseEntity.ok().contentType(mediaType).body(file);
    }

    /**
     * 访问存储对象
     * @param key 存储对象key
     */
    @GetMapping("/download/{key:.+}")
    public ResponseEntity<Resource> download(@PathVariable String key) {
        Resource file = storageService.generateFile(key);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        MediaType mediaType = storageService.getMediaType(key);
        String headerValue = "attachment; filename=\"" + file.getFilename() + "\"";
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(file);
    }

}
