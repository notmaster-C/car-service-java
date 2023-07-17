package org.click.carservice.admin.model.share.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 分享列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShareListBody extends PageBody {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 分享人
     */
    private String inviterId;
    /**
     * 分享状态
     */
    private Short status;

}
