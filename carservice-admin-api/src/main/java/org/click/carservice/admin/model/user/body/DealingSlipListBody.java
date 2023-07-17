package org.click.carservice.admin.model.user.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * 交易记录列表查询
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DealingSlipListBody extends PageBody implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 交易类型
     */
    private Short dealType;

}
