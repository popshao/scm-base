package com.gangling.scm.base.middleware.datasource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhanglei03
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AddQueryVersion {
    /**
     * 表名
     * @return
     */
    String tableName() default  "";

    /**
     * 版本号
     */
    int version() default 1;
}
