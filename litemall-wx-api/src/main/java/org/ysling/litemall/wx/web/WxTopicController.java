package org.ysling.litemall.wx.web;
/**
 *  Copyright (c) [ysling] [927069313@qq.com]
 *  [litemall-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.enums.CollectType;
import org.ysling.litemall.db.enums.LikeType;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.db.domain.LitemallGoods;
import org.ysling.litemall.db.domain.LitemallTopic;
import org.ysling.litemall.wx.service.WxCollectService;
import org.ysling.litemall.wx.service.WxGoodsService;
import org.ysling.litemall.wx.service.WxLikeService;
import org.ysling.litemall.wx.service.WxTopicService;
import org.ysling.litemall.wx.model.topic.body.TopicListBody;
import org.ysling.litemall.wx.model.topic.result.TopicDetailResult;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 专题服务
 * @author Ysling
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
        LitemallTopic topic = topicService.findById(id);
        List<LitemallGoods> goods = new ArrayList<>();
        for (String i : topic.getGoodsIds()) {
            LitemallGoods good = goodsService.findByIdVO(i);
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