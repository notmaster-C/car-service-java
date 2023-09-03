package org.click.carservice.admin.service;
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
import org.click.carservice.admin.model.comment.body.CommentListBody;
import org.click.carservice.db.domain.CarServiceGoodsComment;
import org.click.carservice.db.service.impl.GoodsCommentServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品评论服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_goods_comment")
public class AdminGoodsCommentService extends GoodsCommentServiceImpl {


    //@Cacheable(sync = true)
    public List<CarServiceGoodsComment> querySelective(CommentListBody body) {
        QueryWrapper<CarServiceGoodsComment> wrapper = startPage(body);
        if (StringUtils.hasText(body.getUserId())) {
            wrapper.eq(CarServiceGoodsComment.USER_ID, body.getUserId());
        }
        if (StringUtils.hasText(body.getGoodsId())) {
            wrapper.eq(CarServiceGoodsComment.GOODS_ID, body.getGoodsId());
        }
        return queryAll(wrapper);
    }


}
