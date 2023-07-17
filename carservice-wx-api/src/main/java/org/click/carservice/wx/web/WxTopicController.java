package org.click.carservice.wx.web;
/**
 * Copyright (c) [click] [927069313@qq.com]
 * [carservice-plus] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceGoods;
import org.click.carservice.db.domain.carserviceTopic;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 专题服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/topic")
@Validated
public class WxTopicController {

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
    @GetMapping("list")
    public Object list(TopicListBody body) {
        return ResponseUtil.okList(topicService.queryList(body));
    }

    /**
     * 专题详情
     * @param id 专题ID
     * @return 专题详情
     */
    @GetMapping("detail")
    public Object detail(@LoginUser String userId, @NotNull String id) {
        carserviceTopic topic = topicService.findById(id);
        List<carserviceGoods> goods = new ArrayList<>();
        for (String i : topic.getGoodsIds()) {
            carserviceGoods good = goodsService.findByIdVO(i);
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
    @GetMapping("related")
    public Object related(@NotNull String id) {
        return ResponseUtil.okList(topicService.queryRelatedList(id, 4));
    }

}