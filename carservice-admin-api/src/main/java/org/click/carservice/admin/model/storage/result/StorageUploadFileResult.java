package org.click.carservice.admin.model.storage.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 富文本响应结果
 *
 * @author click
 */
@Data
public class StorageUploadFileResult implements Serializable {

    /**
     * 接口状态
     */
    private String state;

    /**
     * 文件OriginalFilename名称
     */
    private String title;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件地址
     */
    private String url;

    /**
     * 文件名称
     */
    private String original;


}
