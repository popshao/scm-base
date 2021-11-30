package com.gangling.scm.base.middleware.trace;

import com.gangling.scm.base.common.ConstantValue;
import com.gangling.scm.base.common.context.BaseContextHandler;
import com.gangling.scm.base.utils.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

@Slf4j
public class TraceidInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(ConstantValue.X_GLOBAL_REQUEST_ID);
        traceId = StringUtils.isEmpty(traceId) ? TraceIdUtil.putTraceId() : traceId;
        if (StringUtils.isNotEmpty(traceId)) {
            MDC.put(ConstantValue.X_GLOBAL_REQUEST_ID, traceId);
            BaseContextHandler.put(ConstantValue.X_GLOBAL_REQUEST_ID, traceId);
            response.setHeader(ConstantValue.X_GLOBAL_REQUEST_ID, traceId);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        response.setHeader(ConstantValue.X_GLOBAL_REQUEST_ID, MDC.get(ConstantValue.X_GLOBAL_REQUEST_ID));
        MDC.remove(ConstantValue.X_GLOBAL_REQUEST_ID);
        BaseContextHandler.remove();
    }

}
