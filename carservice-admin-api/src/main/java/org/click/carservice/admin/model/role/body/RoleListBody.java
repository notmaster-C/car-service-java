package org.click.carservice.admin.model.role.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * 角色集合请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleListBody extends PageBody implements Serializable {

    /**
     * 角色名称
     */
    private String name;


}
