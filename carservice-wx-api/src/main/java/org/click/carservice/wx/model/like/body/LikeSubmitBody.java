package org.click.carservice.wx.model.like.body;

import lombok.Data;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class LikeSubmitBody implements Serializable {

    /**
     * 点赞类型
     */
    private Short likeType;

    /**
     * 点赞ID
     */
    private String valueId;

}
