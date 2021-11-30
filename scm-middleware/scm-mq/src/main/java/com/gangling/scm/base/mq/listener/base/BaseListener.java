package com.gangling.scm.base.mq.listener.base;

import com.gangling.middleware.mq.AbstractMessageListener;
import com.gangling.scm.base.mq.OnsBeanUtil;
import com.gangling.scm.base.mq.OnsConfig;
import com.gangling.scm.base.mq.annotation.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * @author zhanglei03
 */
@Slf4j
public class BaseListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${mq.access.key}")
    protected String accessKey;
    @Value("${mq.secret.key}")
    protected String secretKey;
    @Value("${mq.namesrv.addr}")
    protected String namesrvAddr;
    protected String profile;

    private static final String PROFILE_LOCAL = "local";
    private static final String PROFILE_DEV = "dev";
    private static final String PROFILE_TEST = "test";
    private static final String PROFILE_STG = "stg";
    private static final String SUFFIX_DEV = "_DEV";
    private static final String SUFFIX_TEST = "_TEST";
    private static final String SUFFIX_STG = "_STG";


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        profile = event.getApplicationContext().getEnvironment().getActiveProfiles()[0];
    }

    protected String addSuffix(String str) {
        String suffix = "";
        if (PROFILE_DEV.equals(profile) || PROFILE_LOCAL.equals(profile)) {
            suffix = SUFFIX_DEV;
        } else if (PROFILE_TEST.equals(profile)) {
            suffix = SUFFIX_TEST;
        } else if (PROFILE_STG.equals(profile)) {
            suffix = SUFFIX_STG;
        }

        if ("".equals(suffix)) {
            log.warn("[ConsumerListener][onApplicationEvent]没有配置profile，请检查配置");
        }

        return str + suffix;
    }
}
