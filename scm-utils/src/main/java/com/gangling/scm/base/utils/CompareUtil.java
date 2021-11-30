package com.gangling.scm.base.utils;

/**
 * <pre>
 * Long类型比较
 * </pre>
 */
public class CompareUtil {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static boolean compare(Comparable a, Comparable b) {
        if (a == null) {
            return b == null;
        } else {
            return b == null ? Boolean.FALSE : a.compareTo(b) == 0;
        }
    }

    /*
     * 支持参数可能为空的Comparable对象进行比较
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static int compare(Comparable a, Comparable b, boolean nullIsBigger) {
        if (a == null) {
            if (b == null)
                return 0;
            else
                return nullIsBigger ? 1 : -1;
        } else {
            if (b == null)
                return nullIsBigger ? -1 : 1;
            else
                return a.compareTo(b);
        }
    }

    public static <T> boolean isIn(T c, T... values) {
        if (values.length >= 1) {
            for (T value : values) {
                if (!value.equals(c)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static <T> boolean isNotIn(T c, T... values) {
        if (values.length >= 1) {
            for (T value : values) {
                if (value.equals(c)) {
                    return false;
                }
            }
        }

        return true;
    }
}