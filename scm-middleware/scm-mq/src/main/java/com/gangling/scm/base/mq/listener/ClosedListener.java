package com.gangling.scm.base.mq.listener;

import com.aliyun.openservices.ons.api.Admin;
import com.gangling.middleware.mq.Consumer;
import com.gangling.middleware.mq.Producer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ClosedListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();

        // 销毁消费者
        String[] consumerNames = applicationContext.getBeanNamesForType(Consumer.class);
        Arrays.stream(consumerNames).forEach(beanName -> {
            Consumer consumer = applicationContext.getBean(beanName, Consumer.class);
            consumer.shutdown();
        });

        // 销毁生产者
        String[] producerNames = applicationContext.getBeanNamesForType(Producer.class);
        Arrays.stream(producerNames).forEach(beanName -> {
            Producer producer = applicationContext.getBean(beanName, Producer.class);
            producer.shutdown();
        });

        // 销毁aliyun消费者
        String[] adminNames = applicationContext.getBeanNamesForType(Admin.class);
        Arrays.stream(adminNames).forEach(beanName -> {
            Admin admin = applicationContext.getBean(beanName, Admin.class);
            admin.shutdown();
        });
    }
}
