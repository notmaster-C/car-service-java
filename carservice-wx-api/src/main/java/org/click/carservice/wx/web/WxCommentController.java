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
import org.click.carservice.core.weixin.service.WxSecCheckService;
import org.click.carservice.db.domain.*;
import org.click.carservice.db.enums.CommentType;
import org.click.carservice.db.enums.LikeType;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.comment.body.CommentListBody;
import org.click.carservice.wx.model.comment.body.CommentReplyListBody;
import org.click.carservice.wx.model.comment.body.CommentSubmitBody;
import org.click.carservice.wx.model.comment.result.CommentListResult;
import org.click.carservice.wx.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 用户评论服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/comment")
@Validated
public class WxCommentController {

    @Autowired
    private WxCommentService commentService;
    @Autowired
    private WxUserService userService;
    @Autowired
    private WxTopicService topicService;
    @Autowired
    private WxBrandService brandService;
    @Autowired
    private WxDynamicService dynamicService;
    @Autowired
    private WxLikeService likeService;
    @Autowired
    private WxSecCheckService secCheckService;


    /**
     * 评论列表
     */
    @GetMapping("list")
    public Object list(@LoginUser String userId, CommentListBody body) {
        List<CarServiceComment> commentList = commentService.querySelective(body);
        List<CommentListResult> commentVoList = new ArrayList<>(commentList.size());
        for (CarServiceComment comment : commentList) {
            CommentListResult result = new CommentListResult();
            result.setCommentId(comment.getId());
            result.setAddTime(comment.getAddTime());
            result.setContent(comment.getContent());
            result.setPicList(comment.getPicUrls());
            result.setUserId(comment.getUserId());
            result.setValueId(comment.getValueId());
            result.setLikeCount(comment.getLikeCount());
            result.setShowReply(false);
            result.setCommentLike(likeService.count(LikeType.TYPE_COMMENT, comment.getId(), userId));
            result.setReplyId(comment.getReplyId());
            result.setCommentType(comment.getType());
            result.setReplyCount(commentService.replyCount(comment.getId()));
            result.setUserInfo(userService.findUserVoById(comment.getUserId()));
            //子评论
            CommentReplyListBody listBody = new CommentReplyListBody(comment.getId());
            result.setReplyList(replyList(userId, listBody));
            commentVoList.add(result);
        }
        return ResponseUtil.okList(commentVoList, commentList);
    }

    /**
     * 回复评论列表
     */
    @GetMapping("reply-list")
    public Object replyList(@LoginUser String userId, CommentReplyListBody body) {
        List<CarServiceComment> commentList = commentService.queryReplySelective(body);
        List<CommentListResult> commentVoList = new ArrayList<>(commentList.size());
        for (CarServiceComment comment : commentList) {
            CommentListResult result = new CommentListResult();
            result.setCommentId(comment.getId());
            result.setAddTime(comment.getAddTime());
            result.setCommentType(comment.getType());
            result.setContent(comment.getContent());
            result.setPicList(comment.getPicUrls());
            result.setUserId(comment.getUserId());
            result.setCommentLike(likeService.count(LikeType.TYPE_COMMENT, comment.getId(), userId));
            result.setValueId(comment.getValueId());
            result.setLikeCount(comment.getLikeCount());
            result.setReplyId(comment.getReplyId());
            result.setReplyUserInfo(userService.findUserVoById(comment.getReplyUserId()));
            result.setReplyCount(commentService.replyCount(comment.getId()));
            result.setUserInfo(userService.findUserVoById(comment.getUserId()));
            commentVoList.add(result);
        }
        return ResponseUtil.okList(commentVoList, commentList);
    }


    /**
     * 评论数量
     * @param commentType      评论类型。
     * @param valueId   类型ID。
     * @return 评论数量
     */
    @GetMapping("count")
    public Object count(@NotNull Short commentType, @NotNull String valueId) {
        Map<String, Object> entity = new HashMap<>();
        entity.put("commentCount", commentService.count(commentType, valueId));
        return ResponseUtil.ok(entity);
    }


    /**
     * 评论回复
     *
     * @param userId 用户ID
     * @param body   评论信息
     * @return 提交订单操作结果
     */
    @PostMapping("submit")
    public Object submit(@LoginUser String userId, @Valid @RequestBody CommentSubmitBody body) {
        String content = body.getContent();
        Short commentType = body.getCommentType();
        String valueId = body.getValueId();
        String commentId = body.getCommentId();
        String replyUserId = body.getReplyUserId();
        String[] commentImage = body.getCommentImage();

        if (commentImage == null || commentImage.length <= 0) {
            if (!StringUtils.hasText(content)) {
                return ResponseUtil.fail("评论内容不能为空");
            }
        }
        if (commentType == null || valueId == null) {
            return ResponseUtil.badArgument();
        }
        if (CommentType.parseValue(commentType) == null) {
            return ResponseUtil.fail("评论类型不支持");
        }
        CarServiceUser user = userService.findById(userId);
        secCheckService.checkMessage(user.getOpenid(), content);
        //整合信息
        CarServiceComment comment = new CarServiceComment();
        comment.setContent(content);
        comment.setType(commentType);
        comment.setValueId(valueId);
        comment.setPicUrls(commentImage);
        comment.setReplyId(Objects.equals(commentId, "0") ? null : commentId);
        comment.setReplyUserId(Objects.equals(replyUserId, "0") ? null : replyUserId);
        comment.setLikeCount((long) 0);
        comment.setUserId(userId);
        if (commentService.add(comment) == 0) {
            return ResponseUtil.fail("评论失败请重试");
        }

        if (CommentType.TYPE_TOPIC.getStatus().equals(commentType)) {
            CarServiceTopic topic = topicService.findById(valueId);
            if (topic != null) {
                Integer count = commentService.count(commentType, valueId);
                topic.setCommentCount((long) count);
                topicService.updateSelective(topic);
            }
        }

        if (CommentType.TYPE_BRAND.getStatus().equals(commentType)) {
            CarServiceBrand brand = brandService.findById(valueId);
            if (brand != null) {
                Integer count = commentService.count(commentType, valueId);
                brand.setCommentCount((long) count);
                brandService.updateSelective(brand);
            }
        }

        if (CommentType.TYPE_TIMELINE.getStatus().equals(commentType)) {
            CarServiceDynamic dynamic = dynamicService.findById(valueId);
            if (dynamic != null) {
                Integer count = commentService.count(commentType, valueId);
                dynamic.setCommentCount((long) count);
                dynamicService.updateSelective(dynamic);
            }
        }
        return ResponseUtil.ok();
    }
}