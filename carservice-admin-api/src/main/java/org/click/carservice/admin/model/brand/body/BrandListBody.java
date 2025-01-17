package org.click.carservice.admin.model.brand.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 店铺列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BrandListBody extends PageBody {

    /**
     * 店铺id
     */
    private String id;
    /**
     * 店铺名称
     */
    private String name;


}
