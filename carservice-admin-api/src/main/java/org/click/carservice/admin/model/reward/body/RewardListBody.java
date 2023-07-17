package org.click.carservice.admin.model.reward.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 赏金列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RewardListBody extends PageBody {

    /**
     * 商品ID
     */
    private String goodsId;

}
