package org.ysling.litemall.admin.model.order.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 回复订单商品
 * @author Ysling
 */
@Data
public class OrderReplyBody implements Serializable {

    /**
     * 评论ID
     */
    @NotNull(message = "评论ID不能为空")
    private String commentId;
    /**
     * 回复内容
     */
    @NotNull(message = "回复内容不能为空")
    private String content;

}
