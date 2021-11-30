package com.gangling.scm.base.middleware.trace;

import com.gangling.scm.base.utils.ClientIPUtil;
import com.gangling.scm.base.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class ControllerAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long nowTime = System.currentTimeMillis();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 从HttpServletRequest获取query param
        String params = "";
        Map<String, String[]> paramsMap = request.getParameterMap();
        for (String key : paramsMap.keySet()) {
            String[] values = paramsMap.get(key);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                params = params + key + "=" + value + "&";
            }
        }

        // json参数，特殊处理
        if (request.getContentType() != null && request.getContentType().equalsIgnoreCase("application/json")) {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            if (ArrayUtils.isNotEmpty(method.getParameters())) {
                for (int i = 0; i < method.getParameters().length; i++) {
                    String key = method.getParameters()[i].getName();
                    String value = JSONUtil.writeValueAsString(pjp.getArgs()[i]).replace("\n", ",");
                    params = params + key + "=" + value + "&";
                }
            }
        }

        if (StringUtils.isNotEmpty(params)) {
            params = params.substring(0, params.length() - 1);
        }

        //请求的IP
        String ip = ClientIPUtil.getIpAddress(request);
        String uri = request.getRequestURI();
        String controllerName = getControllerName(pjp.getTarget().getClass().getName());
        String methodName = pjp.getSignature().getName();
        Object result = null;
        try {
            result = pjp.proceed(pjp.getArgs());
        } finally {
            log.info("http服务--->耗时:{}ms,method:{},uri:{},方法:{},参数:{},ip:{},结果:{}",
                    (System.currentTimeMillis() - nowTime),
                    request.getMethod(),
                    uri,
                    controllerName + "." + methodName,
                    params,
                    ip,
                    getSplitStr(result, 500)
            );
        }
        return result;
    }

    private String getControllerName(String controllerPackageName) {
        List<String> names = Arrays.asList(controllerPackageName.split("\\."));
        return names.get(names.size() - 1);
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
}
