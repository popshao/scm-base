package com.gangling.scm.base.middleware.datasource.interceptor;


import com.gangling.scm.base.middleware.alarm.AlarmUtils;
import com.gangling.scm.base.middleware.datasource.annotation.AddQueryVersion;
import com.gangling.scm.base.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhanglei03
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class}) })
@Component
@Slf4j
public class QueryVersionInterceptor implements Interceptor {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AlarmUtils alarmUtils;
    @Value("${query.version.flag}")
    private Integer queryVersionFlag;
    @Value("${query.version.filter}")
    private String queryVersionFilter;

    private static final String WORLD_VERSION = "WORLD_VERSION=";
    private static final Map<String, Integer> ADD_QUERY_MAP = new ConcurrentHashMap<>(5);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            this.init();
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
            MappedStatement ms = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
            BoundSql boundSql = statementHandler.getBoundSql();
            AddQueryVersion addQueryVersion = Class.forName(ms.getId().substring(0, ms.getId().lastIndexOf("."))).getAnnotation(AddQueryVersion.class);
            if (addQueryVersion != null) {
                if (this.isFilter(ms)) {
                    return invocation.proceed();
                }
                Select select = (Select) CCJSqlParserUtil.parse(boundSql.getSql());
                this.processSelectBody(select.getSelectBody(), addQueryVersion.version());

                CommonUtil.setFildValueByName("sql", select.getSelectBody().toString(), boundSql);
            }
        } catch (Exception e) {
            log.error("[QueryVersionInterceptor][intercept]出现异常", e);
            alarmUtils.sendErrorAlarm("UpdateVersionInterceptor.intercept出现异常", e);
        }

        return invocation.proceed();
    }

    private void processSelectBody(SelectBody selectBody, Integer version) throws JSQLParserException {
        if (selectBody instanceof PlainSelect) {
            this.addQueryVersion((PlainSelect) selectBody, version);
        } else if (selectBody instanceof WithItem) {
            this.processSelectBody(selectBody, version);
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (CommonUtil.isNotEmpty(operationList.getSelects())) {
                for (SelectBody tempSelect : operationList.getSelects()) {
                    this.processSelectBody(tempSelect, version);
                }
            }
        }
    }

    private void addQueryVersion(PlainSelect plainSelect, Integer version) throws JSQLParserException {
        this.processWhere(plainSelect, version);

        if (CommonUtil.isNotEmpty(plainSelect.getJoins())) {
            for (Join join : plainSelect.getJoins()) {
                this.processJoin(join, version);
            }
        }
    }

    private void processWhere(PlainSelect plainSelect, Integer version) throws JSQLParserException {
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                this.processSelectBody(subSelect.getSelectBody(), version);
            }
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null && lateralSubSelect.getSubSelect().getSelectBody() != null) {
                this.processSelectBody(lateralSubSelect.getSubSelect().getSelectBody(), version);
            }
        } else if (fromItem instanceof Table) {
            Table table = (Table) fromItem;
            if (!ADD_QUERY_MAP.containsKey(table.getName().toUpperCase())) {
                return;
            }
            Expression where;
            if (table.getAlias() != null) {
                where = CCJSqlParserUtil.parseCondExpression(table.getAlias().getName() + "." + WORLD_VERSION + version);
            } else {
                where = CCJSqlParserUtil.parseCondExpression(WORLD_VERSION + version);
            }
            if (plainSelect.getWhere() == null) {
                plainSelect.setWhere(where);
            } else {
                AndExpression andExpression = new AndExpression(plainSelect.getWhere(), where);
                plainSelect.setWhere(andExpression);
            }
        }
    }

    private void processJoin(Join join, Integer version) throws JSQLParserException {
        FromItem fromItem = join.getRightItem();
        if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                this.processSelectBody(subSelect.getSelectBody(), version);
            }
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null && lateralSubSelect.getSubSelect().getSelectBody() != null) {
                this.processSelectBody(lateralSubSelect.getSubSelect().getSelectBody(), version);
            }
        } else if (fromItem instanceof Table) {
            Table joinTable = (Table) fromItem;
            if (!ADD_QUERY_MAP.containsKey(joinTable.getName().toUpperCase())) {
                return;
            }

            Expression on;
            if (joinTable.getAlias() != null) {
                on = CCJSqlParserUtil.parseCondExpression(joinTable.getAlias().getName() + "." + WORLD_VERSION + ADD_QUERY_MAP.get(joinTable.getName()));
            } else {
                on = CCJSqlParserUtil.parseCondExpression(WORLD_VERSION + ADD_QUERY_MAP.get(joinTable.getName()));
            }

            if (join.getOnExpression() == null) {
                join.setOnExpression(on);
            } else {
                AndExpression andExpression = new AndExpression(join.getOnExpression(), on);
                join.setOnExpression(andExpression);
            }
        }
    }


    private boolean isFilter(MappedStatement ms) {
        if (ms.getSqlCommandType().equals(SqlCommandType.INSERT)
                || ms.getSqlCommandType().equals(SqlCommandType.DELETE)
                || ms.getSqlCommandType().equals(SqlCommandType.UPDATE)) {
            return true;
        }
        if (CommonUtil.isNotEmpty(queryVersionFilter)) {
            return Arrays.asList(queryVersionFilter.split(",")).contains(ms.getId());
        }
        return false;
    }

    private void init() {
        if (CommonUtil.isNotEmpty(ADD_QUERY_MAP)) {
            return;
        }
        synchronized (ADD_QUERY_MAP) {
            if (CommonUtil.isEmpty(ADD_QUERY_MAP)) {
                Map<String, Object> addQueryVersionBeans = applicationContext.getBeansWithAnnotation(AddQueryVersion.class);
                if (CommonUtil.isEmpty(addQueryVersionBeans)) {
                    return;
                }
                for (Map.Entry<String, Object> entry : addQueryVersionBeans.entrySet()) {
                    AddQueryVersion addQueryVersion = AnnotationUtils.findAnnotation((Class<?>) AopUtils.getTargetClass(entry.getValue()).getGenericInterfaces()[0], AddQueryVersion.class);
                    if (addQueryVersion != null) {
                        ADD_QUERY_MAP.put(addQueryVersion.tableName(), addQueryVersion.version());
                    }
                }
            }
        }
    }

}