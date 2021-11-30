package com.gangling.scm.base.middleware.log.annotation;

import com.gangling.scm.base.middleware.log.listener.DynamicLoggersListener;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启修改日志级别
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({DynamicLoggersListener.class})
public @interface EnableUpdateLogLevel {
}
