package org.click.carservice.wx.model.topic.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceGoods;
import org.click.carservice.db.domain.CarServiceTopic;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class TopicDetailResult implements Serializable {

    /**
     * 专题信息
     */
    private CarServiceTopic topic;

    /**
     * 商品列表
     */
    private List<CarServiceGoods> goods;

    /**
     * 是否点赞
     */
    private Boolean topicLike;

    /**
     * 是否收藏
     */
    private Boolean userHasCollect;

}
