package org.click.carservice.wx.model.groupon.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceGroupon;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class GrouponJoinResult implements Serializable {

    /**
     * 商品ID
     */
    private String goodId;
    /**
     * 团购信息
     */
    private CarServiceGroupon groupon;

}
