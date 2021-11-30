package com.gangling.scm.base.mq;

import lombok.Data;

import java.io.Serializable;

@Data
public class OnsConfig implements Serializable {
    private String NAMESRV_ADDR;

    private String AccessKey;

    private String SecretKey;

    private String topic;

    // ---------消息发送参数---------
    //设置消息发送的超时时间，默认值：3000；单位：毫秒。
    private String SendMsgTimeoutMillis;
    //设置事务消息第一次回查的最快时间，单位：秒。
    private String CheckImmunityTimeInSeconds;

    // ---------消息订阅参数---------
    private String GROUP_ID;
    /*设置Consumer实例的消费模式，取值说明如下：
        CLUSTERING（默认值）：表示集群消费。
        BROADCASTING：表示广播消费。
    */
    private String MessageModel;
    // 设置Consumer实例的消费线程数，默认值：20。
    private String ConsumeThreadNums;
    // 设置消息消费失败的最大重试次数，默认值：16。
    private String MaxReconsumeTimes;
    // 设置每条消息消费的最大超时时间，超过设置时间则被视为消费失败，等下次重新投递再次消费。每个业务需要设置一个合理的值，默认值：15，单位：分钟。
    private String ConsumeTimeout;
    // 只适用于顺序消息，设置消息消费失败的重试间隔时间。
    private String suspendTimeMillis;
    // 客户端本地的最大缓存消息数据，默认值：1000，单位：条。
    private String maxCachedMessageAmount;
    // 客户端本地的最大缓存消息大小，取值范围：16 MB~2 GB，默认值：512 MB。
    private String maxCachedMessageSizeInMiB;

    //          ---------Push方式特有参数（批量消费)---------
    // 批量消费的最大消息数量，缓存的消息数量达到设置的参数值，消息队列RocketMQ版会将缓存的消息统一推送给消费者进行批量消费。默认值：32，取值范围：1~1024。
    private String ConsumeMessageBatchMaxSize;
    // 批量消费的等待时长，等待时长达到参数设置的值，消息队列RocketMQ版会将缓存的消息统一推送给消费者进行批量消费。默认为：0，取值范围：0~450，单位：秒
    private String BatchConsumeMaxAwaitDurationInSeconds;

}
