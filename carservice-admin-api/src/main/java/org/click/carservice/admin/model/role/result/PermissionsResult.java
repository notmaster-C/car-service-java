package org.click.carservice.admin.model.role.result;

import lombok.Data;
import org.click.carservice.admin.annotation.entity.PermVo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 管理员的权限情况
 *
 * @author click
 */
@Data
public class PermissionsResult implements Serializable {

    /**
     * 系统权限
     */
    private List<PermVo> systemPermissions;
    /**
     * 路由权限
     */
    private Set<String> assignedPermissions;
    /**
     * 用户权限
     */
    private Set<String> curPermissions;

}
