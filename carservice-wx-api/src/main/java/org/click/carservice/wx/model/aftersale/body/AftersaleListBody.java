package org.click.carservice.wx.model.aftersale.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * 售后请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AftersaleListBody extends PageBody implements Serializable {

    /**
     * 售后状态
     */
    private Short status;


}
