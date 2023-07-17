package org.click.carservice.admin.model.aftersale.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 售后列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AftersaleListBody extends PageBody {

    /**
     * 售后状态
     */
    private Short status;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 售后编号
     */
    private String aftersaleSn;


}
