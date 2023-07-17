package org.click.carservice.admin.model.order.result;

import lombok.Data;
import org.click.carservice.db.domain.carserviceOrder;
import org.click.carservice.db.domain.carserviceOrderGoods;
import org.click.carservice.db.entity.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情
 *
 * @author click
 */
@Data
public class OrderDetailResult implements Serializable {

    /**
     * 用户信息
     */
    private UserInfo user;
    /**
     * 订单状态文本
     */
    private String orderStatusText;
    /**
     * 订单信息
     */
    private carserviceOrder order;
    /**
     * 商品信息
     */
    private List<carserviceOrderGoods> orderGoods;


}
