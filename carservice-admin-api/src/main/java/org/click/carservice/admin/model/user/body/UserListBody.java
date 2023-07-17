package org.click.carservice.admin.model.user.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * 用户列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserListBody extends PageBody implements Serializable {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 用户邀请者
     */
    private String inviter;
    /**
     * 用户等级
     */
    private Byte level;

}
