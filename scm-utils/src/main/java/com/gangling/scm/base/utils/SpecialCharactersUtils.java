package com.gangling.scm.base.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 特殊字符处理类
 *
 * @author xiaowei
 * @date 2019年8月21日
 */
public class SpecialCharactersUtils {

    /**
     * 处理电话特殊字符问题
     *
     * @param str
     * @return
     */
    public static String clean(String str) {
        if (StringUtils.isNotBlank(str)) {
            str = removeNonAscii(str);
            str = removeSomeControlChar(str);
            str = removeFullControlChar(str).trim();
            return str;
        }
        return "";
    }

    /**
     * 去除非ascii码字符
     *
     * @param str
     * @return
     */
    public static String removeNonAscii(String str) {
        return str.replaceAll("[^\\x00-\\x7F]", "");
    }

    /**
     * 去除不可打印字符
     *
     * @param str
     * @return
     */
    public static String removeNonPrintable(String str) {
        return str.replaceAll("[\\p{C}]", "");
    }

    /**
     * 去除一些控制字符 Control Char
     *
     * @param str
     * @return
     */
    public static String removeSomeControlChar(String str) {
        return str.replaceAll("[\\p{Cntrl}\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", ""); // Some Control Char
    }

    /**
     * 去除一些换行制表符
     *
     * @param str
     * @return
     */
    public static String removeFullControlChar(String str) {
        return removeNonPrintable(str).replaceAll("[\\r\\n\\t]", "");
    }


}
