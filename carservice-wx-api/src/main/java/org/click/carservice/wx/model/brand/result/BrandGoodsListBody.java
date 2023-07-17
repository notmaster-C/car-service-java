package org.click.carservice.wx.model.brand.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BrandGoodsListBody extends PageBody implements Serializable {

    /**
     * 店铺ID
     */
    private String brandId;


}
