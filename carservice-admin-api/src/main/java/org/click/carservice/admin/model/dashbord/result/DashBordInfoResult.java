package org.click.carservice.admin.model.dashbord.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class DashBordInfoResult implements Serializable {

    /**
     * 用户数量
     */
    private long userTotal;
    /**
     * 商品数量
     */
    private long goodsTotal;
    /**
     * 货品数量
     */
    private long productTotal;
    /**
     * 订单数量
     */
    private long orderTotal;

}
