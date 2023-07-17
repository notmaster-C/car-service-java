package org.click.carservice.wx.model.home.result;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.click.carservice.db.domain.*;

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
    private List<CarServiceGoods> newGoodsList;
    /**
     * 热卖商品
     */
    private List<CarServiceGoods> hotGoodsList;
    /**
     * 所有商品第一页
     */
    private PageInfo<CarServiceGoods> allGoodsList;
    /**
     * 赏金规则列表
     */
    private List<CarServiceRewardTask> rewardTaskList;
    /**
     * 团购规则列表
     */
    private List<CarServiceGrouponRules> grouponRuleList;

}
