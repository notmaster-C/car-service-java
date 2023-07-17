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

import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.storage.service.Storage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * 阿里云对象存储服务
 * @author click
 */
@Slf4j
@Data
public class AliyunStorage implements Storage {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 获取阿里云OSS客户端对象
     * @return ossClient
     */
    private OSSClient getOSSClient() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    private String getBaseUrl() {
        return "https://" + bucketName + "." + endpoint + "/";
    }

    /**
     * 阿里云OSS对象存储简单上传实现
     */
    @Override
    public void store(File file, String keyName) {
        try {
            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length());
            metadata.setContentType(FileUtil.getType(file));
            // 对象键（Key）是对象在存储桶中的唯一标识。
            getOSSClient().putObject(new PutObjectRequest(bucketName, getNewPath(keyName), file, metadata));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 阿里云OSS对象存储简单上传实现
     */
    @Override
    public void store(MultipartFile file, String keyName) {
        try {
            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            // 对象键（Key）是对象在存储桶中的唯一标识。
            getOSSClient().putObject(new PutObjectRequest(bucketName, getNewPath(keyName), file.getInputStream(), metadata));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 阿里云OSS对象存储简单上传实现
     */
    @Override
    public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        try {
            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);
            metadata.setContentType(contentType);
            // 对象键（Key）是对象在存储桶中的唯一标识。
            getOSSClient().putObject(new PutObjectRequest(bucketName, getNewPath(keyName), inputStream, metadata));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String keyName) {
        return null;
    }

    @Override
    public Resource loadAsResource(String keyName) {
        try {
            URL url = new URL(getBaseUrl() + getNewPath(keyName));
            Resource resource = new UrlResource(url);
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
    public void delete(String keyName) {
        try {
            getOSSClient().deleteObject(bucketName, getNewPath(keyName));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public String generateUrl(String keyName) {
        return getBaseUrl() + getNewPath(keyName);
    }


}
