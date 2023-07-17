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
    private carserviceGoods goods;

    /**
     * 团购规则
     */
    private carserviceGrouponRules grouponRules;

    /**
     * 商品货品信息
     */
    private List<carserviceGoodsProduct> products;

    /**
     * 商品参数信息
     */
    private List<carserviceGoodsAttribute> attributes;

    /**
     * 商品规格信息
     */
    private List<carserviceGoodsSpecification> specifications;


}
