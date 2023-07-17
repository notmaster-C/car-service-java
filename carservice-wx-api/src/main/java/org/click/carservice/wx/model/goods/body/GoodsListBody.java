package org.click.carservice.wx.model.goods.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.validator.sort.Sort;

import java.io.Serializable;

/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsListBody extends PageBody implements Serializable {

    /**
     * 分类ID
     */
    private String categoryId;
    /**
     * 店铺ID
     */
    private String brandId;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 新品
     */
    private Boolean isNew;
    /**
     * 热卖
     */
    private Boolean isHot;
    /**
     * 管理员
     */
    private Boolean isAdmin;

    /**
     * 排序字段
     */
    @Sort(accepts = {"add_time", "retail_price", "name"})
    private String sort = "add_time";

}
