package org.click.carservice.wx.model.reward.result;

import lombok.Data;
import org.click.carservice.db.domain.carserviceReward;
import org.click.carservice.db.domain.carserviceRewardTask;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class RewardJoinResult implements Serializable {

    /**
     * 商品ID
     */
    private String goodId;
    /**
     * 赏金参与信息
     */
    private carserviceReward reward;

    /**
     * 赏金规则信息
     */
    private carserviceRewardTask rewardTask;

}
