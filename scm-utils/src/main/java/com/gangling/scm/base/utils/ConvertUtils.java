package com.gangling.scm.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 转换工具
 *
 * @Filename: ConvertUtils.java
 * @Version: 1.0
 * @Author: Zhang Lei
 * @Email: zhanglei03@111.com.cn
 */
@Slf4j
public final class ConvertUtils {

    public static Boolean toBool(String value) {
        return toBool(value, Boolean.FALSE);
    }

    public static Boolean toBool(String value, Boolean defaultValue) {
        if ((value == null) || (value.length() <= 0)) {
            return defaultValue;
        }
        value = value.trim().toLowerCase();
        if (value.length() <= 0) {
            return defaultValue;
        }
        if (("false".equals(value)) || ("0".equals(value))) {
            return Boolean.valueOf(false);
        }
        if (("true".equals(value)) || ("1".equals(value))) {
            return Boolean.valueOf(true);
        }
        if (log.isInfoEnabled()) {
            log.info("Unrecognized boolean string: " + value + ", use default value: true");
        }
        return Boolean.valueOf(true);
    }

    public static Boolean toBool(Object value) {
        return toBool(value, Boolean.FALSE);
    }

    public static Boolean toBool(Object value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> clazz = value.getClass();
        if (Boolean.class.equals(clazz)) {
            return (Boolean) value;
        }
        if (String.class.equals(clazz)) {
            return toBool(String.valueOf(value), defaultValue);
        }
        if (Integer.class.equals(clazz)) {
            return Boolean.valueOf(((Integer) value).intValue() != 0);
        }
        if (Byte.class.equals(clazz)) {
            return Boolean.valueOf(((Byte) value).byteValue() != 0);
        }
        if (Short.class.equals(clazz)) {
            return Boolean.valueOf(((Short) value).shortValue() != 0);
        }
        if (Long.class.equals(clazz)) {
            return Boolean.valueOf(((Long) value).longValue() != 0L);
        }
        if (Double.class.equals(clazz)) {
            return Boolean.valueOf((BigDecimal.valueOf(((double) value))).compareTo(BigDecimal.ZERO) != 0);
        }
        if (Float.class.equals(clazz)) {
            return Boolean.valueOf((BigDecimal.valueOf(((float) value))).compareTo(BigDecimal.ZERO) != 0);
        }
        if (BigDecimal.class.equals(clazz)) {
            return Boolean.valueOf(((BigDecimal) value).compareTo(BigDecimal.ZERO) != 0);
        }
        if (BigInteger.class.equals(clazz)) {
            return Boolean.valueOf(((BigInteger) value).compareTo(BigInteger.ZERO) != 0);
        }
        if (log.isInfoEnabled()) {
            log.info("can not convert " + clazz.getName() + " to boolean, use default value: "
                    + defaultValue);
        }
        return defaultValue;
    }

    public static Integer toInt(String value) {
        return toInt(value, 0);
    }

