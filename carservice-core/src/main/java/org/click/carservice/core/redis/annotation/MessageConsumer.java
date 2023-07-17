package org.click.carservice.core.redis.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * redis消息队列消费者注解
 *
 * @author click
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MessageConsumer {

    @AliasFor(annotation = Component.class)
    String value() default "";

}