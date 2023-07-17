package org.click.carservice.wx.model.catalog.body;

import lombok.Data;
import org.click.carservice.db.domain.carserviceCategory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author click
 */
@Data
public class CatalogIndexResult implements Serializable {

    /**
     * 当前一级分类目录
     */
    private carserviceCategory currentCategory;

    /**
     * 所有一级分类目录
     */
    private List<carserviceCategory> categoryList;

    /**
     * 子分类列表
     */
    private List<carserviceCategory> currentSubCategory;

    /**
     * 所有子分类列表
     */
    private Map<String, List<carserviceCategory>> allList;


}
