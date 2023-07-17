package org.click.carservice.core.redis.handler;

import cn.hutool.core.util.StrUtil;
import org.click.carservice.core.redis.annotation.MessageHandler;
import org.click.carservice.core.redis.annotation.MessageListener;
import org.click.carservice.core.redis.cache.RedisCacheService;
import org.springframework.data.redis.connection.RedisConnection;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * redis消息队列Pub/sub 处理方式 实现 广播消息
 *
 * @author click
 */
@MessageHandler(value = MessageListener.Mode.PUBSUB)
public class PubsubMessageHandler extends AbstractMessageHandler {

    @Override
    public void invokeMessage(Method method) {
        Set<String> consumers = new HashSet<>();
        MessageListener annotation = method.getAnnotation(MessageListener.class);
        String channel = getChannel(annotation);
        RedisConnection connection = RedisCacheService.getConnection();
        connection.subscribe((message, pattern) -> {
            Class<?> declaringClass = method.getDeclaringClass();
            Object bean = applicationContext.getBean(declaringClass);
            byte[] body = message.getBody();
            consumer(method, consumers, bean, body, annotation);
        }, channel.getBytes());
    }

    private String getChannel(MessageListener annotation) {
        String value = annotation.value();
        String channel = annotation.channel();
        return StrUtil.isBlank(channel) ? value : channel;
    }
}
