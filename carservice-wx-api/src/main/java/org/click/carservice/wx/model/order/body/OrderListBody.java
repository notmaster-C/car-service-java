package org.click.carservice.wx.model.order.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderListBody extends PageBody implements Serializable {

    /**
     * 查看类型
     */
    private Integer showType;


}
