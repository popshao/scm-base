package com.gangling.scm.base.utils;

import com.gangling.scm.base.common.ConstantValue;
import com.gangling.scm.base.common.context.BaseContextHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class TraceIdUtil {

    /**
     * 塞入跟踪traceId
     */
    public static String putTraceId() {
        String traceId = MDC.get(ConstantValue.X_GLOBAL_REQUEST_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
            MDC.put(ConstantValue.X_GLOBAL_REQUEST_ID, traceId);
        }
        return traceId;
    }

    public static void clear() {
        MDC.clear();
        BaseContextHandler.remove();
    }

    public static String putTraceId(String traceId) {
        if (StringUtils.isBlank(traceId)) {
            return null;
        }
        MDC.put(ConstantValue.X_GLOBAL_REQUEST_ID, traceId);
        return traceId;
    }

    /**
     * 关闭可关闭的东西，这几行代码太恶心了，要不停的重复写
     *
     * @param c
     */
    public static void close(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException e) {
            log.warn("close error", e);
        }
    }
}
