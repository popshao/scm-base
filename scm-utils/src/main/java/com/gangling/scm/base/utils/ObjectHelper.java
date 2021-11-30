package com.gangling.scm.base.utils;

import java.math.BigDecimal;

public class ObjectHelper {
    public static Object nvlObject(Object obj, Object defVal) {
        return null == obj ? defVal : obj;
    }

    public static BigDecimal nvl(Double obj, BigDecimal defVal) {
        return null == obj ? defVal : BigDecimal.valueOf(obj);
    }

    public static Double nvl(BigDecimal obj, Double defVal) {
        return null == obj ? defVal : obj.doubleValue();
    }

    public static BigDecimal nvl(Integer obj, BigDecimal defVal) {
        return null == obj ? defVal : BigDecimal.valueOf(obj);
    }

    public static Long nvl(Integer obj, Long defVal) {
        return null == obj ? defVal : Long.valueOf(obj);
    }

    public static Integer nvl(String obj, Integer defVal) {
        return StringUtil.isEmpty(obj) ? defVal : Integer.valueOf(obj);
    }

    public static String nvl(Integer obj, String defVal) {
        return null == obj ? defVal : String.valueOf(obj);
    }

    public static String nvl(Long obj, String defVal) {
        return null == obj ? defVal : String.valueOf(obj);
    }


    public static Long nvl(String obj, Long defVal) {
        return StringUtil.isEmpty(obj) ? defVal : Long.valueOf(obj);
    }

    public static Boolean nvl(String obj, Boolean defVal) {
        return StringUtil.isEmpty(obj) ? defVal : Boolean.valueOf(obj);
    }

    public static Long nvl(Long obj, long defVal) {
        return null == obj ? defVal : obj;
    }

    public static String nvl(BigDecimal obj, String defVal) {
        return null == obj ? defVal : obj.toString();
    }

    public static Long nvl(BigDecimal obj, Long defVal) {
        return null == obj ? defVal : obj.longValue();
    }

    public static BigDecimal nvl(BigDecimal obj, BigDecimal defVal) {
        return obj == null ? defVal : obj;
    }
}
