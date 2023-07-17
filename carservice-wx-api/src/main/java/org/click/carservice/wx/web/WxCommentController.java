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
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.comment.body.CommentListBody;
import org.click.carservice.wx.model.comment.body.CommentReplyListBody;
import org.click.carservice.wx.model.comment.body.CommentSubmitBody;
import org.click.carservice.wx.web.impl.WxWebCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    private WxWebCommentService commentService;


    /**
     * 评论列表
     */
    @GetMapping("list")
    public Object list(@LoginUser String userId, CommentListBody body) {
        return commentService.list(userId , body);
    }

    /**
     * 回复评论列表
     */
    @GetMapping("reply-list")
    public Object replyList(@LoginUser String userId, CommentReplyListBody body) {
        return commentService.replyList(userId , body);
    }


    /**
     * 评论数量
     * @param commentType      评论类型。
     * @param valueId   类型ID。
     * @return 评论数量
     */
    @GetMapping("count")
    public Object count(@NotNull Short commentType , @NotNull String valueId) {
        return commentService.count(commentType , valueId);
    }


    /**
     * 评论回复
     * @param userId 用户ID
     * @param body   评论信息
     * @return 提交订单操作结果
     */
    @PostMapping("submit")
    public Object submit(@LoginUser String userId, @Valid @RequestBody CommentSubmitBody body) {
        return commentService.submit(userId , body);
    }


}