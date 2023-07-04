package org.ysling.litemall.core.storage.service;
/**
 *  Copyright (c) [ysling] [927069313@qq.com]
 *  [litemall-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * 对象存储接口
 */
public interface Storage {

    /**
     * 目录分离
     * @param newFileName 获取文件路径
     * @return 文件路径
     */
    default String getNewPath(String newFileName) {
        //获取hashCode值
        int hashCode = newFileName.hashCode();
        //作为一级目录
        int d1 = hashCode & 0xf;
        //把hashCode右移4位得到新的值
        int code = hashCode >>> 4;
        //作为二级目录
        int d2 = code & 0xf;
        //返回一二级目录
        return d1+"/"+d2+"/" + newFileName;
    }

    /**
     * 存储一个文件对象
     * @param file 文件对象
     * @param keyName 对象名称
     */
    void store(File file , String keyName);

    /**
     * 存储一个文件对象
     * @param file 文件对象
     * @param keyName 对象名称
     */
    void store(MultipartFile file , String keyName);

    /**
     * 存储一个文件对象
     *
     * @param inputStream   文件输入流
     * @param contentLength 文件长度
     * @param contentType   文件类型
     * @param keyName       文件名
     */
    void store(InputStream inputStream, long contentLength, String contentType, String keyName);

    /**
     * 获取所有对象地址
     * @return Path
     */
    Stream<Path> loadAll();

    /**
     * 获取对象地址
     * @param keyName 对象名称
     * @return 地址
     */
    Path load(String keyName);

    /**
     * 获取对象信息
     * @param keyName 对象名称
     * @return 对象信息
     */
    Resource loadAsResource(String keyName);

    /**
     * 删除对象
     * @param keyName 对象名称
     */
    void delete(String keyName);

    /**
     * 获取对象URL
     * @param keyName 对象名称
     * @return url地址
     */
    String generateUrl(String keyName);


}