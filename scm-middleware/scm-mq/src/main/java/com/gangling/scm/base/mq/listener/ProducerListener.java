package com.gangling.scm.base.mq.listener;

import com.aliyun.openservices.ons.api.batch.BatchMessageListener;
import com.gangling.middleware.mq.ons.OnsProducer;
import com.gangling.scm.base.mq.OnsBeanUtil;
import com.gangling.scm.base.mq.OnsConfig;
import com.gangling.scm.base.mq.annotation.ProduceBean;
import com.gangling.scm.base.mq.annotation.ProduceService;
import com.gangling.scm.base.mq.listener.base.BaseListener;
import com.gangling.scm.base.utils.CommonUtil;
import com.gangling.scm.base.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author zhanglei03
 */
@Slf4j
@Service
public class ProducerListener extends BaseListener {

    @Autowired
    DefaultListableBeanFactory defaultListableBeanFactory;
    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            super.onApplicationEvent(event);
            // 生成生产者关系
            Map<String, Object> producerBeans = event.getApplicationContext().getBeansWithAnnotation(ProduceService.class);
            if (CommonUtil.isEmpty(producerBeans)) {
                return;
            }
            List<CompletableFuture<?>> cfList = new ArrayList<>(producerBeans.size());
            producerBeans.forEach((beanName, bean) -> {
                Arrays.asList(bean.getClass().getDeclaredFields()).forEach(field -> {
                    if (field.isAnnotationPresent(ProduceBean.class)) {
                        ProduceBean annotation = field.getAnnotation(ProduceBean.class);
                        if (StringUtils.isNotEmpty(annotation.topic())) {
                            OnsConfig config = new OnsConfig();
                            config.setAccessKey(accessKey);
                            config.setSecretKey(secretKey);
                            config.setNAMESRV_ADDR(namesrvAddr);
                            config.setTopic(addSuffix(annotation.topic()));

                            CompletableFuture<Integer> tempCf = ThreadUtils.supplyAsync(()-> {
                                OnsProducer producer = OnsBeanUtil.producerBean(config);
                                producer.start();

                                defaultListableBeanFactory.registerSingleton(field.getName(), producer);
                                beanFactory.autowireBean(producer);
                                try {
                                    field.setAccessible(true);
                                    field.set(bean, producer);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }

                                return 1;
                            });

                            cfList.add(tempCf);
                        }
                    }
                });
            });
            CompletableFuture.allOf(cfList.toArray(new CompletableFuture<?>[]{})).join();

        }
    }

}
