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
import org.click.carservice.core.annotation.JsonBody;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.message.body.HistoryMessageBody;
import org.click.carservice.wx.web.impl.WxWebMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户聊天列表
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/wx/message")
@Validated
public class WxMessageController {


    @Autowired
    private WxWebMessageService messageService;

    /**
     * 获取聊天记录
     * @param userId 用户ID
     * @return 聊天记录
     */
    @GetMapping("list")
    public Object getMessageList(@LoginUser String userId){
        return messageService.getMessageList(userId);
    }

    /**
     * 获取历史聊天记录
     * @param userId 用户ID
     * @return 聊天记录
     */
    @GetMapping("history")
    public Object getHistoryMessageList(@LoginUser String userId, HistoryMessageBody body){
        return messageService.getHistoryMessageList(userId , body);
    }

    /**
     * 删除聊天记录
     * @param userId 用户ID
     * @param sendUserId 发送用户
     * @return 成功
     */
    @PostMapping("delete")
    public Object delete(@LoginUser String userId, @JsonBody String sendUserId){
        return messageService.delete(userId , sendUserId);
    }

}
