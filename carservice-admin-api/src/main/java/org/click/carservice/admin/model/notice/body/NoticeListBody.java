package org.click.carservice.admin.model.notice.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 管理员通知列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeListBody extends PageBody {


    /**
     * 通知标题
     */
    private String title;
    /**
     * 通知内容
     */
    private String content;


}
