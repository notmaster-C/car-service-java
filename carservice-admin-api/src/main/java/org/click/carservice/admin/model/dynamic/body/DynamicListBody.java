package org.click.carservice.admin.model.dynamic.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 动态列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DynamicListBody extends PageBody {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 动态内容
     */
    private String content;


}
