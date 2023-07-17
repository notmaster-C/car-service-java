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
import org.click.carservice.core.redis.annotation.RequestRateLimiter;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.*;
import org.click.carservice.db.enums.LikeType;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.like.body.LikeSubmitBody;
import org.click.carservice.wx.service.*;
import org.redisson.api.RateIntervalUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户点赞服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/like")
@Validated
public class WxLikeController {

    @Autowired
    private WxTopicService topicService;
    @Autowired
    private WxLikeService likeService;
    @Autowired
    private WxBrandService brandService;
    @Autowired
    private WxDynamicService dynamicService;
    @Autowired
    private WxCommentService commentService;


    /**
     * 点赞
     *
     * @param userId 用户ID
     * @param body   评论信息
     * @return 提交订单操作结果
     */
    @PostMapping("submit")
    @RequestRateLimiter(rate = 5, rateInterval = 10, timeUnit = RateIntervalUnit.SECONDS)
    public Object submit(@LoginUser String userId, @Valid @RequestBody LikeSubmitBody body) {
        Short likeType = body.getLikeType();
        String valueId = body.getValueId();

        if (LikeType.parseValue(likeType) == null) {
            return ResponseUtil.fail("类型不支持");
        }

        carserviceLike like = likeService.query(likeType, valueId, userId);
        if (like != null) {
            like.setCancel(!like.getCancel());
            likeService.updateSelective(like);
        } else {
            like = new carserviceLike();
            like.setUserId(userId);
            like.setType(likeType);
            like.setValueId(valueId);
            like.setCancel(false);
            likeService.add(like);
        }

        if (LikeType.TYPE_TOPIC.getStatus().equals(likeType)) {
            carserviceTopic topic = topicService.findById(valueId);
            if (like.getCancel()) {
                topic.setLikeCount(topic.getLikeCount() > 0 ? topic.getLikeCount() - 1 : 0);
            } else {
                topic.setLikeCount(topic.getLikeCount() + 1);
            }
            if (topicService.updateVersionSelective(topic) == 0) {
                throw new RuntimeException("系统繁忙请稍后重试");
            }
        }
        if (LikeType.TYPE_BRAND.getStatus().equals(likeType)) {
            carserviceBrand brand = brandService.findById(valueId);
            if (like.getCancel()) {
                brand.setLikeCount(brand.getLikeCount() > 0 ? brand.getLikeCount() - 1 : 0);
            } else {
                brand.setLikeCount(brand.getLikeCount() + 1);
            }
            if (brandService.updateVersionSelective(brand) == 0) {
                throw new RuntimeException("系统繁忙请稍后重试");
            }
        }
        if (LikeType.TYPE_TIMELINE.getStatus().equals(likeType)) {
            carserviceDynamic dynamic = dynamicService.findById(valueId);
            if (like.getCancel()) {
                dynamic.setLikeCount(dynamic.getLikeCount() > 0 ? dynamic.getLikeCount() - 1 : 0);
            } else {
                dynamic.setLikeCount(dynamic.getLikeCount() + 1);
            }
            if (dynamicService.updateVersionSelective(dynamic) == 0) {
                throw new RuntimeException("系统繁忙请稍后重试");
            }
        }
        if (LikeType.TYPE_COMMENT.getStatus().equals(likeType)) {
            carserviceComment comment = commentService.findById(valueId);
            if (like.getCancel()) {
                comment.setLikeCount(comment.getLikeCount() > 0 ? comment.getLikeCount() - 1 : 0);
            } else {
                comment.setLikeCount(comment.getLikeCount() + 1);
            }
            if (commentService.updateVersionSelective(comment) == 0) {
                throw new RuntimeException("系统繁忙请稍后重试");
            }
        }
        return ResponseUtil.ok();
    }
}