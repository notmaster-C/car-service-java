package org.click.carservice.core.storage.storage;
/**
 * Copyright (c) [click] [927069313@qq.com]
 * [naonao-plus] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.storage.service.Storage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * 服务器本地对象存储服务
 * @author click
 */
@Slf4j
@Data
public class LocalStorage implements Storage {

    /**
     * 根路径
     */
    private Path rootLocation;
    /**
     * 添加对象存储地址
     */
    private String storagePath;
    /**
     * 这个地方应该是wx模块的WxStorageController的fetch方法对应的地址
     */
    private String address;

    /**
     * 添加对象存储·地址
     */
    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
        this.rootLocation = Paths.get(storagePath);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void store(File file, String keyName) {
        try {
            Path datePath = rootLocation.resolve(getNewPath(keyName));
            Files.createDirectories(datePath);
            Files.copy(datePath, file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + keyName, e);
        }
    }

    @Override
    public void store(MultipartFile file, String keyName) {
        try {
            Path datePath = rootLocation.resolve(getNewPath(keyName));
            Files.createDirectories(datePath);
            Files.copy(file.getInputStream(), datePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + keyName, e);
        }
    }

    @Override
    public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        try {
            Path datePath = rootLocation.resolve(getNewPath(keyName));
            Files.createDirectories(datePath);
            Files.copy(inputStream, datePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + keyName, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(rootLocation, 1)
                    .filter(path -> !path.equals(rootLocation))
                    .map(path -> rootLocation.relativize(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String keyName) {
        return rootLocation.resolve(getNewPath(keyName));
    }

    @Override
    public Resource loadAsResource(String keyName) {
        try {
            Path file = load(keyName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void delete(String filename) {
        Path file = load(filename);
        try {
            Files.delete(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String generateUrl(String keyName) {
        return address + keyName;
    }
}