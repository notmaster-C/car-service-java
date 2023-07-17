package org.click.carservice.wx.model.user.result;

import lombok.Data;
import org.click.carservice.db.entity.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class UserShareResult implements Serializable {

    /**
     * 用户信息
     */
    private UserInfo userShare;

    /**
     * 已邀请用户
     */
    private List<UserInfo> inviterList;

}
