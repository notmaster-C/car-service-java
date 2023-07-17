package org.click.carservice.wx.model.coupon.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CouponListBody extends PageBody implements Serializable {

    /**
     * 优惠券使用状态
     */
    private Short status;

}
