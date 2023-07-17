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
 * 角色表
 * </p>
 *
 * @author click
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("carservice_role")
public class carserviceRole implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 角色表ID
     */
    @TableId("`id`")
    private String id;
    /**
     * 角色名称
     */
    @TableField("`name`")
    private String name;
    /**
     * 角色描述
     */
    @TableField("`depict`")
    private String depict;
    /**
     * 是否启用
     */
    @TableField("`enabled`")
    private Boolean enabled;
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
     * 角色表ID
     */
    public static final String ID = "`id`";
    /**
     * 角色名称
     */
    public static final String NAME = "`name`";
    /**
     * 角色描述
     */
    public static final String DEPICT = "`depict`";
    /**
     * 是否启用
     */
    public static final String ENABLED = "`enabled`";
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
