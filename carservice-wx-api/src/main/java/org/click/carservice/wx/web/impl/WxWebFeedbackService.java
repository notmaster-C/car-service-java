package org.click.carservice.wx.web.impl;
/**
 *  Copyright (c) [click] [927069313@qq.com]
 *  [CarService-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.RegexUtil;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceFeedback;
import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.wx.service.WxFeedbackService;
import org.click.carservice.wx.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 意见反馈服务
 * @author click
 */
@Slf4j
@Service
public class WxWebFeedbackService {

    @Autowired
    private WxUserService userService;
    @Autowired
    private WxFeedbackService feedbackService;


    private Object validate(CarServiceFeedback feedback) {
        String content = feedback.getContent();
        if (Objects.isNull(content)) {
            return ResponseUtil.badArgument();
        }
        String type = feedback.getFeedType();
        if (Objects.isNull(type)) {
            return ResponseUtil.badArgument();
        }
        Boolean hasPicture = feedback.getHasPicture();
        if (hasPicture == null || !hasPicture) {
            feedback.setPicUrls(new String[0]);
        }
        // 测试手机号码是否正确
        String mobile = feedback.getMobile();
        if (Objects.isNull(mobile)) {
            return ResponseUtil.fail("请输入手机号");
        }
        if (!RegexUtil.isMobileSimple(mobile)) {
            return ResponseUtil.fail("手机号不正确");
        }
        return null;
    }

    /**
     * 添加意见反馈
     *
     * @param userId   用户ID
     * @param feedback 意见反馈
     * @return 操作结果
     */
    public Object submit(String userId, CarServiceFeedback feedback) {
        Object error = validate(feedback);
        if (error != null) {
            return error;
        }
        CarServiceUser user = userService.findById(userId);
        //状态默认是0，1表示状态已发生变化
        feedback.setStatus(true);
        feedback.setUserId(userId);
        feedback.setUsername(user.getUsername());
        feedbackService.add(feedback);
        return ResponseUtil.ok();
    }

}
