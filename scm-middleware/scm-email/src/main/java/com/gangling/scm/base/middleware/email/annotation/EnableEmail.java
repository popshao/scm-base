package com.gangling.scm.base.middleware.email.annotation;

import com.gangling.scm.base.middleware.email.SpringEmailConfig;
import com.gangling.scm.base.middleware.email.SpringEmailUtil;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({SpringEmailConfig.class, SpringEmailUtil.class})
public @interface EnableEmail {
}
