package com.gangling.scm.base.middleware.alarm;

import com.gangling.architecture.leopard.client.api.AlarmApi;
import com.gangling.architecture.leopard.client.api.AlertRequest;
import com.gangling.scm.base.common.context.BaseContextHandler;
import com.gangling.scm.base.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class AlarmUtils {

    @Value("${spring.profiles.active}")
    private String profiles;
    @Value("${app.name: }")
    private String appName;
    @Value("${global.alarm.at.persons: }")
    private String webhookUsers;
    @Value("${global.alarm.code: }")
    private String globalAlarmCode;

    /**
     * 自定义告警
     * @return 加入发送队列；每隔10秒或者队列满10个发送。true - 验证通过加入队列成功；false - 验证失败或者加入发送队列失败。
     */
    public boolean sendAlarm(AlarmParam alarmParam) {
        AlertRequest alertRequest = AlarmApi.newAlert(alarmParam.getAlertCode())
                .setAlertSubject(alarmParam.getAlertSubject())
                .setAlertApp(appName)
//                .setAlertIp(alarmParam.getIp())
                .setAlertInterface(alarmParam.getInterfaces())
                .setAlertContent(alarmParam.getAlertContent());

        alertRequest.addParam("alertEnv", profiles);
        alertRequest.addParam("traceid", BaseContextHandler.getTraceId());
        if (!StringUtils.isEmpty(alarmParam.getWebhookUsers())) {
            alertRequest.addParam("webhookUsers", alarmParam.getWebhookUsers());
        }

        if (MapUtils.isNotEmpty(alarmParam.getParams())) {
            for (String key : alarmParam.getParams().keySet()) {
                alertRequest.addParam(key, alarmParam.getParams().get(key));
            }
        }

        return alertRequest.send();
    }

    /**
     * 发送异常告警
     * @param alertSubject
     * @param e
     */
    public void sendErrorAlarm(String alertSubject, Exception e) {
        AlarmParam alarmParam = AlarmParam.builder()
                .alertCode(globalAlarmCode)
                .alertSubject(alertSubject)
                .alertContent(e.getMessage() == null ? e.getClass().getName() : e.getMessage())
                .interfaces(e.getStackTrace()[0].toString())
                .webhookUsers(webhookUsers)
                .build();

        boolean result = false;
        try {
            result = sendAlarm(alarmParam);
        } finally {
            log.debug("send alarm message, param={}, result={}", JSONUtil.writeValueAsString(alarmParam), result);
        }

    }
}
