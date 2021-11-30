/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gangling.scm.base.middleware.datasource.mapper.ids;

import com.gangling.scm.base.common.context.BaseContextHandler;
import com.gangling.scm.base.middleware.datasource.mapper.Sequence;
import com.gangling.scm.base.utils.CommonUtil;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.LogicDeleteException;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.annotation.LogicDelete;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.StringUtil;
import tk.mybatis.mapper.version.VersionException;

import java.util.Iterator;
import java.util.Set;

/**
 * BaseInsertProvider实现类，基础方法实现类
 *
 * @author liuzh
 */
public class ExtIdsProvider extends MapperTemplate {

    private static final String IS_DELETED = "IS_DELETED";

    public ExtIdsProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String logicalDeleteByIds(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, this.tableName(entityClass)));
        Set<EntityColumn> pkColumnList = EntityHelper.getPKColumns(entityClass);
        if (pkColumnList.size() == 1) {
            EntityColumn column = pkColumnList.iterator().next();
            sql.append(" set is_deleted=1,update_by='${updateBy}',update_time=to_date('${updateTime}','yyyy-mm-dd hh24:mi:ss')");
            sql.append(" where ");
            sql.append(column.getColumn());
            sql.append(" in (${ids})");
            return sql.toString();
        } else {
            throw new MapperException("继承 logicalDeleteByIds 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
    }

    public String updateByIds(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, this.tableName(entityClass)));
        sql.append(updateSetColumns(entityClass, "record", true, this.isNotEmpty()));
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append(" where ");
            sql.append(column.getColumn());
            sql.append(" in (${ids})");
            return sql.toString();
        } else {
            throw new MapperException("继承 updateByIds 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
    }

    public String insertList(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        Sequence sequence = entityClass.getAnnotation(Sequence.class);
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"listNotEmptyCheck\" value=\"@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, '").append(ms.getId()).append(" 方法参数为空')\"/>");
        sql.append(SqlHelper.insertIntoTable(entityClass, this.tableName(entityClass), "list[0]"));
        Set<EntityColumn> pkColumnList = EntityHelper.getPKColumns(entityClass);
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        if (sequence != null && CommonUtil.isNotEmpty(sequence.sql())) {
            sql.append(sequence.sql(), 0, sequence.sql().lastIndexOf(".nextval") + 8).append(",");
        }
        sql.append("a.* from (");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\"union all\" >");
        sql.append(" select ");
        sql.append("<trim suffixOverrides=\",\">");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Iterator var5 = columnList.iterator();
        while(var5.hasNext()) {
            EntityColumn column = (EntityColumn)var5.next();
            if (!column.isId() && column.isInsertable()) {
                sql.append(SqlHelper.getIfNotNull("record", column, "#{record." + column.getProperty() + "} " + column.getColumn() + ",", isNotEmpty()));
                sql.append(SqlHelper.getIfIsNull("record", column, "NULL " + column.getColumn() +",", isNotEmpty()));
            }
        }
        sql.append("</trim>");
        sql.append("from dual");
        sql.append("</foreach>");
        sql.append(") a");
        EntityHelper.setKeyProperties(EntityHelper.getPKColumns(entityClass), ms);
        return sql.toString();
    }

    private static String updateSetColumns(Class<?> entityClass, String entityName, boolean notNull, boolean notEmpty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<set>");
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        EntityColumn versionColumn = null;
        EntityColumn logicDeleteColumn = null;
        Iterator var8 = columnSet.iterator();

        while(var8.hasNext()) {
            EntityColumn column = (EntityColumn)var8.next();
            if (column.getEntityField().isAnnotationPresent(Version.class)) {
                if (versionColumn != null) {
                    throw new VersionException(entityClass.getCanonicalName() + " 中包含多个带有 @Version 注解的字段，一个类中只能存在一个带有 @Version 注解的字段!");
                }

                versionColumn = column;
            }

            if (column.getEntityField().isAnnotationPresent(LogicDelete.class)) {
                if (logicDeleteColumn != null) {
                    throw new LogicDeleteException(entityClass.getCanonicalName() + " 中包含多个带有 @LogicDelete 注解的字段，一个类中只能存在一个带有 @LogicDelete 注解的字段!");
                }

                logicDeleteColumn = column;
            }

            if (!column.isId() && column.isUpdatable()) {
                if (column == logicDeleteColumn) {
                    sql.append(SqlHelper.logicDeleteColumnEqualsValue(column, false)).append(",");
                } else if (notNull) {
                    sql.append(SqlHelper.getIfNotNull(entityName, column, column.getColumnEqualsHolder(entityName) + ",", notEmpty));
                } else {
                    sql.append(column.getColumnEqualsHolder(entityName)).append(",");
                }
            }
        }

        sql.append("</set>");
        return sql.toString();
    }

    public String listByKeyList(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(" where ");
        sql.append("${columnName}");
        sql.append(" in (${keys})");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnList) {
            if (column.getColumn().equals(IS_DELETED)) {
                sql.append(SqlHelper.getIfNotNull(null, column, " and is_deleted = ${isDeleted}", isNotEmpty()));
                break;
            }
        }

        return sql.toString();
    }
}
