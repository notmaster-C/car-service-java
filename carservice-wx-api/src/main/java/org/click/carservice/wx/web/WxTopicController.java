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
import org.click.carservice.wx.model.topic.body.TopicListBody;
import org.click.carservice.wx.web.impl.WxWebTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;

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
    private WxWebTopicService topicService;

    /**
     * 专题列表
     */
    @GetMapping("list")
    public Object list(TopicListBody body) {
        return topicService.list(body);
    }

    /**
     * 专题详情
     * @param id 专题ID
     * @return 专题详情
     */
    @GetMapping("detail")
    public Object detail(@LoginUser String userId, @NotNull String id) {
        return topicService.detail(userId , id);
    }

    /**
     * 相关专题
     * @param id 专题ID
     * @return 相关专题
     */
    @GetMapping("related")
    public Object related(@NotNull String id) {
        return topicService.related(id);
    }

}