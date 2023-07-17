package org.click.carservice.wx.web.impl;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceGoods;
import org.click.carservice.db.domain.CarServiceTopic;
import org.click.carservice.db.enums.CollectType;
import org.click.carservice.db.enums.LikeType;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.topic.body.TopicListBody;
import org.click.carservice.wx.model.topic.result.TopicDetailResult;
import org.click.carservice.wx.service.WxCollectService;
import org.click.carservice.wx.service.WxGoodsService;
import org.click.carservice.wx.service.WxLikeService;
import org.click.carservice.wx.service.WxTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 专题服务
 * @author click
 */
@Slf4j
@Service
public class WxWebTopicService {

    @Autowired
    private WxTopicService topicService;
    @Autowired
    private WxGoodsService goodsService;
	@Autowired
	private WxCollectService collectService;
    @Autowired
    private WxLikeService likeService;

    /**
     * 专题列表
     */
    public Object list(TopicListBody body) {
        return ResponseUtil.okList(topicService.queryList(body));
    }

    /**
     * 专题详情
     * @param id 专题ID
     * @return 专题详情
     */
    public Object detail(@LoginUser String userId, @NotNull String id) {
        CarServiceTopic topic = topicService.findById(id);
        List<CarServiceGoods> goods = new ArrayList<>();
        for (String i : topic.getGoodsIds()) {
            CarServiceGoods good = goodsService.findByIdVO(i);
            if (null != good) {
                goods.add(good);
            }
        }
        TopicDetailResult result = new TopicDetailResult();
        result.setTopic(topic);
        result.setGoods(goods);
        result.setTopicLike(likeService.count(LikeType.TYPE_TOPIC, topic.getId(), userId));
        result.setUserHasCollect(collectService.count(userId, CollectType.TYPE_TOPIC, id));
        return ResponseUtil.ok(result);
    }

    /**
     * 相关专题
     * @param id 专题ID
     * @return 相关专题
     */
    public Object related(@NotNull String id) {
        return ResponseUtil.okList(topicService.queryRelatedList(id, 4));
    }

}