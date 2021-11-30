package com.gangling.scm.base.middleware.distributed.job.base;

import com.gangling.scm.base.common.context.BaseContextHandler;
import com.gangling.scm.base.common.exception.ArgumentException;
import com.gangling.scm.base.common.exception.BusinessException;
import com.gangling.scm.base.middleware.alarm.AlarmUtils;
import com.gangling.scm.base.utils.ThreadUtils;
import com.gangling.scm.base.utils.TraceIdUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Profile({"dev", "test", "stg", "prod"})
@Component
public class WmsBaseJob {
    @Autowired
    private AlarmUtils alarmUtils;

    /**
     * startTimeStamp=1614907586465&endTimeStamp=1614907586000&warehouseIdList=13
     *
     * @param param
     * @return
     */
    public WmsJobParameter pareParam(String param) {
        if (StringUtils.isEmpty(param)) {
            return null;
        }

        WmsJobParameter wmsJobParameter = new WmsJobParameter();
        Arrays.asList(param.split("&")).forEach(item -> {
            String[] pair = item.split("=");
            if ("startTimeStamp".equalsIgnoreCase(pair[0])) {
                wmsJobParameter.setStartTimeStamp(Long.valueOf(pair[1]));
            } else if ("endTimeStamp".equalsIgnoreCase(pair[0])) {
                wmsJobParameter.setEndTimeStamp(Long.valueOf(pair[1]));
            } else if ("warehouseIdList".equalsIgnoreCase(pair[0])) {
                wmsJobParameter.setWarehouseIdList(Arrays.asList(pair[1].split(",")).stream().map(Long::valueOf).collect(Collectors.toList()));
            }
        });
        return wmsJobParameter;
    }

    public ReturnT<String> callJob(String jobName, String param, Function<String, ReturnT<String>> function) {
        String traceid = TraceIdUtil.putTraceId();
        BaseContextHandler.put(BaseContextHandler.LOGIN_NAME, "SYSTEM");
        XxlJobLogger.log("{}，param={}, traceid={}", jobName, param, traceid);
        ReturnT<String> result = new ReturnT<>();

        try {
            result = function.apply(param);
        } catch (Exception ex) {
            log.error("{}异常：{}" , jobName, ex.getMessage(), ex);
            XxlJobLogger.log(ex);
            result.setCode(ReturnT.FAIL_CODE);
            result.setMsg(ex.getMessage());

            if (!(ex instanceof ArgumentException) && !(ex instanceof BusinessException)) {
                alarmUtils.sendErrorAlarm("job异常-" + jobName, ex);
            }
        } finally {
            TraceIdUtil.clear();
        }
        return result;
    }
}
