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
public class BrandOrderListBody extends PageBody implements Serializable {

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 查看类型
     */
    private Integer showType = 0;


}
