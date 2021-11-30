package com.gangling.scm.base.mq.listener;

import com.aliyun.openservices.ons.api.batch.BatchMessageListener;
import com.gangling.middleware.mq.AbstractMessageListener;
import com.gangling.scm.base.mq.OnsBeanUtil;
import com.gangling.scm.base.mq.OnsConfig;
import com.gangling.scm.base.mq.annotation.ConsumerService;
import com.gangling.scm.base.mq.listener.base.BaseListener;
import com.gangling.scm.base.utils.CommonUtil;
import com.gangling.scm.base.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglei03
 */
@Slf4j
@Service
public class ConsumerListener extends BaseListener {

    @Value("${spring.profiles.active}")
    private String profiles;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            super.onApplicationEvent(event);

            // 本地环境不允许消费mq，统一交由dev环境消费
            if (profiles.equals("local")) {
                return;
            }

            // 生成消息消费者 绑定关系
            Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(ConsumerService.class);
            beans.forEach((beanName, bean) -> {
                ConsumerService annotation = bean.getClass().getAnnotation(ConsumerService.class);
                if (StringUtils.isNotEmpty(annotation.topic()) && StringUtils.isNotEmpty(annotation.groupId())) {
                    OnsConfig config = new OnsConfig();
                    config.setAccessKey(accessKey);
                    config.setSecretKey(secretKey);
                    config.setNAMESRV_ADDR(namesrvAddr);
                    config.setTopic(addSuffix(annotation.topic()));
                    config.setGROUP_ID(addSuffix(annotation.groupId()));
                    config.setConsumeThreadNums(annotation.consumeThreadNums());
                    boolean isBatchFlag = false;
                    if (StringUtils.isNotEmpty(annotation.ConsumeMessageBatchMaxSize())) {
                        config.setConsumeMessageBatchMaxSize(annotation.ConsumeMessageBatchMaxSize());
                        isBatchFlag = true;
                    }
                    if (StringUtils.isNotEmpty(annotation.BatchConsumeMaxAwaitDurationInSeconds())) {
                        config.setBatchConsumeMaxAwaitDurationInSeconds(annotation.BatchConsumeMaxAwaitDurationInSeconds());
                        isBatchFlag = true;
                    }
                    if (isBatchFlag) {
                        ThreadUtils.supplyAsync(()-> {
                            OnsBeanUtil.batchConsumerBean(config, annotation.tagList(), (BatchMessageListener)bean).start();
                            return 1;
                        });
                    } else {
                        ThreadUtils.supplyAsync(()-> {
                            OnsBeanUtil.consumerBean(config, annotation.tagList(), (AbstractMessageListener) bean).start();
                            return 1;
                        });
                    }
                }
             });
        }
    }

}
