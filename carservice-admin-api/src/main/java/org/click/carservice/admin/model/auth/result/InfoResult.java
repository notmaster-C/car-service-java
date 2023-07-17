package org.click.carservice.admin.model.auth.result;

import lombok.Data;
import org.click.carservice.db.entity.AdminInfo;

import java.io.Serializable;
import java.util.Collection;

/**
 * 管理员信息
 *
 * @author click
 */
@Data
public class InfoResult implements Serializable {

    /**
     * 管理员名称
     */
    private String name;

    /**
     * 头像图片
     */
    private String avatar;

    /**
     * 管理员信息
     */
    private AdminInfo adminInfo;

    /**
     * 管理员角色列表
     */
    private Collection<String> roles;

    /**
     * 管理员权限列表
     */
    private Collection<String> perms;

}
