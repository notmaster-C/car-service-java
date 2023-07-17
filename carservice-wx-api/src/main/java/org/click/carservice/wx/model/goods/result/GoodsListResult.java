package org.click.carservice.wx.model.goods.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceCategory;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class GoodsListResult implements Serializable {

    /**
     * 数据列表
     */
    private List<?> list;

    /**
     * 总数据条数
     */
    private long total;

    /**
     * 当前页码
     */
    private long page;

    /**
     * 每页数量
     */
    private long limit;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 分类列表
     */
    private List<CarServiceCategory> filterCategoryList;

}
