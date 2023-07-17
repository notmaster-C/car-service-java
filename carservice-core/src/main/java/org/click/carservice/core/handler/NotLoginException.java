package org.click.carservice.core.handler;

/**
 * 自定义未登录异常
 *
 * @author click
 */
public class NotLoginException extends RuntimeException {

    public NotLoginException(String message) {
        super(message);
    }

}
