package org.click.carservice.wx.model.reward.result;

import lombok.Data;
import org.click.carservice.db.domain.carserviceRewardTask;
import org.click.carservice.db.entity.UserInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author click
 */
@Data
public class RewardListInfo implements Serializable {

    /**
     * 赏金规则
     */
    private carserviceRewardTask rewardTask;

    /**
     * 完成百分比
     */
    private String percentage;

    /**
     * 参与数量
     */
    private Long total;

    /**
     * 参与用户
     */
    private ArrayList<UserInfo> joiners;

}
