package org.click.carservice.wx.model.brand.result;

import lombok.Data;
import org.click.carservice.db.domain.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class BrandGoodsDetailResult implements Serializable {

    /**
     * 分类ids
     */
    private String[] categoryIds;

    /**
     * 商品信息
     */
    private CarServiceGoods goods;

    /**
     * 团购规则
     */
    private CarServiceGrouponRules grouponRules;

    /**
     * 商品货品信息
     */
    private List<CarServiceGoodsProduct> products;

    /**
     * 商品参数信息
     */
    private List<CarServiceGoodsAttribute> attributes;

    /**
     * 商品规格信息
     */
    private List<CarServiceGoodsSpecification> specifications;


}
