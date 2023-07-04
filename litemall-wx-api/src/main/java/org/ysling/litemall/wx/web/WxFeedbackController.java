package org.ysling.litemall.wx.web;
/**
 *  Copyright (c) [ysling] [927069313@qq.com]
 *  [litemall-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.ysling.litemall.core.utils.RegexUtil;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.domain.LitemallFeedback;
import org.ysling.litemall.db.domain.LitemallUser;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ysling.litemall.wx.service.WxFeedbackService;
import org.ysling.litemall.wx.service.WxUserService;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 意见反馈服务
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/wx/feedback")
@Validated
public class WxFeedbackController {

    @Autowired
    private WxUserService userService;
    @Autowired
    private WxFeedbackService feedbackService;


    private Object validate(LitemallFeedback feedback) {
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
    @PostMapping("submit")
    public Object submit(@LoginUser String userId, @Valid @RequestBody LitemallFeedback feedback) {
        Object error = validate(feedback);
        if (error != null) {
            return error;
        }
        LitemallUser user = userService.findById(userId);
        //状态默认是0，1表示状态已发生变化
        feedback.setStatus(true);
        feedback.setUserId(userId);
        feedback.setUsername(user.getUsername());
        feedbackService.add(feedback);
        return ResponseUtil.ok();
    }

}
