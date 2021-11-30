package com.gangling.scm.base.middleware.datasource.interceptor;

import com.gangling.scm.base.common.exception.BusinessException;
import com.gangling.scm.base.middleware.datasource.annotation.CheckUpdateVersion;
import com.gangling.scm.base.utils.CommonUtil;
import com.gangling.scm.base.utils.DateUtil;
import com.gangling.scm.base.utils.PlainResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author zhanglei03
 */
@Slf4j
@Intercepts({@Signature(
        type= Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class})})
@Component
public class UpdateStockInterceptor implements Interceptor {

    @Value("${update.stockSnapshot.filter:}")
    private String updateStockSnapshotFilter;
    @Value("${stock.snapshot.switch:}")
    private String snapshotTime;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        try {
            if (isStockSnapshotTime() && CommonUtil.isNotEmpty(updateStockSnapshotFilter) && Arrays.asList(updateStockSnapshotFilter.split(",")).contains(ms.getId().substring(0,
                    ms.getId().lastIndexOf(".")))) {
                throw new BusinessException("库存镜像备份中，当前时间系统禁止操作");
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            log.error("[UpdateStockInterceptor][intercept]出现异常：", e);
        }

        return invocation.proceed();
    }

    private Boolean isStockSnapshotTime() {
        try {
            if(StringUtils.isEmpty(snapshotTime)){
                return false;
            }
            String[] hourArray=snapshotTime.split(",");
            String startHour=hourArray[0];
            String endHour=hourArray[1];
            int configDay= CommonUtil.isNotEmpty(hourArray[2]) ? Integer.parseInt(hourArray[2]) : 1;
            Calendar calendar=Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            //当前时间不为每月1号
            if(day!= configDay){
                return false;
            }
            Date startDate= DateUtils.parseDate(DateUtil.dateFormat(calendar.getTime(),DateUtil.ISO_EXPANDED_DATE_FORMAT)+" "+startHour,DateUtil.DATETIME_PATTERN);
            Date endDate=DateUtils.parseDate(DateUtil.dateFormat(calendar.getTime(),DateUtil.ISO_EXPANDED_DATE_FORMAT)+" "+endHour,DateUtil.DATETIME_PATTERN);
            if(calendar.getTime().compareTo(startDate)>=0 && calendar.getTime().before(endDate)){
                return true;
            }
        } catch (Exception e) {
            log.error("[UpdateStockInterceptor][isStockSnapshotTime]出现异常", e);
        }

        return false;
    }
}
