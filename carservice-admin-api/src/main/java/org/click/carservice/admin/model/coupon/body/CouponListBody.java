package org.click.carservice.admin.model.coupon.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 优惠券列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CouponListBody extends PageBody {

    /**
     * 优惠券类型
     */
    private Short type;
    /**
     * 优惠券名称
     */
    private String name;
    /**
     * 优惠券状态
     */
    private Short status;


}
