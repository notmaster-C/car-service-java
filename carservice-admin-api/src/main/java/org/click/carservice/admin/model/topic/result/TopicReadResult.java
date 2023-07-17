package org.click.carservice.admin.model.topic.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceGoods;
import org.click.carservice.db.domain.CarServiceTopic;

import java.io.Serializable;
import java.util.List;

/**
 * 专题详情
 *
 * @author click
 */
@Data
public class TopicReadResult implements Serializable {

    /**
     * 专题信息
     */
    private CarServiceTopic topic;

    /**
     * 商品列表
     */
    private List<CarServiceGoods> goodsList;

}
