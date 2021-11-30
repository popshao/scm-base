package com.gangling.scm.base.common.mq;

import com.gangling.scm.base.common.context.BaseContextHandler;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseMessage implements Serializable {
    private static final long serialVersionUID = -4202149029014006743L;
    private String traceId = BaseContextHandler.getTraceId();
}
