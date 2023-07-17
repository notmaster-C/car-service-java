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
 * 权限表
 * </p>
 *
 * @author click
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("car_service_permission")
public class CarServicePermission implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 权限表ID
     */
    @TableId("`id`")
    private String id;
    /**
     * 角色ID
     */
    @TableField("`role_id`")
    private String roleId;
    /**
     * 权限
     */
    @TableField("`permission`")
    private String permission;
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
     * 权限表ID
     */
    public static final String ID = "`id`";
    /**
     * 角色ID
     */
    public static final String ROLE_ID = "`role_id`";
    /**
     * 权限
     */
    public static final String PERMISSION = "`permission`";
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
