package org.click.carservice.wx.model.goods.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class GoodsCommentCountResult implements Serializable {

    /**
     * 全部评论
     */
    private int allCount;
    /**
     * 有图评论
     */
    private int hasPicCount;

}
