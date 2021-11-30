package com.gangling.scm.base.middleware.distributed.job.annotation;

import com.gangling.scm.base.middleware.distributed.job.config.XxlJobConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({XxlJobConfig.class})
public @interface EnableXxlJob {
}
