package org.click.carservice.wx.model.goods.result;

import lombok.Data;
import org.click.carservice.db.domain.carserviceCategory;

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
    private carserviceCategory currentCategory;

    /**
     * 父分类
     */
    private carserviceCategory parentCategory;

    /**
     * 子分类列表
     */
    private List<carserviceCategory> brotherCategory;


}
