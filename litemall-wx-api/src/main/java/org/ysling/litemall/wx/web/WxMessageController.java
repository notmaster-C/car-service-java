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
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.core.annotation.JsonBody;
import org.ysling.litemall.core.utils.chatgpt.ChatGPTClient;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.domain.LitemallMessage;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.ysling.litemall.wx.service.WxMessageService;
import org.ysling.litemall.wx.model.message.body.HistoryMessageBody;
import java.time.LocalDateTime;
import java.util.*;

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
    private WxMessageService messageService;

    /**
     * 获取聊天记录
     * @param userId 用户ID
     * @return 聊天记录
     */
    @GetMapping("list")
    public Object getMessageList(@LoginUser String userId){
        //chatGPT对话
        LitemallMessage chatMessage = new LitemallMessage();
        chatMessage.setSendUserId(ChatGPTClient.CHAT_GPT_USERID);
        chatMessage.setNickName(ChatGPTClient.CHAT_GPT_NICKNAME);
        chatMessage.setAvatarUrl(ChatGPTClient.CHAT_GPT_AVATAR);
        chatMessage.setPicUrls(new String[0]);
        chatMessage.setContent("你好，我是ChatGPT.");
        chatMessage.setAddTime(LocalDateTime.now());
        chatMessage.setReceiveUserId(userId);

        //过滤重复数据 TODO 暂时只能获取给自己发过信息的列表
        HashMap<String, LitemallMessage> receiveMessageMap = new HashMap<>();
        receiveMessageMap.put(chatMessage.getSendUserId(), chatMessage);
        List<LitemallMessage> receiveList = messageService.queryByReceiveUserId(userId);
        for (LitemallMessage message :receiveList) {
            receiveMessageMap.put(message.getSendUserId(), message);
        }

        //合并历史消息记录
        ArrayList<LitemallMessage> messageList = new ArrayList<>(receiveMessageMap.values());
        for (LitemallMessage message :messageList) {
            if (!StringUtils.hasText(message.getContent())) {
                message.setContent("[图片]...");
            }
        }
        //历史消息排序
        messageList.sort(Comparator.comparing(LitemallMessage::getAddTime).reversed());
        //返回信息列表
        return ResponseUtil.okList(messageList);
    }


    /**
     * 获取历史聊天记录
     * @param userId 用户ID
     * @return 聊天记录
     */
    @GetMapping("history")
    public Object getHistoryMessageList(@LoginUser String userId, HistoryMessageBody body){
        //获取聊天记录
        List<LitemallMessage> historyMessage = messageService.getHistoryMessage(userId , body);
        //历史消息排序
        historyMessage.sort(Comparator.comparing(LitemallMessage::getAddTime));
        return ResponseUtil.okList(historyMessage);
    }

    /**
     * 删除聊天记录
     * @param userId 用户ID
     * @param sendUserId 发送用户
     * @return 成功
     */
    @PostMapping("delete")
    public Object delete(@LoginUser String userId, @JsonBody String sendUserId){
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }
        messageService.deleteMessage(userId , sendUserId);
        messageService.deleteMessage(sendUserId, userId);
        return ResponseUtil.ok();
    }

}
