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
    private List<carserviceAd> banner;
    /**
     * 分类列表
     */
    private List<carserviceCategory> channel;
    /**
     * 优惠券列表
     */
    private List<carserviceCoupon> couponList;
    /**
     * 新品商品
     */
    private List<carserviceGoods> newGoodsList;
    /**
     * 热卖商品
     */
    private List<carserviceGoods> hotGoodsList;
    /**
     * 所有商品第一页
     */
    private PageInfo<carserviceGoods> allGoodsList;
    /**
     * 赏金规则列表
     */
    private List<carserviceRewardTask> rewardTaskList;
    /**
     * 团购规则列表
     */
    private List<carserviceGrouponRules> grouponRuleList;

}
