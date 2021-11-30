/* ============================================================
 * StringUtil.java
 * Created: [2008-10-9] by YangHongyuan
 * ============================================================
 * ProjectName :ehr
 * Description:
 *
 * ==========================================================*/

package com.gangling.scm.base.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 字符串工具类
 * </p>
 *
 * @author xubo
 * @date 2008-9-4
 */

public class StringUtil extends StringUtils {
    /**
     * 返回一个对象的toString()
     *
     * @param obj 被处理的对象
     * @return 如果obj!=null 返回 obj.toString(),如果obj==null 返回 "";
     */
    public static String notNullString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 返回一个对象的toString()
     *
     * @param obj 被处理的对象
     * @param dft 当obj为null时的默认值
     * @return 如果obj!=null 返回 obj.toString(),如果obj==null 返回 notNullString(dft);
     */
    public static String notNullString(Object obj, Object dft) {
        return obj == null ? StringUtil.notNullString(dft) : obj.toString();
    }

    /**
     * 去掉以 suffix 结尾的部分
     *
     * @param original 原字符串
     * @param suffix   后缀
     * @return 返回去掉后缀的字符串
     */
    public static String cutSuffix(String original, String suffix) {
        if (original == null)
            return null;
        if (original.endsWith(suffix)) {
            int pos = original.lastIndexOf(suffix);
            return original.substring(0, pos);
        }
        return original;
    }

    /**
     * 根据文本框的显示长度来输出相应的字符串,文本显示长度是中文1个占位,西文0.5个占位
     *
     * @param original 原字串
     * @param size     文本框的长度
     * @return 结果字串
     */
    public static String cutWithTextSize(String original, int size) {
        if (original == null) {
            return null;
        }
        if (size < 0) {
            return original;
        }
        if (size == 0) {
            return "";
        }

        if (original.length() > size * 2) {
            original = original.substring(0, size * 2);
        }
        int lenofByte = original.getBytes().length;
        char[] chars = original.toCharArray();
        int len = chars.length;
        while (lenofByte > size * 2) {
            if (chars[--len] > 256)
                lenofByte -= 2;
            else
                lenofByte -= 1;
        }
        return original.substring(0, len);
    }

    /**
     * 字符串的首字符大写
     *
     * @param original 原字符串
     * @return 结果字串
     */
    public static String upperCaseFirstCharacter(String original) {
        if (original == null) {
            return original;
        }
        if (original.equals("")) {
            return original;
        }
        char[] chrs = original.toCharArray();
        chrs[0] = Character.toUpperCase(chrs[0]);
        return new String(chrs);
    }

    /**
     * 字符串的首字符小写
     *
     * @param original 原字符串
     * @return 结果字串
     */
    public static String lowerCaseFirstCharacter(String original) {
        if (original == null) {
            return original;
        }
        if (original.equals("")) {
            return original;
        }
        char[] chrs = original.toCharArray();
        chrs[0] = Character.toLowerCase(chrs[0]);
        return new String(chrs);
    }

    /**
     * 得到数据库中用来表识true和false的值
     *
     * @param value Boolean
     * @return String
     */
    public static String toSQLBooleanChar(Boolean value) {
        if (value == null)
            return null;
        if (value.booleanValue())
            return "Y";
        return "N";
    }

    /**
     * 解析字符串为boolean值
     *
     * @param s            原字符串
     * @param defaultValue 默认值
     * @return 当 s=="true" 或 s=="yes" 时, 返回 true; 当 s==null, 返回 defaultValue;
     * 否则, 返回 false;
     */
    public static boolean parseBoolean(String s, boolean defaultValue) {
        boolean b = defaultValue;
        if (s != null) {
            if ("true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "y".equalsIgnoreCase(s)) {
                b = true;
            } else
                b = false;
        }
        return b;
    }

