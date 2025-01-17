package org.click.carservice.admin.model.footprint.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 用户足迹列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FootprintListBody extends PageBody {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 商品ID
     */
    private String goodsId;


}