    public static Integer toInt(String value, Integer defaultValue) {
        if ((value == null) || (value.trim().length() <= 0)) {
            return defaultValue;
        }
        if (value.indexOf('.') >= 0) {
            try {
                Double d = Double.valueOf(value);
                return Integer.valueOf(d.intValue());
            } catch (Exception e) {
                log.warn("Can not convert \"" + value + "\" to Integer, use default value: "
                                + defaultValue,
                        e);

                return defaultValue;
            }
        }
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            log.warn(
                    "Can not convert \"" + value + "\" to Integer, use default value: " + defaultValue,
                    e);
        }
        return defaultValue;
    }

    public static Integer toInt(Object value) {
        return toInt(value, 0);
    }

    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> cls = value.getClass();
        if (Integer.class.equals(cls)) {
            return (Integer) value;
        }
        if ((value instanceof Number)) {
            return Integer.valueOf(((Number) value).intValue());
        }
        if (String.class.equals(cls)) {
            return toInt((String) value, defaultValue);
        }
        log.warn(
                "Can not convert " + cls.getName() + " to Integer, use default value: " + defaultValue);

        return defaultValue;
    }

    public static Long toLong(String value) {
        return toLong(value, 0L);
    }

    public static Long toLong(String value, Long defaultValue) {
        if ((value == null) || (value.trim().length() <= 0)) {
            return defaultValue;
        }
        if (value.indexOf('.') >= 0) {
            try {
                Double d = Double.valueOf(value);
                return Long.valueOf(d.longValue());
            } catch (Exception e) {
                log.warn(
                        "Can not convert \"" + value + "\" to Long, use default value: " + defaultValue,
                        e);

                return defaultValue;
            }
        }
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            log.warn(
                    "Can not convert \"" + value + "\" to Long, use default value: " + defaultValue, e);
        }
        return defaultValue;
    }

    public static Long toLong(Object value) {
        return toLong(value, 0L);
    }

    public static Long toLong(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> cls = value.getClass();
        if (Long.class.equals(cls)) {
            return (Long) value;
        }
        if ((value instanceof Number)) {
            return Long.valueOf(((Number) value).longValue());
        }
        if (String.class.equals(cls)) {
            return toLong((String) value, defaultValue);
        }
        log.warn(
                "Can not convert " + cls.getName() + " to Long, use default value: " + defaultValue);

        return defaultValue;
    }

    public static Float toFloat(String value) {
        return toFloat(value, 0F);
    }

    public static Float toFloat(String value, Float defaultValue) {
        if ((value == null) || (value.trim().length() <= 0)) {
            return defaultValue;
        }
        try {
            return Float.valueOf(value);
        } catch (Exception e) {
            log.warn(
                    "Can not convert \"" + value + "\" to Float, use default value: " + defaultValue,
                    e);
        }
        return defaultValue;
    }

    public static Float toFloat(Object value) {
        return toFloat(value, 0F);
    }

    public static Float toFloat(Object value, Float defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> cls = value.getClass();
        if (Float.class.equals(cls)) {
            return (Float) value;
        }
        if ((value instanceof Number)) {
            return Float.valueOf(((Number) value).floatValue());
        }
        if (String.class.equals(cls)) {
            return toFloat((String) value, defaultValue);
        }
        log.warn(
                "Can not convert " + cls.getName() + " to Float, use default value: " + defaultValue);

        return defaultValue;
    }

    public static Double toDouble(String value) {
        return toDouble(value, 0D);
    }

    public static Double toDouble(String value, Double defaultValue) {
        if ((value == null) || (value.trim().length() <= 0)) {
            return defaultValue;
        }
        try {
            return Double.valueOf(value);
        } catch (Exception e) {
            log.warn(
                    "Can not convert \"" + value + "\" to Double, use default value: " + defaultValue,
                    e);
        }
        return defaultValue;
    }

    public static Double toDouble(Object value) {
        return toDouble(value, 0D);
    }

    public static Double toDouble(Object value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> cls = value.getClass();
        if (Double.class.equals(cls)) {
            return (Double) value;
        }
        if ((value instanceof Number)) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        if (String.class.equals(cls)) {
            return toDouble((String) value, defaultValue);
        }
        log.warn(
                "Can not convert " + cls.getName() + " to Double, use default value: " + defaultValue);

        return defaultValue;
    }

    public static BigDecimal toDecimal(String value) {
        return toDecimal(value, BigDecimal.ZERO);
    }

    public static BigDecimal toDecimal(String value, BigDecimal defaultValue) {
        if ((value == null) || (value.trim().length() <= 0)) {
            return defaultValue;
        }
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            log.warn("Can not convert \"" + value + "\" to BigDecimal, use default value: "
                    + defaultValue);
        }
        return defaultValue;
    }

    public static BigDecimal toDecimal(Object value) {
        return toDecimal(value, BigDecimal.ZERO);
    }

    public static BigDecimal toDecimal(Object value, BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class clazz = value.getClass();
        if (BigDecimal.class.equals(clazz)) {
            return (BigDecimal) value;
        }
        if (Double.class.equals(clazz)) {
            return toDecimal(value.toString(), defaultValue);
        }
        if (Float.class.equals(clazz)) {
            return toDecimal(value.toString(), defaultValue);
        }
        if (String.class.equals(clazz)) {
            return toDecimal((String) value, defaultValue);
        }
        if (Integer.class.equals(clazz)) {
            return new BigDecimal(((Integer) value).intValue());
        }
        if (Short.class.equals(clazz)) {
            return new BigDecimal(((Short) value).shortValue());
        }
        if (Byte.class.equals(clazz)) {
            return new BigDecimal(((Byte) value).byteValue());
        }
        if (Long.class.equals(clazz)) {
            return new BigDecimal(((Long) value).longValue());
        }
        log.warn("Can not convert " + clazz.getName() + " to BigDecimal, use default value: "
                + defaultValue);

        return defaultValue;
    }
}
