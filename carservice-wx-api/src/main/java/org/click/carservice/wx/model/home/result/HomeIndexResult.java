package org.click.carservice.wx.model.home.result;

import lombok.Data;
import org.click.carservice.db.domain.*;
import org.click.carservice.wx.model.goods.body.GoodsListBodyB;

import java.io.Serializable;
import java.util.List;

/**
 * 首页信息
 *
 * @author click
 */
@Data
public class HomeIndexResult implements Serializable {

    /**
     * 多租户token
     */
    private String tenantId;
    /**
     * 广告列表
     */
    private List<CarServiceAd> banner;
    /**
     * 分类列表
     */
    private List<CarServiceCategory> channel;
    /**
     * 优惠券列表
     */
    private List<CarServiceCoupon> couponList;
    /**
     * 新品商品
     */
    private List<GoodsListBodyB> newGoodsList;
    /**
     * 热卖商品
     */
    private List<GoodsListBodyB> hotGoodsList;
    /**
     * 所有商品第一页
     */
    private List<GoodsListBodyB> allGoodsList;
    /**
     * 赏金规则列表
     */
    private List<CarServiceRewardTask> rewardTaskList;
    /**
     * 团购规则列表
     */
    private List<CarServiceGrouponRules> grouponRuleList;

}
