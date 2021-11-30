package com.gangling.scm.base.mq.annotation;

import com.gangling.scm.base.mq.listener.ClosedListener;
import com.gangling.scm.base.mq.listener.ConsumerListener;
import com.gangling.scm.base.mq.listener.ProducerListener;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({ConsumerListener.class, ProducerListener.class, ClosedListener.class})
public @interface EnableOnsMQ {
}
