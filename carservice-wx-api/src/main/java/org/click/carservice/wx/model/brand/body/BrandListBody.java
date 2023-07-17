package org.click.carservice.wx.model.brand.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;


/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BrandListBody extends PageBody implements Serializable {

    /**
     * 店铺名称
     */
    private String brandName;


}
