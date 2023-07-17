package org.click.carservice.admin.model.groupon.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 团购规则列表列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GrouponRuleListBody extends PageBody {

    /**
     * 商品ID
     */
    private String goodsId;

}
