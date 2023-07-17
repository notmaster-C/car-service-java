package org.click.carservice.wx.web.impl;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.chatgpt.ChatGPTClient;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceMessage;
import org.click.carservice.wx.model.message.body.HistoryMessageBody;
import org.click.carservice.wx.service.WxMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 用户聊天列表
 * @author click
 */
@Slf4j
@Service
public class WxWebMessageService {

    @Autowired
    private WxMessageService messageService;

    /**
     * 获取聊天记录
     * @param userId 用户ID
     * @return 聊天记录
     */
    public Object getMessageList(String userId){
        //chatGPT对话
        CarServiceMessage chatMessage = new CarServiceMessage();
        chatMessage.setSendUserId(ChatGPTClient.CHAT_GPT_USERID);
        chatMessage.setNickName(ChatGPTClient.CHAT_GPT_NICKNAME);
        chatMessage.setAvatarUrl(ChatGPTClient.CHAT_GPT_AVATAR);
        chatMessage.setPicUrls(new String[0]);
        chatMessage.setContent("你好，我是ChatGPT.");
        chatMessage.setAddTime(LocalDateTime.now());
        chatMessage.setReceiveUserId(userId);

        //过滤重复数据 TODO 暂时只能获取给自己发过信息的列表
        HashMap<String, CarServiceMessage> receiveMessageMap = new HashMap<>();
        receiveMessageMap.put(chatMessage.getSendUserId(), chatMessage);
        List<CarServiceMessage> receiveList = messageService.queryByReceiveUserId(userId);
        for (CarServiceMessage message :receiveList) {
            receiveMessageMap.put(message.getSendUserId(), message);
        }

        //合并历史消息记录
        ArrayList<CarServiceMessage> messageList = new ArrayList<>(receiveMessageMap.values());
        for (CarServiceMessage message :messageList) {
            if (!StringUtils.hasText(message.getContent())) {
                message.setContent("[图片]...");
            }
        }
        //历史消息排序
        messageList.sort(Comparator.comparing(CarServiceMessage::getAddTime).reversed());
        //返回信息列表
        return ResponseUtil.okList(messageList);
    }


    /**
     * 获取历史聊天记录
     * @param userId 用户ID
     * @return 聊天记录
     */
    public Object getHistoryMessageList(String userId, HistoryMessageBody body){
        //获取聊天记录
        List<CarServiceMessage> historyMessage = messageService.getHistoryMessage(userId , body);
        //历史消息排序
        historyMessage.sort(Comparator.comparing(CarServiceMessage::getAddTime));
        return ResponseUtil.okList(historyMessage);
    }

    /**
     * 删除聊天记录
     * @param userId 用户ID
     * @param sendUserId 发送用户
     * @return 成功
     */
    public Object delete(String userId, String sendUserId){
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }
        messageService.deleteMessage(userId , sendUserId);
        messageService.deleteMessage(sendUserId, userId);
        return ResponseUtil.ok();
    }

}
