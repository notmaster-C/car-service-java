package org.click.carservice.admin.model.goods.result;

import lombok.Data;
import org.click.carservice.db.domain.*;

import java.io.Serializable;
import java.util.List;

/**
 * 商品详情
 *
 * @author click
 */
@Data
public class GoodsDetailResult implements Serializable {

    /**
     * 分类ID列表
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
     * 货品信息
     */
    private List<carserviceGoodsProduct> products;
    /**
     * 商品参数列表
     */
    private List<carserviceGoodsAttribute> attributes;
    /**
     * 商品规格列表
     */
    private List<carserviceGoodsSpecification> specifications;


}
