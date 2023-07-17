package org.click.carservice.wx.model.reward.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceReward;
import org.click.carservice.db.domain.CarServiceRewardTask;

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
    private CarServiceReward reward;

    /**
     * 赏金规则信息
     */
    private CarServiceRewardTask rewardTask;

}
