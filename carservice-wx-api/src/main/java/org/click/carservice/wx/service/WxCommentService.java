package org.click.carservice.wx.service;
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.CarServiceComment;
import org.click.carservice.db.service.impl.CommentServiceImpl;
import org.click.carservice.wx.model.comment.body.CommentListBody;
import org.click.carservice.wx.model.comment.body.CommentReplyListBody;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_comment")
public class WxCommentService extends CommentServiceImpl {


    @Cacheable(sync = true)
    public Integer count(Short type, String valueId) {
        QueryWrapper<CarServiceComment> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceComment.TYPE, type);
        wrapper.eq(CarServiceComment.VALUE_ID, valueId);
        return Math.toIntExact(count(wrapper));
    }


    @Cacheable(sync = true)
    public Integer replyCount(String commentId) {
        QueryWrapper<CarServiceComment> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceComment.REPLY_ID, commentId);
        return Math.toIntExact(count(wrapper));
    }


    @Cacheable(sync = true)
    public List<CarServiceComment> queryReplySelective(CommentReplyListBody body) {
        QueryWrapper<CarServiceComment> wrapper = startPage(body);
        wrapper.eq(CarServiceComment.REPLY_ID, body.getCommentId());
        wrapper.orderByDesc(CarServiceComment.LIKE_COUNT);
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<CarServiceComment> querySelective(CommentListBody body) {
        QueryWrapper<CarServiceComment> wrapper = startPage(body);
        wrapper.eq(CarServiceComment.TYPE, body.getCommentType());
        wrapper.eq(CarServiceComment.VALUE_ID, body.getValueId());
        wrapper.isNull(CarServiceComment.REPLY_ID);
        return queryAll(wrapper);
    }


}
