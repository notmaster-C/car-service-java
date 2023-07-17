package org.click.carservice.admin.model.groupon.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 团购参与列表列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GrouponListBody extends PageBody {

    /**
     * 团购状态
     */
    private Short status;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 团购规则ID
     */
    private String ruleId;
    /**
     * 团购ID
     */
    private String grouponId;

}
