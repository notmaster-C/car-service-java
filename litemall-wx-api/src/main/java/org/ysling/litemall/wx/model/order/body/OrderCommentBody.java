package org.ysling.litemall.wx.model.order.body;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Ysling
 */
@Data
public class OrderCommentBody implements Serializable {

    /**
     * 订单商品ID
     */
    @NotNull(message = "订单商品ID不能为空")
    private String goodsId;
    /**
     * 评论信息
     */
    @NotNull(message = "评论信息不能为空")
    private String content;
    /**
     * 评分
     */
    @NotNull(message = "评分不能为空")
    private Integer star;
    /**
     * 是否有图
     */
    private Boolean hasPicture;
    /**
     * 图片列表
     */
    private List<String> picUrls;

}
