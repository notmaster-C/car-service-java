package org.click.carservice.admin.model.role.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 批量更新权限请求参数
 *
 * @author click
 */
@Data
public class RoleUpdatePermissionsBody implements Serializable {

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    private String roleId;
    /**
     * 权限列表
     */
    @NotNull(message = "权限列表不能为空")
    private List<String> permissions;

}
