package com.gangling.scm.base.middleware.datasource.interceptor;

import com.gangling.scm.base.common.exception.BusinessException;
import com.gangling.scm.base.middleware.alarm.AlarmUtils;
import com.gangling.scm.base.middleware.datasource.annotation.CheckUpdateVersion;
import com.gangling.scm.base.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
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
public class UpdateVersionInterceptor implements Interceptor {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private AlarmUtils alarmUtils;
    @Value("${update.version.flag}")
    private Integer updateVersionFlag;
    @Value("${update.version.filter}")
    private String updateVersionFilter;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (updateVersionFlag != 1) {
            return invocation.proceed();
        }
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        CheckUpdateVersion needCheckVersion = Class.forName(ms.getId().substring(0,
                ms.getId().lastIndexOf("."))).getAnnotation(CheckUpdateVersion.class);
        if (needCheckVersion != null) {
            // 过滤
            if (this.isFilter(ms)) {
                return invocation.proceed();
            }
            BoundSql boundSql = ms.getSqlSource().getBoundSql(invocation.getArgs()[1]);
            PreparedStatement ps = null;
            ResultSet rs = null;
            SqlSession sqlSession = null;
            try {
                String querySql = this.getQuerySql(boundSql);
                sqlSession = sqlSessionFactory.openSession();
                ps = sqlSession.getConnection().prepareStatement(querySql);
                List<ParameterMapping> queryParamList = boundSql.getParameterMappings().subList(boundSql.getParameterMappings().size() - (querySql.length() - querySql.replace("?", "").length()), boundSql.getParameterMappings().size());
                int i = 1;
                for (ParameterMapping pm : queryParamList) {
                    String propertyName = pm.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    Object value;
                    if (ms.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(boundSql.getParameterObject().getClass())) {
                        value = boundSql.getParameterObject();
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = ms.getConfiguration().newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = ms.getConfiguration().newMetaObject(boundSql.getParameterObject()).getValue(propertyName);
                    }
                    TypeHandler<Object> typeHandler = (TypeHandler<Object>) pm.getTypeHandler();
                    typeHandler.setParameter(ps, i++, value, pm.getJdbcType());
                }
                rs = ps.executeQuery();
                if (rs.next()) {
                    this.checkWorkVersion(rs, needCheckVersion);
                }
            } catch (BusinessException be) {
                throw be;
            } catch (Exception e) {
                log.error("[UpdateVersionInterceptor][intercept]出现异常：" + boundSql.getSql(), e);
                alarmUtils.sendErrorAlarm("UpdateVersionInterceptor.intercept出现异常", e);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                    if (sqlSession != null) {
                        sqlSession.close();
                    }
                } catch (SQLException e) {
                    log.error("[UpdateVersionInterceptor][intercept]出现异常", e);
                    alarmUtils.sendErrorAlarm("UpdateVersionInterceptor.intercept出现异常", e);
                }
            }
        }

        return invocation.proceed();
    }

    private boolean isFilter(MappedStatement ms) {
        if (ms.getSqlCommandType().equals(SqlCommandType.INSERT)
                || ms.getSqlCommandType().equals(SqlCommandType.DELETE)) {
            return true;
        }
        if (CommonUtil.isNotEmpty(updateVersionFilter)) {
            return Arrays.asList(updateVersionFilter.split(",")).contains(ms.getId());
        }
        return false;
    }

    private void checkWorkVersion(ResultSet rs, CheckUpdateVersion needCheckVersion) throws SQLException {
        for (int j = rs.getMetaData().getColumnCount(); j > 0; j--) {
            if (("WORLD_VERSION").equals(rs.getMetaData().getColumnName(j))) {
                if (rs.getInt(j) != needCheckVersion.version()) {
                    throw new BusinessException("只能操作" + needCheckVersion.version() + ".0的订单，请确认后在操作");
                }
            }
        }
    }

    private String getQuerySql(BoundSql boundSql) {
        String updateSql = boundSql.getSql().toLowerCase();
        return updateSql.replaceFirst("update", "select * from").replace(updateSql.substring(this.getStartIndex("set", updateSql), this.getStartIndex("where", updateSql)), "");
    }

    private int getStartIndex(String key, String sql) {
        String[] strs = new String[] {" ", "\n", "\t"};
        int start = -1;
        for (String str : strs) {
            if (start < 0) {
                start = sql.toLowerCase().indexOf(str + key + " ");
                if (start < 0) {
                    start = sql.toLowerCase().indexOf(str + key + "\t");
                    if (start < 0) {
                        start = sql.toLowerCase().indexOf(str + key + "\n");
                    }
                }
            }
        }

        return start;
    }
}
