package org.ysling.litemall.wx.model.user.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ysling.litemall.db.entity.PageBody;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ysling
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TradingRecordListBody extends PageBody implements Serializable {

    /**
     * 类型，如果是0则是商品收藏，如果是1则是专题收藏
     */
    @NotNull(message = "类型不能为空")
    private Byte type;

}
