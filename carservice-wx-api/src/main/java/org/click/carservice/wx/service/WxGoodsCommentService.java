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
import org.click.carservice.db.domain.CarServiceGoodsComment;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.entity.UserInfo;
import org.click.carservice.db.service.impl.GoodsCommentServiceImpl;
import org.click.carservice.wx.model.goods.body.GoodsCommentListBody;
import org.click.carservice.wx.model.goods.result.GoodsCommentInfo;
import org.click.carservice.wx.model.goods.result.GoodsCommentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品评论服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_goods_comment")
public class WxGoodsCommentService extends GoodsCommentServiceImpl {


    @Autowired
    private WxUserService userService;

    /**
     * 获取评论信息
     * @param goodsId 商品id
     * @param page 页数
     * @param limit  条数
     */
    public GoodsCommentResult getComments(String goodsId, int page, int limit) {
        // 评论
        List<CarServiceGoodsComment> comments = queryCommentList(goodsId, page, limit);
        List<GoodsCommentInfo> commentsVo = new ArrayList<>(comments.size());
        int commentCount = count(goodsId, false);
        for (CarServiceGoodsComment comment : comments) {
            GoodsCommentInfo commentInfo = new GoodsCommentInfo();
            commentInfo.setGoodId(comment.getGoodsId());
            commentInfo.setAddTime(comment.getAddTime());
            commentInfo.setContent(comment.getContent());
            commentInfo.setAdminContent(comment.getAdminContent());
            UserInfo user = userService.findUserVoById(comment.getUserId());
            commentInfo.setNickName(user.getNickName());
            commentInfo.setAvatarUrl(user.getAvatarUrl());
            commentInfo.setPicList(comment.getPicUrls());
            commentsVo.add(commentInfo);
        }
        GoodsCommentResult result = new GoodsCommentResult();
        result.setCount(commentCount);
        result.setData(commentsVo);
        return result;
    }


    //@Cacheable(sync = true)
    public List<CarServiceGoodsComment> querySelective(GoodsCommentListBody body) {
        QueryWrapper<CarServiceGoodsComment> wrapper = startPage(body);
        wrapper.eq(CarServiceGoodsComment.GOODS_ID, body.getGoodsId());
        if (body.getHasPicture() != null && body.getHasPicture()) {
            wrapper.eq(CarServiceGoodsComment.HAS_PICTURE, true);
        }
        return queryAll(wrapper);
    }


    //@Cacheable(sync = true)
    public List<CarServiceGoodsComment> queryCommentList(String goodsId, Integer page, Integer limit) {
        QueryWrapper<CarServiceGoodsComment> wrapper = startPage(new PageBody(page, limit));
        wrapper.eq(CarServiceGoodsComment.GOODS_ID, goodsId);
        return queryAll(wrapper);
    }


    //@Cacheable(sync = true)
    public Integer count(String goodsId, Boolean hasPicture) {
        QueryWrapper<CarServiceGoodsComment> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceGoodsComment.GOODS_ID, goodsId);
        if (hasPicture) {
            wrapper.eq(CarServiceGoodsComment.HAS_PICTURE, true);
        }
        return Math.toIntExact(count(wrapper));
    }

}
