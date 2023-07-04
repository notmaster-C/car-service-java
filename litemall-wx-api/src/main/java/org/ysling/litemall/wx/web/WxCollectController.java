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
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.domain.LitemallCollect;
import org.ysling.litemall.db.domain.LitemallGoods;
import org.ysling.litemall.db.domain.LitemallTopic;
import org.ysling.litemall.db.enums.CollectType;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ysling.litemall.wx.service.WxCollectService;
import org.ysling.litemall.wx.service.WxGoodsService;
import org.ysling.litemall.wx.service.WxTopicService;
import org.ysling.litemall.wx.model.collect.body.CollectListBody;
import org.ysling.litemall.wx.model.collect.body.CollectUpdateBody;

import javax.validation.Valid;

/**
 * 用户收藏服务
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/wx/collect")
@Validated
public class WxCollectController {

    @Autowired
    private WxCollectService collectService;
    @Autowired
    private WxGoodsService goodsService;
    @Autowired
    private WxTopicService topicService;

    /**
     * 用户收藏列表
     */
    @GetMapping("list")
    public Object list(@LoginUser String userId, CollectListBody body) {
        return ResponseUtil.okList(collectService.queryByType(userId, body));
    }

    /**
     * 用户收藏添加或删除
     * <p>
     * 如果商品没有收藏，则添加收藏；如果商品已经收藏，则删除收藏状态。
     *
     * @param userId 用户ID
     * @param body   请求内容，{ type: xxx, valueId: xxx }
     * @return 操作结果 update
     */
    @PostMapping("update")
    public Object addOrDelete(@LoginUser String userId, @Valid @RequestBody CollectUpdateBody body) {
        LitemallCollect collect = collectService.queryByTypeAndValue(userId, body.getType(), body.getValueId());
        if (collect != null) {
            collect.setCancel(!collect.getCancel());
            collectService.updateSelective(collect);
        } else {
            collect = new LitemallCollect();
            if (Objects.equals(body.getType() , CollectType.TYPE_GOODS.getStatus())){
                //商品收藏
                LitemallGoods goods = goodsService.findById(body.getValueId());
                if (goods == null) {
                    return ResponseUtil.badArgument();
                }
                collect.setPicUrl(goods.getPicUrl());
                collect.setName(goods.getName());
                collect.setBrief(goods.getBrief());
                collect.setPrice(goods.getRetailPrice());
            } else if (Objects.equals(body.getType() , CollectType.TYPE_TOPIC.getStatus())){
                //专题收藏
                LitemallTopic topic = topicService.findById(body.getValueId());
                if (topic == null) {
                    return ResponseUtil.badArgument();
                }
                collect.setPicUrl(topic.getPicUrl());
                collect.setName(topic.getTitle());
                collect.setBrief(topic.getSubtitle());
                collect.setPrice(topic.getPrice());
            } else {
                return ResponseUtil.badArgument();
            }
            collect.setUserId(userId);
            collect.setValueId(body.getValueId());
            collect.setType(body.getType());
            collect.setCancel(false);
            collectService.add(collect);
        }
        return ResponseUtil.ok();
    }


}