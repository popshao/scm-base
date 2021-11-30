package com.gangling.scm.base.mq.base;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.batch.BatchMessageListener;
import com.gangling.scm.base.common.mq.BaseMessage;
import com.gangling.scm.base.middleware.alarm.AlarmUtils;
import com.gangling.scm.base.utils.CommonUtil;
import com.gangling.scm.base.utils.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseBatchMessageListener<T extends BaseMessage> implements BatchMessageListener {

    @Autowired
    private AlarmUtils alarmUtils;

    private final Class<T> clazz = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public Action consume(List<Message> list, ConsumeContext consumeContext) {
        try {
            List<T> dtoList = new ArrayList<>(60);
            for (Message message : list) {
                dtoList.add(JSON.parseObject(new String(message.getBody(), "utf-8"), clazz));
            }
            log.info("consume traceIds{}", StringUtils.join(CommonUtil.listForList(dtoList, "traceId"), "||"));
            TraceIdUtil.putTraceId();
            log.debug("{} consume start {}", this.getClass().getSimpleName(), JSON.toJSONString(dtoList));
            return this.consume(dtoList);
        } catch (Exception e) {
            log.error("{} consume error {}", this.getClass().getSimpleName(), JSON.toJSONString(list), e);
            alarmUtils.sendErrorAlarm(String.format("mq异常-%s", this.getClass().getSimpleName()), e);
            return Action.ReconsumeLater;
        } finally {
            MDC.clear();
        }
    }

    public abstract Action consume(List<T> list) throws Exception;
}
