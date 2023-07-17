package org.click.carservice.admin.model.goods.result;

import lombok.Data;
import org.click.carservice.db.entity.BaseOption;

import java.io.Serializable;
import java.util.List;

/**
 * 获取店铺与分类选择列表
 *
 * @author click
 */
@Data
public class CatAndBrandResult implements Serializable {

    /**
     * 店铺选择列表
     */
    private List<BaseOption> brandList;
    /**
     * 分类选择列表
     */
    private List<BaseOption> categoryList;


}
