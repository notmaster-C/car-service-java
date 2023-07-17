package org.click.carservice.wx.model.goods.result;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author click
 */
@Data
public class GoodsCommentInfo implements Serializable {

    /**
     * 商品ID
     */
    private String goodId;
    /**
     * 添加日期
     */
    private LocalDateTime addTime;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 商家回复
     */
    private String adminContent;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 评论图片
     */
    private String[] picList;

}
