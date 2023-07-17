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
 * 搜索历史表
 * </p>
 *
 * @author click
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("carservice_search_history")
public class carserviceSearchHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 搜索历史表ID
     */
    @TableId("`id`")
    private String id;
    /**
     * 用户表的用户ID
     */
    @TableField("`user_id`")
    private String userId;
    /**
     * 搜索关键字
     */
    @TableField("`keyword`")
    private String keyword;
    /**
     * 搜索来源，如pc、wx、app
     */
    @TableField("`from`")
    private String from;
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
     * 搜索历史表ID
     */
    public static final String ID = "`id`";
    /**
     * 用户表的用户ID
     */
    public static final String USER_ID = "`user_id`";
    /**
     * 搜索关键字
     */
    public static final String KEYWORD = "`keyword`";
    /**
     * 搜索来源，如pc、wx、app
     */
    public static final String FROM = "`from`";
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
