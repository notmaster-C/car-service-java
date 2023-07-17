package org.click.carservice.admin.model.topic.result;

import lombok.Data;
import org.click.carservice.db.domain.carserviceGoods;
import org.click.carservice.db.domain.carserviceTopic;

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
    private carserviceTopic topic;

    /**
     * 商品列表
     */
    private List<carserviceGoods> goodsList;

}
