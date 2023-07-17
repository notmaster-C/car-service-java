package org.click.carservice.admin.model.history.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 搜索列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HistoryListBody extends PageBody {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 关键字
     */
    private String keyword;


}
