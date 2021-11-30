package com.gangling.scm.base.mq.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

/**
 * @author zhanglei03
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ConsumerService {

    String topic() default "";

    String groupId() default "";

    /* 设置tag用||分隔
     */
    String tagList() default "";

    /*设置Consumer实例的消费模式，取值说明如下：
    CLUSTERING（默认值）：表示集群消费。
    BROADCASTING：表示广播消费。
    */
    String messageModel() default "";
    // 设置Consumer实例的消费线程数，默认值：20。
    String consumeThreadNums() default "";
    // 设置消息消费失败的最大重试次数，默认值：16。
    String maxReconsumeTimes() default "";
    // 设置每条消息消费的最大超时时间，超过设置时间则被视为消费失败，等下次重新投递再次消费。每个业务需要设置一个合理的值，默认值：15，单位：分钟。
    String consumeTimeout() default "";
    // 只适用于顺序消息，设置消息消费失败的重试间隔时间。
    String suspendTimeMillis() default "";
    // 客户端本地的最大缓存消息数据，默认值：1000，单位：条。
    String maxCachedMessageAmount() default "";
    // 客户端本地的最大缓存消息大小，取值范围：16 MB~2 GB，默认值：512 MB。
    String maxCachedMessageSizeInMiB() default "";
    // 批量消费的最大消息数量，缓存的消息数量达到设置的参数值，消息队列RocketMQ版会将缓存的消息统一推送给消费者进行批量消费。默认值：32，取值范围：1~1024。
    String ConsumeMessageBatchMaxSize() default "";
    // 批量消费的等待时长，等待时长达到参数设置的值，消息队列RocketMQ版会将缓存的消息统一推送给消费者进行批量消费。默认为：0，取值范围：0~450，单位：秒
    String BatchConsumeMaxAwaitDurationInSeconds() default "";
}
