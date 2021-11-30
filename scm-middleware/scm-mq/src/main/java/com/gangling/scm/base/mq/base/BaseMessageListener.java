package com.gangling.scm.base.mq.base;

import com.alibaba.fastjson.JSON;
import com.gangling.middleware.mq.AbstractMessageListener;
import com.gangling.middleware.mq.ConsumeResult;
import com.gangling.middleware.mq.Message;
import com.gangling.middleware.mq.ons.OnsMessageConvert;
import com.gangling.scm.base.common.exception.ArgumentException;
import com.gangling.scm.base.common.exception.BusinessException;
import com.gangling.scm.base.common.mq.BaseMessage;
import com.gangling.scm.base.middleware.alarm.AlarmUtils;
import com.gangling.scm.base.utils.CommonUtil;
import com.gangling.scm.base.utils.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;

@Slf4j
public abstract class BaseMessageListener<T extends BaseMessage> extends AbstractMessageListener<T> {

    @Autowired
    private AlarmUtils alarmUtils;
    private final Class<T> clazz = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public ConsumeResult consumeMessage(Object obj) {
        try {
            OnsMessageConvert cvt = new OnsMessageConvert();
            Message<T> msg = cvt.convertMessage(this.clazz, obj);
            if (CommonUtil.isEmpty(msg.getData().getTraceId())) {
                TraceIdUtil.putTraceId();
            } else {
                TraceIdUtil.putTraceId(msg.getData().getTraceId());
            }
            log.debug("{} consume start {}", this.getClass().getSimpleName(), JSON.toJSONString(msg.getData()));
            return super.consumeMessage(obj);
        } catch (Exception e) {
            log.error("{} consume error {}", this.getClass().getSimpleName(), JSON.toJSONString(obj), e);
            if (!(e instanceof ArgumentException) && !(e instanceof BusinessException)) {
                alarmUtils.sendErrorAlarm(String.format("mq异常-%s", this.getClass().getSimpleName()), e);
            }
            return ConsumeResult.ReconsumeLater;
        } finally {
            TraceIdUtil.clear();
        }
    }

}
