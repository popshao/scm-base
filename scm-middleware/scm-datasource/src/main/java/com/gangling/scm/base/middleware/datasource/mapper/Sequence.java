package com.gangling.scm.base.middleware.datasource.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sequence {
    /**
     * 参考：@Sequence(sql = "select seq_user_acct_id.nextval from dual")
     * @return
     */
    String sql() default "";
}