    /**
     * 解析字符串为int值
     *
     * @param s            原字符串
     * @param defaultValue 默认值
     * @return 当s可以被解析时, 返回Integer.parseInt(s) 否则, 返回 defaultValue;
     */
    public static int parseInt(String s, int defaultValue) {
        int i = defaultValue;
        try {
            i = Integer.parseInt(s);
        } catch (Exception ex) {
        }
        return i;
    }

    /**
     * 解析字符串为long值
     *
     * @param s            原字符串
     * @param defaultValue 默认值
     * @return 当s可以被解析时, 返回Integer.parseInt(s) 否则, 返回 defaultValue;
     */
    public static long parseLong(String s, long defaultValue) {
        long i = defaultValue;
        try {
            i = Long.parseLong(s);
        } catch (Exception ex) {
        }
        return i;
    }

    /**
     * 解析字串为日期
     *
     * @param s String
     * @return java.util.Date
     * @throws java.text.ParseException
     */
    public static Date parseDate(String s) throws java.text.ParseException {
        if (s == null) {
            return null;
        }
        java.text.DateFormat f = java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT);
        return f.parse(s);
    }

    /**
     * 解析字符串为float值
     *
     * @param s            原字符串
     * @param defaultValue 默认值
     * @return 当s可以被解析时, 返回Float.parseFloat(s) 否则, 返回 defaultValue;
     */
    public static float parseFloat(String s, float defaultValue) {
        float i = defaultValue;
        try {
            i = Float.parseFloat(s);
        } catch (Exception ex) {
        }
        return i;
    }

    /**
     * 解析字符串为double值
     *
     * @param s            原字符串
     * @param defaultValue 默认值
     * @return 当s可以被解析时, 返回Integer.parseInt(s) 否则, 返回 defaultValue;
     */
    public static double parseDouble(String s, double defaultValue) {
        double i = defaultValue;
        try {
            i = Double.parseDouble(s);
        } catch (Exception ex) {
        }
        return i;
    }

    /**
     * 将字符串转换为Integer对象
     *
     * @param s 原字符串
     * @return 当符合10进制整数, 并且值不越界时, 返回相应对象, 否则返回null
     */
    public static Integer toInteger(String s) {
        try {
            return Integer.valueOf(s);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 将字符串转换为Double对象
     *
     * @param s 原字符串
     * @return 当符合10进制浮点数, 并且值不越界时, 返回相应对象, 否则返回null
     */
    public static Double toDouble(String s) {
        try {
            return Double.valueOf(s);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 将字符串转换为Float对象
     *
     * @param s 原字符串
     * @return 当符合10进制浮点数, 并且值不越界时, 返回相应对象, 否则返回null
     */
    public static Float toFloat(String s) {
        try {
            return Float.valueOf(s);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 将字符串转换为Long对象
     *
     * @param s 原字符串
     * @return 当符合10进制整数, 并且值不越界时, 返回相应对象, 否则返回null
     */
    public static Long toLong(String s) {
        try {
            return Long.valueOf(s);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 将字符串转换为Boolean对象
     *
     * @param s 原字符串
     * @return 当 s=="true" 或 s=="yes" 时, 返回 Boolean.True; 否则, 返回 Boolean.False;
     */
    public static Boolean toBoolean(String s) {
        if (s == null) {
            return null;
        }
        if ("true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "y".equalsIgnoreCase(s)) {
            return Boolean.TRUE;
        }
        Integer i = toInteger(s);
        if (i != null && i.intValue() > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static Timestamp toTimestamp(String str) {
        if ((str == null) || (str.trim().length() < 1)) {
            return null;
        }
        if (str.indexOf(" ") != -1)
            str = str.substring(0, str.indexOf(" "));
        StringTokenizer st = new StringTokenizer(str, "-");
        int year = Integer.parseInt(st.nextToken());
        int month = Integer.parseInt(st.nextToken());
        int day = Integer.parseInt(st.nextToken());

        return DateUtil.createTimestamp(year, month, day);

    }

    public static Timestamp toFullTimestamp(String str) {
        if ((str == null) || (str.trim().length() < 1)) {
            return null;
        }
        if (str.indexOf(" ") != -1)
            str = str.substring(0, str.indexOf(" "));
        StringTokenizer st = new StringTokenizer(str, "-");
        int year = Integer.parseInt(st.nextToken());
        int month = Integer.parseInt(st.nextToken());
        int day = Integer.parseInt(st.nextToken());

        return DateUtil.createTimestamp(year, month, day, 23, 59, 59);

    }

    public static BigDecimal toBigDecimal(String str) {
        try {
            return new BigDecimal(str);
        } catch (Exception ex) {
            return null;
        }

    }

    /**
     * 字符串替换(在jdk1.4 中有实现,这里是对jdk1.4之前版本的支持);
     *
     * @param str    原字符串
     * @param target 被替换的字符串
     * @param with   替换成的字符串
     * @return 返回结果, 当with为null时, 被处理为空字符串;
     */
    public static String replace(String str, String target, String with) {
        if (str == null) {
            return null;
        } else if ("".equals(str)) {
            return "";
        } else if (target == null || target.equals("")) {
            return str;
        }
        if (with == null) {
            with = "";
        }
        int len = target.length();
        int pos = str.indexOf(target);
        if (pos == -1) {
            return str;
        } else {
            return str.substring(0, pos) + with + replace(str.substring(pos + len), target, with);
        }
    }

    /**
     * 将 txt 文本以 HTML 格式输出,主要是对空格,换行和tab的替换,1tab=4空格
     *
     * @param str 原字符串
     * @return 格式化结果字符串, 当 str==null 时,返回""
     */
    public static String htmlFormat(String str) {
        if (str == null) {
            return "";
        }
        char[] chars = str.toCharArray();
        int len = chars.length;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            if (chars[i] == 10) {
                sb.append("<br>");
            } else if (chars[i] == 32) {
                sb.append("&nbsp;");
            } else if (chars[i] == 9) {
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 得到字符串的字节数
     *
     * @param str 字符串
     * @return 字符串的字节数, str为null时返回0;
     */
    public static int getByteCount(String str) {
        if (str == null) {
            return 0;
        }
        return str.getBytes().length;
    }

    /**
     * 得到字符串的字符数
     *
     * @param str 字符串
     * @return 字符串的字符数, str为null时返回0;
     */
    public static int getCharacterCount(String str) {
        if (str == null) {
            return 0;
        }
        return str.toCharArray().length;
    }

    /**
     * 格式化输出日期
     *
     * @param datetime 待格式化的日期
     * @param pattern  格式化的样式,如 "yyyy.MM.dd 'at' hh:mm:ss z"
     * @return 符合格式的字符串
     * @see SimpleDateFormat.format( Date );
     */
    public static String format(Date datetime, String pattern) {
        if (datetime == null)
            return "";
        SimpleDateFormat f = null;
        if (pattern != null) {
            f = new SimpleDateFormat(pattern);
        } else {
            f = new SimpleDateFormat();
        }
        return f.format(datetime);
    }

    /**
     * 格式化输出日期
     *
     * @param datetime 待格式化的日期
     * @param pattern  格式化的样式,如 "yyyy.MM.dd 'at' hh:mm:ss z"
     * @return 符合格式的字符串
     * @see format( Date, String );
     */
    public static String format(Timestamp datetime, String pattern) {
        if (datetime == null)
            return "";
        return format(new Date(datetime.getTime()), pattern);
    }

    /**
     * 格式化输出日期yyyymmdd
     *
     * @param date 待格式化的日期
     * @return 符合格式的字符串
     */
    public static String format(Date date) {
        if (date == null)
            return "";
        String rs = format(date, "yyyy-MM-dd");
        rs = rs.replaceAll("-", "");
        return rs;
    }

    /**
     * 格式化输出日期yyyymmdd
     *
     * @param datetime 待格式化的日期
     * @return 符合格式的字符串
     */
    public static String format(Timestamp datetime) {
        if (datetime == null)
            return "";
        String rs = format(new Date(datetime.getTime()));
        return rs;
    }

    /**
     * 格式化输出日期
     *
     * @param datetime 待格式化的日期
     * @param pattern  格式化的样式,如 "yyyy.MM.dd 'at' hh:mm:ss z"
     * @return 符合格式的字符串
     * @see format( Date, String );
     */
    public static String format(Calendar datetime, String pattern) {
        if (datetime == null)
            return "";
        return format(datetime.getTime(), pattern);
    }

    /**
     * 格式化输出数字
     *
     * @param number  待格式化的数字
     * @param pattern 格式化的样式,如 "$#,###"
     * @return 符合格式的字符串
     * @see DecimalFormat.format(long number);
     */
    public static String format(long number, String pattern) {
        DecimalFormat f = null;
        if (pattern != null) {
            f = new DecimalFormat(pattern);
        } else {
            f = new DecimalFormat();
        }
        return f.format(number);
    }

    /**
     * 格式化输出数字
     *
     * @param number  待格式化的数字
     * @param pattern 格式化的样式,如 "$#,###"
     * @return 符合格式的字符串
     * @see format(long, String );
     */
    public static String format(Integer number, String pattern) {
        if (number == null)
            return "";
        return format(number.longValue(), pattern);
    }

    /**
     * 格式化输出数字
     *
     * @param number  待格式化的数字
     * @param pattern 格式化的样式,如 "$#,###"
     * @return 符合格式的字符串
     * @see format(long, String );
     */
    public static String format(int number, String pattern) {
        return format((long) number, pattern);
    }

    /**
     * 格式化输出数字
     *
     * @param number  待格式化的数字
     * @param pattern 格式化的样式,如 "$#,###"
     * @return 符合格式的字符串
     * @see format(long, String );
     */
    public static String format(Long number, String pattern) {
        if (number == null)
            return "";
        return format(number.longValue(), pattern);
    }

    /**
     * 格式化输出数字
     *
     * @param number  待格式化的数字
     * @param pattern 格式化的样式,如 "#,##0.0#"
     * @return 符合格式的字符串
     * @see DecimalFormat.format(double);
     */
    public static String format(double number, String pattern) {
        DecimalFormat f = null;
        if (pattern != null) {
            f = new DecimalFormat(pattern);
        } else {
            f = new DecimalFormat();
        }
        return f.format(number);
    }

    /**
     * 格式化输出数字
     *
     * @param number  待格式化的数字
     * @param pattern 格式化的样式,如 "#,##0.0#"
     * @return 符合格式的字符串
     * @see format( Double , String )
     */
    public static String format(Double number, String pattern) {
        if (number == null)
            return "";
        return format(number.doubleValue(), pattern);
    }

    /**
     * 格式化输出数字
     *
     * @param number  待格式化的数字
     * @param pattern 格式化的样式,如 "#,##0.0#"
     * @return 符合格式的字符串
     * @see format( Double , String )
     */
    public static String format(Float number, String pattern) {
        if (number == null)
            return "";
        return format(number.doubleValue(), pattern);
    }

    /**
     * 格式化输出数字
     *
     * @param number  待格式化的数字
     * @param pattern 格式化的样式,如 "#,##0.0#"
     * @return 符合格式的字符串
     * @see format( Double , String )
     */
    public static String format(float number, String pattern) {
        return format((double) number, pattern);
    }

    /**
     * 将数组对象以字符串方式输出
     *
     * @param objs Object[]
     * @param dlim String 分隔符
     * @return String
     */
    public static String combine(Object[] objs, String dlim) {
        if (objs == null || objs.length == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < objs.length; i++) {
            sb.append(objs[i]).append(dlim);
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 将集合以字符串方式输出
     *
     * @param col  Collection
     * @param dlim String 分隔符
     * @return String
     */
    public static String combine(Collection col, String dlim) {
        if (col == null || col.size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (Iterator iter = col.iterator(); iter.hasNext(); ) {
            sb.append((Object) iter.next()).append(dlim);
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static String combine(Map map, String dlim) {
        if (map == null || map.size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
            Object item = (Object) iter.next();
            sb.append(item).append('=').append(map.get(item)).append(dlim);
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 判断一个字串是否为空字串, null或0长度
     *
     * @param s String
     * @return boolean
     */
    public static boolean isEmpty(String s) {
        if (s == null || s.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * 判断多个字符串中是否至少有一个为空字串, null或0长度
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String... s) {
        if (null == s || s.length == 0) {
            return false;
        }
        for (String str : s) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个字串是否为非空字串, null或0长度
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 打印字符串的编码
     *
     * @param s   String
     * @param out PrintStream
     */
    public static void printStringCode(String s, java.io.PrintStream out) {
        if (s == null || s.length() == 0) {
            out.println("The String is empty");
            return;
        }
        out.print("The String is:");
        out.println(s);
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            out.print(Integer.toHexString((short) chars[i]));
            out.print(",");
        }
        out.println();
    }

    /**
     * 得到本地编码的字符串
     *
     * @param s String
     * @return String
     */
    public static String nativeEncode(String s) {
        if (s == null)
            return null;
        try {
            return new String(s.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException ex) {
            return s;
        }
    }

    /**
     * 本地编码转换为Unicode
     *
     * @param s String
     * @return String
     */
    public static String native2Unicode(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        byte[] buffer = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            buffer[i] = (byte) s.charAt(i);
        }
        return new String(buffer);
    }

    /**
     * Unicode转本地编码
     *
     * @param s String
     * @return String
     */
    public static String unicode2Native(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        char[] buffer = new char[s.length() * 2];
        char c;
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= 0x100) {
                c = s.charAt(i);
                byte[] buf = ("" + c).getBytes();
                buffer[j++] = (char) buf[0];
                buffer[j++] = (char) buf[1];
            } else {
                buffer[j++] = s.charAt(i);
            }
        }
        return new String(buffer, 0, j);
    }

    /**
     * 金额转换所用方法 供NumToRMBStr调用
     *
     * @param NumStr String
     * @return String
     */
    public static String positiveIntegerToHanStr(String NumStr) { // 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
        String HanDigiStr[] = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

        String HanDiviStr[] = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰",
                "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾",
                "佰", "仟"};

        String RMBStr = "";
        boolean lastzero = false;
        boolean hasvalue = false; // 亿、万进位前有数值标记
        int len, n;
        len = NumStr.length();
        if (len > 15)
            return "数值过大!";
        for (int i = len - 1; i >= 0; i--) {
            if (NumStr.charAt(len - i - 1) == ' ')
                continue;
            n = NumStr.charAt(len - i - 1) - '0';
            if (n < 0 || n > 9)
                return "输入含非数字字符!";

            if (n != 0) {
                if (lastzero)
                    RMBStr += HanDigiStr[0]; // 若干零后若跟非零值，只显示一个零
                // 除了亿万前的零不带到后面
                // if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) ) //
                // 如十进位前有零也不发壹音用此行
                if (!(n == 1 && (i % 4) == 1 && i == len - 1)) // 十进位处于第一位不发壹音
                    RMBStr += HanDigiStr[n];
                RMBStr += HanDiviStr[i]; // 非零值后加进位，个位为空
                hasvalue = true; // 置万进位前有值标记

            } else {
                if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) { // 亿万之间必须有非零值方显示万
                    RMBStr += HanDiviStr[i]; // “亿”或“万”
                    hasvalue = false;
                }
            }
            if ((i % 8) == 0 || (i % 8) == 4)
                hasvalue = false;
            lastzero = (n == 0) && (i % 4 != 0); // 亿万前有零后不加零，如：拾万贰仟
        }

        if (RMBStr.length() == 0)
            return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
        return RMBStr;
    }

    /**
     * 金额由数值型转为人民币大写
     *
     * @param val double
     * @return String
     */
    public static String numToRMBStr(double val) {
        String HanDigiStr[] = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

        String HanDiviStr[] = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰",
                "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾",
                "佰", "仟"};

        String SignStr = "";
        String TailStr = "";
        long fraction, integer;
        int jiao, fen;

        if (val < 0) {
            val = -val;
            SignStr = "负";
        }
        if (val > 99999999999999.999 || val < -99999999999999.999)
            return "数值位数过大!";
        // 四舍五入到分
        long temp = Math.round(val * 100);
        integer = temp / 100;
        fraction = temp % 100;
        jiao = (int) fraction / 10;
        fen = (int) fraction % 10;
        if (jiao == 0 && fen == 0) {
            TailStr = "整";
        } else {
            TailStr = HanDigiStr[jiao];
            if (jiao != 0)
                TailStr += "角";
            if (integer == 0 && jiao == 0) // 零圆后不写零几分
                TailStr = "";
            if (fen != 0)
                TailStr += HanDigiStr[fen] + "分";
        }

        // 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零圆叁分”
        // if( !integer ) return SignStr+TailStr;

        return SignStr + positiveIntegerToHanStr(String.valueOf(integer)) + "圆" + TailStr;
    }

    /**
     * 将纯文本转换成html格式进行显示
     *
     * @param text
     * @return
     */
    public static String textToHtml(String text) {
        if (isEmpty(text)) {
            return text;
        }
        text = StringEscapeUtils.escapeHtml(text);
        text = text.replaceAll(" ", "&nbsp;");
        text = text.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        text = text.replaceAll("\n", "<br />");
        return text;
    }

    /**
     * 将对象转换为指定长度的字符串,对象toString之后的长度小于length,则自动用defaultChar填充
     *
     * @param o
     * @param length
     * @param defaultChar
     * @return
     */
    public static String convertToString(Object o, int length, String defaultChar) {
        String r = o.toString();
        int currentLength = r.length();
        if (currentLength < length) {
            StringBuffer sb = new StringBuffer(length);
            for (int i = 0; i < length - currentLength; i++) {
                sb.append(defaultChar);
            }
            sb.append(r);
            r = sb.toString();
        }
        return r;
    }

    /**
     * 校验身份证号码
     *
     * @param idCode
     * @return
     */
    public static Boolean validateIdCode(String idCode) {
        if (idCode == null) {
            return false;
        }
        String idCardRegex = "^[1-9]\\d{5}[1]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}\\w$|^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        if (!idCode.matches(idCardRegex)) {
            return false;
        }

        Date birthday = null;
        if (idCode != null && idCode.length() > 0) {
            String birthdayStr = null;
            if (idCode.length() == 18) {
                birthdayStr = idCode.substring(6, 14);
            } else if (idCode.length() == 15) {
                birthdayStr = "19" + idCode.substring(6, 12);
            }
            birthday = DateUtil.valueOf(birthdayStr, "yyyyMMdd");
        }
        if (birthday == null) {
            return false;
        }

        return true;
    }

    /**
     * 根据身份证号码取得出生日期
     *
     * @param idCode
     * @return
     */
    public static Date getBirthDateFromIdCode(String idCode) {
        if (!validateIdCode(idCode)) {
            throw new IllegalArgumentException();
        }
        Date birthday = null;
        if (idCode != null && idCode.length() > 0) {
            String birthdayStr = null;
            if (idCode.length() == 18) {
                birthdayStr = idCode.substring(6, 14);
            } else if (idCode.length() == 15) {
                birthdayStr = "19" + idCode.substring(6, 12);
            }
            birthday = DateUtil.valueOf(birthdayStr, "yyyyMMdd");
        }

        return birthday;
    }

    /**
     * 格式化输出日志
     *
     * @param logInfos
     * @return
     */
    public static String formatLog(Map<String, Object> logInfos) {
        String separator = System.getProperty("line.separator");
        StringBuffer formatInfos = new StringBuffer();
        if (null == logInfos || 0 == logInfos.size()) {
            return formatInfos.append("[]").append(separator).toString();
        }
        formatInfos.append("[");
        Set<String> attributes = logInfos.keySet();
//        boolean flag = false;
        for (String attribute : attributes) {
//            if (flag) {
//                formatInfos.append(",");
//                flag = true;
//            }
            formatInfos.append(attribute).append(" = ").append(logInfos.get(attribute));
        }
        return formatInfos.append("]").append(separator).toString();
    }

    /**
     * 取得一个字符串在另一个字符串中出现的次数
     *
     * @param source
     * @param toFind
     * @return
     */
    public static int countStr(String source, String toFind) {
        if (source == null) {
            return 0;
        }
        int count = 0;
        int index = source.indexOf(toFind);
        while (index != -1) {
            source = source.substring(index + toFind.length());
            index = source.indexOf(toFind);
            count++;
        }
        return count;
    }

    /**
     * <pre>
     * 在字符串中每隔一定长度(英文1个字符，中文2个字符)插入某个字符
     * </pre>
     *
     * @param resource
     * @param size
     * @param insertStr
     * @return
     */
    public static String insertStr2Str(String resource, int size, String insertStr) {
        String returnStr = "";

        char[] chars = resource.toCharArray();
        int i = 0;
        for (char c : chars) {
            returnStr = returnStr + c;

            i = i + (c > 256 ? 2 : 1);
            if (i >= size) {
                returnStr = returnStr + insertStr;
                i = 0;
            }
        }
        return returnStr;
    }

    /**
     * <pre>
     * 判断第一个参数是否等于后续的参数中的某一个
     * </pre>
     *
     * @param value
     * @param value1
     * @param strs
     * @return
     */
    public static boolean isIn(String value, String value1, String... strs) {
        if (value.equals(value1))
            return true;

        if (strs != null && strs.length > 0)
            for (String str : strs) {
                if (str.equals(value))
                    return true;
            }
        return false;
    }

    /**
     * <pre>
     * 判断第一个参数是否等于后续的参数中的某一个
     * </pre>
     *
     * @param value
     * @param value1
     * @param strs
     * @return
     */
    public static boolean isInArrays(String value, String... strs) {
        if (strs != null && strs.length > 0)
            for (String str : strs) {
                if (str.equals(value))
                    return true;
            }
        return false;
    }

    /**
     * <pre>
     * 判断第一个参数是否不等于后续的所有参数
     * </pre>
     *
     * @param value
     * @param value1
     * @param strs
     * @return
     */
    public static boolean isNotIn(String value, String value1, String... strs) {
        return !isIn(value, value1, strs);
    }

    /**
     * 去除字符串中的回车和换行符
     *
     * @param str
     * @return
     */
    public static String replaceLineBreaks(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\r|\\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 将src字符串前补placeholder到length长度
     *
     * @param src
     * @param placeholder
     * @param length
     * @return
     */
    public static String formatStr(String src, String placeholder, int length) {
        if (length < 0) {
            return src;
        }
        int srcLen = src.length();
        StringBuffer sb = new StringBuffer();
        int addCount = length - srcLen;
        if (addCount < 0) {
            return src;
        }
        for (int i = 0; i < addCount; i++) {
            sb.append(placeholder);
        }
        return sb.append(src).toString();
    }

    /**
     * 分隔字符
     *
     * @param str
     * @param pattern
     * @return
     */
    public static Set<String> splitStrAndReturnSet(String str, String pattern) {
        if (isEmpty(str)) {
            return Collections.emptySet();
        }
        String[] split = str.split(pattern);
        Set<String> resultList = new HashSet<String>();
        for (String s : split) {
            resultList.add(s.trim());
        }
        return resultList;
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.isIn("a", "b"));
        System.out.println(StringUtil.isIn("a", "b", "c"));
        System.out.println(StringUtil.isIn("a", "b", "c", "d", "a"));

        System.out.println(StringUtil.isNotIn("a", "b"));
        System.out.println(StringUtil.isNotIn("a", "b", "c"));
        System.out.println(StringUtil.isNotIn("a", "b", "c", "d", "a"));
    }
}
