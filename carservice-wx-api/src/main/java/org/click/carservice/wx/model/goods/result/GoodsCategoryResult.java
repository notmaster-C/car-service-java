package org.click.carservice.wx.model.goods.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceCategory;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class GoodsCategoryResult implements Serializable {

    /**
     * 当前分类
     */
    private CarServiceCategory currentCategory;

    /**
     * 父分类
     */
    private CarServiceCategory parentCategory;

    /**
     * 子分类列表
     */
    private List<CarServiceCategory> brotherCategory;


}
