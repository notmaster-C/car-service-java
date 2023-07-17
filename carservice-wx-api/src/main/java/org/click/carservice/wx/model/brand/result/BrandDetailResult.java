package org.click.carservice.wx.model.brand.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceBrand;
import org.click.carservice.db.entity.UserInfo;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class BrandDetailResult implements Serializable {

    /**
     * 是否点赞
     */
    private Boolean brandLike;
    /**
     * 店铺信息
     */
    private CarServiceBrand brand;
    /**
     * 用户信息
     */
    private UserInfo brandUser;

}
