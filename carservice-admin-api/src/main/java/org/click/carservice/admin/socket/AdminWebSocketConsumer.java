package org.click.carservice.admin.socket;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.redis.annotation.MessageConsumer;
import org.click.carservice.core.redis.annotation.MessageListener;
import org.click.carservice.core.redis.util.Message;

/**
 * redis消息队列消费者
 * 广播wedSocket消息找到分布式项目中的websocket链接对象并发布消息
 *
 * @author click
 */
@MessageConsumer
@Slf4j
public class AdminWebSocketConsumer {

    public static final String MESSAGE_PREFIX = "ADMIN_MESSAGE_PUBSUB";

    @MessageListener(channel = MESSAGE_PREFIX, mode = MessageListener.Mode.PUBSUB, reuse = true)
    public void getMessage(Message<?> message) {
        String body = message.getContent().toString();
        String socketInfo = message.getId();
        AdminWebSocketContext.sendMessage(socketInfo, body);
    }
}