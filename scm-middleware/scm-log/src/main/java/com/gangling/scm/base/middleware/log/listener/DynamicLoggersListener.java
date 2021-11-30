package com.gangling.scm.base.middleware.log.listener;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.gangling.scm.base.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 配置项从apollo中读取
 *
 * 主日志级别配置示例为
 *  logging.level.=INFO
 * 如需各个包单独配置日志级别, 配置示例为
 *  logging.level.com.gangling.scm=INFO
 *  logging.level.com.gangling.scm.base=DEBUG
 *  logging.level.com.springframework=INFO
 *  (可按需配置多个)
 *
 * @author zhukai
 * @since 2021/7/29
 */
@Slf4j
@Component
public class DynamicLoggersListener {

    private static final String PREFIX = "logging.level.";
    private static final String ROOT = LoggingSystem.ROOT_LOGGER_NAME;
    private static final String SPLIT = ".";

    @Autowired
    private LoggingSystem loggingSystem;

    @ApolloConfig
    private Config config;

    @PostConstruct
    private void init() {
        refreshLoggingLevels(config.getPropertyNames());
    }

    @ApolloConfigChangeListener(interestedKeyPrefixes = PREFIX)
    private void onChange(ConfigChangeEvent changeEvent) {
        refreshLoggingLevels(changeEvent.changedKeys());
    }

    private void refreshLoggingLevels(Set<String> changedKeys) {
        for (String key : changedKeys) {
            // key示例: logging.level.com.example.web
            if (StringUtil.startsWithIgnoreCase(key, PREFIX)) {
                String loggerName = PREFIX.equalsIgnoreCase(key) ? ROOT : key.substring(PREFIX.length());
                String strLevel = config.getProperty(key, parentStrLevel(loggerName));
                LogLevel level = LogLevel.valueOf(strLevel.toUpperCase());
                loggingSystem.setLogLevel(loggerName, level);

                log(loggerName, strLevel);
            }
        }
    }

    private String parentStrLevel(String loggerName) {
        String parentLoggerName = loggerName.contains(SPLIT) ? loggerName.substring(0, loggerName.lastIndexOf(SPLIT)) : ROOT;
        LoggerConfiguration loggerConfiguration = loggingSystem.getLoggerConfiguration(parentLoggerName);
        if (loggerConfiguration == null) {
            return "INFO";
        }
        return loggerConfiguration.getEffectiveLevel().name();
    }

    /**
     * 获取当前类的Logger对象有效日志级别对应的方法，进行日志输出。举例：
     * 如果当前类的EffectiveLevel为WARN，则获取的Method为 `org.slf4j.Logger#warn(java.lang.String, java.lang.Object, java.lang.Object)`
     * 目的是为了输出`changed {} log level to:{}`这一行日志
     */
    private void log(String loggerName, String strLevel) {
        try {
            LoggerConfiguration loggerConfiguration = loggingSystem.getLoggerConfiguration(log.getName());
            Method method = log.getClass().getMethod(loggerConfiguration.getEffectiveLevel().name().toLowerCase(), String.class, Object.class, Object.class);
            method.invoke(log, "changed {} log level to:{}", loggerName, strLevel);
        } catch (Exception e) {
            log.error("changed {} log level to:{} error", loggerName, strLevel, e);
        }
    }
}
