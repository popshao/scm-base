package com.gangling.scm.base.mq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhanglei03
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProduceBean {

    String topic() default "";

    //设置消息发送的超时时间，默认值：3000；单位：毫秒。
    String sendMsgTimeoutMillis() default "";
    //设置事务消息第一次回查的最快时间，单位：秒。
    String checkImmunityTimeInSeconds()  default "";
}
