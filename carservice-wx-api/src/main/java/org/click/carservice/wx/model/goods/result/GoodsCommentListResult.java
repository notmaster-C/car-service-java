package org.click.carservice.wx.model.goods.result;

import lombok.Data;
import org.click.carservice.db.entity.UserInfo;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class GoodsCommentListResult implements Serializable {

    /**
     * 评论表ID
     */
    private String id;

    /**
     * 用户表的用户ID
     */
    private String userId;

    /**
     * 则是商品id
     */
    private String goodsId;

    /**
     * 评分， 1-5
     */
    private Short star;

    /**
     * 图片地址列表，采用JSON数组格式
     */
    private String[] picUrls;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 管理员回复内容
     */
    private String adminContent;

    /**
     * 是否含有图片
     */
    private Boolean hasPicture;

    /**
     * 用户信息
     */
    private UserInfo userInfo;
}
