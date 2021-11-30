package com.gangling.scm.base.mq;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.batch.BatchMessageListener;
import com.aliyun.openservices.ons.api.bean.BatchConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.aliyun.openservices.ons.api.bean.TransactionProducerBean;
import com.aliyun.openservices.ons.api.impl.authority.SessionCredentials;
import com.aliyun.openservices.ons.api.impl.util.NameAddrUtils;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.UtilAll;
import com.gangling.middleware.mq.AbstractMessageListener;
import com.gangling.middleware.mq.ons.OnsConsumer;
import com.gangling.middleware.mq.ons.OnsOrderConsumer;
import com.gangling.middleware.mq.ons.OnsOrderProducer;
import com.gangling.middleware.mq.ons.OnsProducer;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

final public class OnsBeanUtil {

    private static Properties toConsumerProperties(OnsConfig config) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, config.getNAMESRV_ADDR());
        properties.setProperty(PropertyKeyConst.AccessKey, config.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, config.getSecretKey());
        properties.setProperty(PropertyKeyConst.GROUP_ID, config.getGROUP_ID());
        if (StringUtils.isNotBlank(config.getMessageModel())) {
            properties.setProperty(PropertyKeyConst.MessageModel, config.getMessageModel());
        }
        if (StringUtils.isNotBlank(config.getConsumeThreadNums())) {
            properties.setProperty(PropertyKeyConst.ConsumeThreadNums, config.getConsumeThreadNums());
        }
        if (StringUtils.isNotBlank(config.getMaxReconsumeTimes())) {
            properties.setProperty(PropertyKeyConst.MaxReconsumeTimes, config.getMaxReconsumeTimes());
        }
        if (StringUtils.isNotBlank(config.getConsumeTimeout())) {
            properties.setProperty(PropertyKeyConst.ConsumeTimeout, config.getConsumeTimeout());
        }
        if (StringUtils.isNotBlank(config.getSuspendTimeMillis())) {
            properties.setProperty(PropertyKeyConst.SuspendTimeMillis, config.getSuspendTimeMillis());
        }
        if (StringUtils.isNotBlank(config.getMaxCachedMessageAmount())) {
            properties.setProperty(PropertyKeyConst.MaxCachedMessageAmount, config.getMaxCachedMessageAmount());
        }
        if (StringUtils.isNotBlank(config.getMaxCachedMessageSizeInMiB())) {
            properties.setProperty(PropertyKeyConst.MaxCachedMessageSizeInMiB, config.getMaxCachedMessageSizeInMiB());
        }
        if (StringUtils.isNotBlank(config.getConsumeMessageBatchMaxSize())) {
            properties.setProperty(PropertyKeyConst.ConsumeMessageBatchMaxSize, config.getConsumeMessageBatchMaxSize());
        }
        if (StringUtils.isNotBlank(config.getBatchConsumeMaxAwaitDurationInSeconds())) {
            properties.setProperty(PropertyKeyConst.BatchConsumeMaxAwaitDurationInSeconds, config.getBatchConsumeMaxAwaitDurationInSeconds());
        }
        return properties;
    }

    private static Properties toProducerProperties(OnsConfig config) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, config.getNAMESRV_ADDR());
        properties.setProperty(PropertyKeyConst.AccessKey, config.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, config.getSecretKey());
        if (StringUtils.isNotBlank(config.getSendMsgTimeoutMillis())) {
            properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, config.getSendMsgTimeoutMillis());
        }
        if (StringUtils.isNotBlank(config.getCheckImmunityTimeInSeconds())) {
            properties.setProperty(PropertyKeyConst.CheckImmunityTimeInSeconds, config.getCheckImmunityTimeInSeconds());
        }
        properties.setProperty(PropertyKeyConst.InstanceName, buildIntanceName(config.getTopic(), config.getNAMESRV_ADDR(), config.getAccessKey()));
        return properties;
    }

    private static String buildIntanceName(String topic, String nameArr, String accessKey) {
        return UtilAll.getPid() + "#" + topic.hashCode() + "#" + nameArr.hashCode() + "#" + accessKey.hashCode() + "#" + System.nanoTime();
    }

    public static BatchConsumerBean batchConsumerBean(OnsConfig config, String tags, BatchMessageListener listener) {
        Map<Subscription, BatchMessageListener> subscriptionTable = new HashMap<>(2);

        Subscription subscription = new Subscription();
        subscription.setTopic(config.getTopic());
        subscription.setExpression(tags);

        subscriptionTable.put(subscription, listener);

        BatchConsumerBean consumer = new BatchConsumerBean();
        consumer.setProperties(toConsumerProperties(config));
        consumer.setSubscriptionTable(subscriptionTable);
        return consumer;
    }

    public static OnsConsumer consumerBean(OnsConfig config, String tags, AbstractMessageListener listener) {
        OnsConsumer consumer = new OnsConsumer();
        consumer.setTopic(config.getTopic());
        consumer.setProperties(toConsumerProperties(config));
        listener.setType("ons");
        consumer.setMessageListener(listener);
        consumer.setExpress(tags);
        return consumer;
    }

    public static OnsOrderConsumer orderConsumerBean(OnsConfig config, List<String> tags, AbstractMessageListener listener) {
        OnsOrderConsumer consumer = new OnsOrderConsumer();
        consumer.setTopic(config.getTopic());
        consumer.setProperties(toConsumerProperties(config));
        listener.setType("ons");
        consumer.setMessageListener(listener);
        consumer.setExpress(join(tags, "||"));
        return consumer;
    }

    public static OnsProducer producerBean(OnsConfig config) {
        OnsProducer producer = new OnsProducer();
        producer.setTopic(config.getTopic());
        producer.setProperties(toProducerProperties(config));
        return producer;
    }

    public static OnsOrderProducer orderProducerBean(OnsConfig config) {
        OnsOrderProducer producer = new OnsOrderProducer();
        producer.setTopic(config.getTopic());
        producer.setProperties(toProducerProperties(config));
        return producer;
    }

    public static TransactionProducerBean transactionProducerBean(OnsConfig config, LocalTransactionChecker localTransactionChecker) {
        TransactionProducerBean producer = new TransactionProducerBean();
        producer.setLocalTransactionChecker(localTransactionChecker);
        producer.setProperties(toProducerProperties(config));
        return producer;
    }


    private static String join(List<String> list, String sep) {
        int size = list.size();

        StringBuilder buf = new StringBuilder();

        int idx = 0;
        for (String ele : list) {
            buf.append(ele);
            if (idx < size - 1) {
                buf.append(sep);
            }
            idx++;
        }

        return buf.toString();
    }

}
