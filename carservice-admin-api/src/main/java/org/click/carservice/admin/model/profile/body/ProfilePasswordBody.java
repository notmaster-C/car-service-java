package org.click.carservice.admin.model.profile.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 管理员修改密码
 *
 * @author click
 */
@Data
public class ProfilePasswordBody implements Serializable {

    /**
     * 旧密码
     */
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;
    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    private String newPassword;
    /**
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    private String confirmPassword;

}
