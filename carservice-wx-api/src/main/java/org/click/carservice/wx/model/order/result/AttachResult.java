package org.click.carservice.wx.model.order.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class AttachResult implements Serializable {

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 订单ID列表
     */
    private List<String> orderIds;

}
