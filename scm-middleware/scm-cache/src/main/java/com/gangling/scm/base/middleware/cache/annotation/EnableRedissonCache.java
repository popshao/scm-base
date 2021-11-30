package com.gangling.scm.base.middleware.cache.annotation;


import com.gangling.scm.base.middleware.cache.config.RedissonConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author shijian on 2021/01/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({RedissonConfig.class})
public @interface EnableRedissonCache {
}
