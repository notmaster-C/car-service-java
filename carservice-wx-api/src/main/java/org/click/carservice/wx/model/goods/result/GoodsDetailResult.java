package org.click.carservice.wx.model.goods.result;

import lombok.Data;
import org.click.carservice.db.domain.*;
import org.click.carservice.db.entity.GoodsSpecificationVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class GoodsDetailResult implements Serializable {

    /**
     * 商品信息
     */
    private CarServiceGoods info;
    /**
     * 店铺经度
     */
    private Float latitude;
    /**
     * 店铺纬度
     */
    private Float longitude;
    /**
     * 分享图片
     */
    private String shareImage;
    /**
     * 是否收藏
     */
    private Boolean userHasCollect;
    /**
     * 通用问题
     */
    private List<CarServiceIssue> issue;
    /**
     * 商品评论
     */
    private GoodsCommentResult comment;
    /**
     * 商品规格信息
     */
    private List<GoodsSpecificationVo> specificationList;
    /**
     * 商品货品信息
     */
    private List<CarServiceGoodsProduct> productList;
    /**
     * 商品参数
     */
    private List<CarServiceGoodsAttribute> attribute;
    /**
     * 店铺信息
     */
    private CarServiceBrand brand;
    /**
     * 团购信息
     */
    private List<CarServiceGrouponRules> groupon;
    /**
     * 是否可以分享
     */
    private Boolean share;


}
