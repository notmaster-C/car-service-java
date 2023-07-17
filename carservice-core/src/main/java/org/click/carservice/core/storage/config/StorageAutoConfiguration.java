package org.click.carservice.core.storage.config;
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

import org.click.carservice.core.storage.service.StorageService;
import org.click.carservice.core.storage.storage.AliyunStorage;
import org.click.carservice.core.storage.storage.LocalStorage;
import org.click.carservice.core.storage.storage.QiniuStorage;
import org.click.carservice.core.storage.storage.TencentStorage;
import org.click.carservice.db.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author click
 */
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

    private final StorageProperties properties;

    public StorageAutoConfiguration(StorageProperties properties) {
        this.properties = properties;
    }

    @Autowired
    private IStorageService storageService;

    @Bean
    public StorageService storageService() {
        StorageService service = new StorageService();
        String active = this.properties.getActive();
        service.setActive(active);
        service.setStorageService(storageService);
        switch (active) {
            case "local":
                LocalStorage localStorage = localStorage();
                service.setStoragePath(localStorage.getAddress());
                service.setStorage(localStorage);
                break;
            case "aliyun":
                AliyunStorage aliyunStorage = aliyunStorage();
                service.setStoragePath(aliyunStorage.getEndpoint());
                service.setStorage(aliyunStorage());
                break;
            case "tencent":
                TencentStorage tencentStorage = tencentStorage();
                service.setStoragePath(tencentStorage.getBucketName());
                service.setStorage(tencentStorage());
                break;
            case "qiniu":
                QiniuStorage qiniuStorage = qiniuStorage();
                service.setStoragePath(qiniuStorage.getEndpoint());
                service.setStorage(qiniuStorage);
                break;
            default:
                throw new RuntimeException("当前存储模式 " + active + " 不支持");
        }
        return service;
    }

    @Bean
    public LocalStorage localStorage() {
        LocalStorage localStorage = new LocalStorage();
        StorageProperties.Local local = this.properties.getLocal();
        localStorage.setAddress(local.getAddress());
        localStorage.setStoragePath(local.getStoragePath());
        return localStorage;
    }

    @Bean
    public AliyunStorage aliyunStorage() {
        AliyunStorage aliyunStorage = new AliyunStorage();
        StorageProperties.Aliyun aliyun = this.properties.getAliyun();
        aliyunStorage.setAccessKeyId(aliyun.getAccessKeyId());
        aliyunStorage.setAccessKeySecret(aliyun.getAccessKeySecret());
        aliyunStorage.setBucketName(aliyun.getBucketName());
        aliyunStorage.setEndpoint(aliyun.getEndpoint());
        return aliyunStorage;
    }

    @Bean
    public TencentStorage tencentStorage() {
        TencentStorage tencentStorage = new TencentStorage();
        StorageProperties.Tencent tencent = this.properties.getTencent();
        tencentStorage.setSecretId(tencent.getSecretId());
        tencentStorage.setSecretKey(tencent.getSecretKey());
        tencentStorage.setBucketName(tencent.getBucketName());
        tencentStorage.setRegion(tencent.getRegion());
        return tencentStorage;
    }

    @Bean
    public QiniuStorage qiniuStorage() {
        QiniuStorage qiniuStorage = new QiniuStorage();
        StorageProperties.Qiniu qiniu = this.properties.getQiniu();
        qiniuStorage.setAccessKey(qiniu.getAccessKey());
        qiniuStorage.setSecretKey(qiniu.getSecretKey());
        qiniuStorage.setBucketName(qiniu.getBucketName());
        qiniuStorage.setEndpoint(qiniu.getEndpoint());
        return qiniuStorage;
    }
}
