package org.click.carservice.wx.model.reward.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageResult;

import java.math.BigDecimal;

/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RewardListResult<T> extends PageResult<T> {

    /**
     * 收益
     */
    private BigDecimal earnings;
    /**
     * 余额
     */
    private BigDecimal balance;

}
