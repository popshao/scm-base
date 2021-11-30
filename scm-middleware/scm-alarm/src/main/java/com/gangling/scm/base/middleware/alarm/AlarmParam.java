package com.gangling.scm.base.middleware.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlarmParam {
    /**
     * @param alertCode    在告警中心配置的有效告警code。长度不超过255字符。
     */
    private String alertCode;
    /**
     * @param alertSubject 告警标题。会替换模板中的{$alertSubject}；告警收敛时的聚合Key之一。长度不超过200字符。
     */
    private String alertSubject;
    /**
     * @param alertContent 告警内容。会替换模板中的{$alertConent}
     */
    private String alertContent;
    /**
     * @param webhookUsers 设置webhookUsers指定对应@人员,多个人员用,隔开，对应名称请使用域控账号
     */
    private String webhookUsers;
    /**
     * @param interfaces 方法
     */
    private String interfaces;
    /**
     * @param params       添加额外的模板参数。
     * key 模板替换key。e.g. 若设置key为"key1"，则会替换告警模板中的{$key1}。长度不超过200字符。
     * value 模板替换值。
     */
    private Map<String, String> params = new HashMap<>();
}
