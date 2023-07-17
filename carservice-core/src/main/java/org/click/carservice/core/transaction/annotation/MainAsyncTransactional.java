package org.click.carservice.core.transaction.annotation;

import java.lang.annotation.*;

/**
 * @author click
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MainAsyncTransactional {

}
