package org.click.carservice.wx.model.goods.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class GoodsCommentResult implements Serializable {

    /**
     * 评论数量
     */
    private int count;

    /**
     * 评论信息
     */
    private List<GoodsCommentInfo> data;

}
