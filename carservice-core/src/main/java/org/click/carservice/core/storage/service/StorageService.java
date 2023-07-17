package org.click.carservice.core.storage.service;
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

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.click.carservice.core.baidu.service.BaibuAipSecCheckService;
import org.click.carservice.core.utils.RandomStrUtil;
import org.click.carservice.db.domain.CarServiceStorage;
import org.click.carservice.db.service.IStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Objects;

/**
 * 提供存储服务类，所有存储服务均由该类对外提供
 * @author click
 */
@Data
public class StorageService {

    private IStorageService storageService;

    private String active;

    private String storagePath;

    private Storage storage;

    /**
     * 存储一个文件对象
     *
     * @param inputStream   文件输入流
     * @param size 文件长度
     * @param contentType   文件类型
     * @param fileName      文件索引名
     */
    public CarServiceStorage store(InputStream inputStream, long size, String contentType, String fileName) {
        String key = generateKey(fileName);
        storage.store(inputStream, size, contentType, key);
        String url = generateUrl(key);
        return addStorage(fileName, size, contentType, key, url);
    }


    /**
     * 存储一个文件对象
     * @param file 文件对象
     */
    public CarServiceStorage store(File file) {
        //添加到对象存储
        String key = generateKey(file.getName());
        storage.store(file, key);
        String url = generateUrl(key);
        String type = FileUtil.getType(file);
        return addStorage(file.getName(), file.length(), type, key, url);
    }


    /**
     * 存储一个文件对象
     * @param file 文件对象
     */
    public CarServiceStorage store(MultipartFile file) {
        //添加到对象存储
        String key = generateKey(Objects.requireNonNull(file.getOriginalFilename()));
        storage.store(file, key);
        String url = generateUrl(key);
        String type = file.getContentType();
        return addStorage(file.getName(), file.getSize(), type, key, url);
    }

    /**
     * 将对象存入数据库
     */
    private CarServiceStorage addStorage(String name, Long size, String type, String key, String url) {
        try {
            BaibuAipSecCheckService.picSecCheck(url);
        } catch (Exception e) {
            delete(key);
            throw new RuntimeException(e.getMessage());
        }
        CarServiceStorage storageInfo = new CarServiceStorage();
        storageInfo.setName(name);
        storageInfo.setSize(Math.toIntExact(size));
        storageInfo.setType(type);
        storageInfo.setKey(key);
        storageInfo.setUrl(url);
        storageService.add(storageInfo);
        return storageInfo;
    }

    /**
     * 查询是否有相同KEY
     * @param key 对象KEY
     */
    public CarServiceStorage findByKey(String key) {
        QueryWrapper<CarServiceStorage> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceStorage.KEY, key);
        return storageService.getOne(wrapper);
    }


    /**
     * 获取对象key值
     * @param originalFilename 文件名称
     */
    private String generateKey(String originalFilename) {
        int index = originalFilename.lastIndexOf('.');
        String suffix = originalFilename.substring(index);
        String key;
        CarServiceStorage storageInfo;
        do {
            key = RandomStrUtil.getRandom(20, RandomStrUtil.TYPE.CAPITAL_NUMBER) + suffix;
            storageInfo = findByKey(key);
        }
        while (storageInfo != null);

        return key;
    }


    /**
     * 获取Resource
     * @param key 对象key
     */
    public Resource generateFile(String key) {
        if (key == null || key.contains("../")) {
            return null;
        }
        return loadAsResource(key);
    }

    /**
     * 获取对象类型
     * @param key 对象key
     */
    public MediaType getMediaType(String key) {
        String type = URLConnection.guessContentTypeFromName(key);
        return MediaType.parseMediaType(type);
    }

    /**
     * 获取Resource
     * @param key 对象key
     */
    public Resource loadAsResource(String key) {
        return storage.loadAsResource(key);
    }

    /**
     * 删除对象
     * @param key 对象key
     */
    public void delete(String key) {
        storage.delete(key);
    }

    /**
     * 获取对象地址
     * @param key   对象key
     */
    private String generateUrl(String key) {
        return storage.generateUrl(key);
    }
}
