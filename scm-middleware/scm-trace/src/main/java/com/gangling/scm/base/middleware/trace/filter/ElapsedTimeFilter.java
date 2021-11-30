package com.gangling.scm.base.middleware.trace.filter;

import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.gangling.scm.base.common.ConstantValue;
import com.gangling.scm.base.common.context.BaseContextHandler;
import com.gangling.scm.base.utils.JSONUtil;
import com.gangling.scm.base.utils.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class ElapsedTimeFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        setTraceId();
        setParameter();

        long start = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        long elapsed = System.currentTimeMillis() - start;
        String host = "";
        String consumer = "";
        if (RpcContext.getContext().isProviderSide()) {
            host = RpcContext.getContext().getRemoteHost();
            consumer = getClientName();
        }

        log.info("dubbo服务--->耗时:{}ms,[{}],[{}],参数:{},消费者:{} 消费者ip:{},结果:{}",
                elapsed,
                invoker.getInterface(),
                invocation.getMethodName(),
                JSON.toJSONString(invocation.getArguments()),
                consumer,
                host,
                getSplitStr(result.getValue(), 500)
        );

        // 资源清理
        if (RpcContext.getContext().isProviderSide()) {
            MDC.clear();
            BaseContextHandler.remove();
        }
        return result;
    }

    private void setTraceId() {
        String traceId = MDC.get(ConstantValue.X_GLOBAL_REQUEST_ID);
        // 原来就有traceId的话，就不覆盖
        if (StringUtils.isBlank(traceId)) {
            traceId = RpcContext.getContext().getAttachment(ConstantValue.X_GLOBAL_REQUEST_ID);
            if (StringUtils.isBlank(traceId)) {
                traceId = TraceIdUtil.putTraceId();
            }
            MDC.put(ConstantValue.X_GLOBAL_REQUEST_ID, traceId);
            BaseContextHandler.put(ConstantValue.X_GLOBAL_REQUEST_ID, traceId);
        }

        // consumer传递traceid给server
        if (RpcContext.getContext().isConsumerSide()) {
            RpcContext.getContext().setAttachment(ConstantValue.X_GLOBAL_REQUEST_ID, traceId);
        }
    }

    private void setParameter() {
        Map<String, String> paramMap = BaseContextHandler.getAll();

        // consumer传递参数给server
        if (RpcContext.getContext().isConsumerSide()) {
            RpcContext.getContext().setAttachments(paramMap);
        } else if (RpcContext.getContext().isProviderSide()) {
            BaseContextHandler.putAll(RpcContext.getContext().getAttachments());
        }
    }

    private String getSplitStr(Object source, int length) {
        if (Objects.isNull(source)) {
            return null;
        }

        String result = JSONUtil.writeValueAsString(source);
        if (StringUtils.length(result) > length) {
            result = StringUtils.substring(result, 0, length);
        }
        return result;
    }

    private String getClientName() {
        try {
            return RpcContext.getContext().getAttachment("consumer_application");
        } catch (Exception e) {
            return "unknown";
        }
    }
}