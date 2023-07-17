package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件存储表
 * </p>
 *
 * @author click
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("car_service_storage")
public class CarServiceStorage implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 文件存储表ID
     */
    @TableId("`id`")
    private String id;
    /**
     * 文件的唯一索引
     */
    @TableField("`key`")
    private String key;
    /**
     * 文件名
     */
    @TableField("`name`")
    private String name;
    /**
     * 文件类型
     */
    @TableField("`type`")
    private String type;
    /**
     * 文件大小
     */
    @TableField("`size`")
    private Integer size;
    /**
     * 文件访问链接
     */
    @TableField("`url`")
    private String url;
    /**
     * 创建时间
     */
    @TableField(value = "`add_time`", fill = FieldFill.INSERT)
    private LocalDateTime addTime;
    /**
     * 更新时间
     */
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 逻辑删除
     */
    @TableField("`deleted`")
    @TableLogic
    private Boolean deleted;
    /**
     * 租户ID，用于分割多个租户
     */
    @TableField("`tenant_id`")
    private String tenantId;
    /**
     * 更新版本号
     */
    @TableField("`version`")
    @Version
    private Integer version;

    /////////////////////////////////
    // 数据库字段常量
    ////////////////////////////////

    /**
     * 文件存储表ID
     */
    public static final String ID = "`id`";
    /**
     * 文件的唯一索引
     */
    public static final String KEY = "`key`";
    /**
     * 文件名
     */
    public static final String NAME = "`name`";
    /**
     * 文件类型
     */
    public static final String TYPE = "`type`";
    /**
     * 文件大小
     */
    public static final String SIZE = "`size`";
    /**
     * 文件访问链接
     */
    public static final String URL = "`url`";
    /**
     * 创建时间
     */
    public static final String ADD_TIME = "`add_time`";
    /**
     * 更新时间
     */
    public static final String UPDATE_TIME = "`update_time`";
    /**
     * 逻辑删除
     */
    public static final String DELETED = "`deleted`";
    /**
     * 租户ID，用于分割多个租户
     */
    public static final String TENANT_ID = "`tenant_id`";
    /**
     * 更新版本号
     */
    public static final String VERSION = "`version`";
}
