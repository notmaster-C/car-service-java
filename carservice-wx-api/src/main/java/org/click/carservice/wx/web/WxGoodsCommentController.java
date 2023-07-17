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

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceGoodsComment;
import org.click.carservice.wx.model.goods.body.GoodsCommentListBody;
import org.click.carservice.wx.model.goods.result.GoodsCommentCountResult;
import org.click.carservice.wx.model.goods.result.GoodsCommentListResult;
import org.click.carservice.wx.service.WxGoodsCommentService;
import org.click.carservice.wx.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户商品评论服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/goods/comment")
@Validated
public class WxGoodsCommentController {


    @Autowired
    private WxUserService userService;
    @Autowired
    private WxGoodsCommentService goodsCommentService;


    /**
     * 评论数量
     * @param goodsId 商品ID。
     * @return 评论数量
     */
    @GetMapping("count")
    public Object count(@NotNull String goodsId) {
        GoodsCommentCountResult result = new GoodsCommentCountResult();
        result.setAllCount(goodsCommentService.count(goodsId, false));
        result.setHasPicCount(goodsCommentService.count(goodsId, true));
        return ResponseUtil.ok(result);
    }

    /**
     * 评论列表
     */
    @GetMapping("list")
    public Object list(GoodsCommentListBody body) {
        List<carserviceGoodsComment> commentList = goodsCommentService.querySelective(body);
        List<GoodsCommentListResult> commentVoList = new ArrayList<>(commentList.size());
        for (carserviceGoodsComment comment : commentList) {
            GoodsCommentListResult result = new GoodsCommentListResult();
            BeanUtil.copyProperties(comment, result);
            result.setUserInfo(userService.findUserVoById(comment.getUserId()));
            commentVoList.add(result);
        }
        return ResponseUtil.okList(commentVoList, commentList);
    }
}