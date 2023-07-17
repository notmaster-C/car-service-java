package org.click.carservice.wx.model.goods.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsCommentListBody extends PageBody implements Serializable {

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private String goodsId;
    /**
     * 是否只查看有图
     */
    @NotNull(message = "是否只查看有图不能为空")
    private Boolean hasPicture;

}
