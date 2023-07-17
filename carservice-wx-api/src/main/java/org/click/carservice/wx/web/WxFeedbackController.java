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

import org.click.carservice.db.domain.CarServiceFeedback;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.web.impl.WxWebFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 意见反馈服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/feedback")
@Validated
public class WxFeedbackController {


    @Autowired
    private WxWebFeedbackService feedbackService;


    /**
     * 添加意见反馈
     * @param userId   用户ID
     * @param feedback 意见反馈
     * @return 操作结果
     */
    @PostMapping("submit")
    public Object submit(@LoginUser String userId, @Valid @RequestBody CarServiceFeedback feedback) {
        return feedbackService.submit(userId , feedback);
    }

}
