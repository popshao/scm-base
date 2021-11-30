package com.gangling.scm.base.utils;

import com.gangling.scm.base.common.exception.ArgumentException;

import java.util.Collection;
import java.util.Map;

/**
 * 断言类
 *
 * @author zhukai
 * @since 2021/4/19
 */
public class AssertUtils {

    private AssertUtils() {}

    public static void isTrue(Boolean obj, String msg) {
        if (obj == null || !obj) {
            throw new ArgumentException(msg);
        }
    }

    public static void notEmpty(String obj, String msg) {
        if (obj == null || "".equals(obj)) {
            throw new ArgumentException(msg);
        }
    }

    public static void notEmpty(Collection<?> list, String msg) {
        if (list == null || list.isEmpty()) {
            throw new ArgumentException(msg);
        }
    }

    public static void notEmpty(Map<?, ?> map, String msg) {
        if (map == null || map.isEmpty()) {
            throw new ArgumentException(msg);
        }
    }

    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new ArgumentException(msg);
        }
    }
}
