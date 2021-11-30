package com.gangling.scm.base.middleware.alarm.annotation;

import com.gangling.scm.base.middleware.alarm.AlarmUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({AlarmUtils.class})
public @interface EnableAlarm {
}
