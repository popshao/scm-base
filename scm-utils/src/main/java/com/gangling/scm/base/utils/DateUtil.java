package com.gangling.scm.base.utils;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * <b> 日期操作工具类 </b>
 * </p>
 * <p>
 * 常用日期方法
 * </p>
 *
 * @author Paulson Tian
 * @version 1.0
 */

public class DateUtil {
    public static final String DAY = "day";
    public static final String WEEK = "week";
    public static final String MONTH = "month";
    public static final String YESTERDAY = "yesterday";
    public static final String LASTWEEK = "lastWeek";
    public static final String LASTMONTH = "lastMonth";


    /**
     * Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of December in the year 2002
     */
    public static final String ISO_DATE_FORMAT = "yyyyMMdd";

    /**
     * Expanded ISO 8601 Date format yyyy-MM-dd i.e., 2002-12-25 for the 25th day of December in the year 2002
     */
    public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy年MM月dd日
     */
    public static final String CHINESE_EXPANDED_DATE_FORMAT = "yyyy年MM月dd日";

    /**
     * Default lenient setting for getDate.
     */
    private static boolean LENIENT_DATE = false;

    /**
     * <p>
     * Description:得到当前时间 不包括时间,分,秒
     * </p>
     *
     * @return Date
     */
    public static Date getNowDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
                .get(Calendar.DATE), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return convertCalToTs(cal);
    }

    /**
     * 获得明天的日期(明天中午)
     *
     * @return 指定日期的下一天 格式:YYYY-MM-DD
     */
    public static Date getTomorrow() {
        Date date = new Date(System.currentTimeMillis());
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.DATE, 1);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        return gc.getTime();
    }

    /**
     * 获取当前的日期，不包括时间
     *
     * @return
     */
    public static java.sql.Date getTodayDate() {
        Calendar c = Calendar.getInstance();
        java.sql.Date now = new java.sql.Date(c.getTimeInMillis());
        return now;
    }

    /**
     * <p>
     * Description:得到当前时间 包括时间,分,秒
     * </p>
     *
     * @return Timestamp
     */
    public static Timestamp getNowTime() {
		/*Session hibernateSession = (Session) Component.getInstance("hibernateSession");
		String hql = "select current_timestamp from UserAccount c";
		Query query = hibernateSession.createQuery(hql);
		Timestamp nowTime = (Timestamp) query.iterate().next();
		return nowTime;*/
		/*Session hibernateSession = (Session) Component.getInstance("hibernateSession");
		String hql = "select current_timestamp from dual";
		Connection conn = hibernateSession.connection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(hql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Timestamp time = rs.getTimestamp("current_timestamp");
				return time;
			}
		} catch (SQLException e) {
		} finally {
			if (rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}*/
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * <p>
     * Description:把Calendar类型转换为Timestamp
     * </p>
     *
     * @param Calendar
     * @return Timestamp
     */
    public static Timestamp convertCalToTs(Calendar cald) {
        return new Timestamp(cald.getTime().getTime());
    }

    /**
     * <p>
     * Description:把Timestamp类型转换为Calendar
     * </p>
     *
     * @param Timestamp
     * @return Calendar
     */
    public static Calendar convertTsToCal(Timestamp ts) {
        Calendar cald = Calendar.getInstance();
        cald.setTime(new Date(ts.getTime()));
        return cald;
    }

    /**
     * <p>
     * Description:把Timestamp类型转换为Date
     * </p>
     *
     * @param Timestamp
     * @return Date
     */
    public static Date convertTsToDt(Timestamp ts) {
        return new Date(ts.getTime());
    }

    /**
     * <p>
     * Description:把Date类型转换为Timestamp
     * </p>
     *
     * @param Date
     * @return Timestamp
     */
    public static Timestamp convertDtToTs(Date dt) {
        return new Timestamp(dt.getTime());
    }

    /**
     * <p>
     * Description:把Timestamp类型转换为String类型,返回数据截至到日期
     * </p>
     *
     * @param Timestamp
     * @return String
     */
    public static String convertTsToStr(Timestamp ts) {
        if (ts != null)
            return ts.toString().substring(0, 10);
        return "";
    }

    /**
     * <p>
     * Description:把Timestamp类型转换为String类型,返回数据截至到秒,格式为2003-02-05 14:23:05
     * </p>
     *
     * @param Timestamp
     * @return String
     */
    public static String convertTsToStrWithSecs(Timestamp ts) {
        if (ts != null)
            return ts.toString().substring(0, 19);
        return "";
    }

    /**
     * <p>
     * Description:把Timestamp类型转换为String类型,返回数据带有星期几显示,格式为2003-11-04 星期二
     * </p>
     *
     * @param Timestamp
     * @return String
     */
    public static String convertTsToStrWithDayOfWeek(Timestamp ts) {
        if (ts != null)
            return ts.toString().substring(0, 10) + " " + getStrDay(ts);
        return "";
    }

    /**
     * <p>
     * Description:根据传入的参数生成一个日期类型
     * </p>
     *
     * @param year :年份如1999 month:月份如3月（为实际需要创建的月份） date:日期如25
     * @param hour :小时如15 minute:分钟如25 second:秒如34
     * @return String
     */
    public static Timestamp createTimestamp(int year, int month, int date,
                                            int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return convertCalToTs(cal);
    }

    /**
     * <p>
     * Description:根据传入的参数生成一个日期类型
     * </p>
     *
     * @param year :年份如1999 month:月份如3月（为实际需要创建的月份） date:日期如25
     * @return String
     */
    public static Timestamp createTimestamp(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return convertCalToTs(cal);
    }

    /**
     * @param str      传入的字符串格式如2003-02-01,2003/02/01
     * @param splitStr 传入字符串当中的分隔符 '-' '/'
     * @return Timestamp 日期类型
     */
    public static Timestamp createTimestamp(String str, String splitStr) {
        if ((str == null) || (str.trim().length() < 1)) {
            return null;
        }
        if ("".equals(splitStr)) {
            splitStr = "-";
        }
        if (str.lastIndexOf(" ") != -1)
            str = str.substring(0, 10);
        try {
            StringTokenizer st = new StringTokenizer(str, splitStr);
            int year = Integer.parseInt(st.nextToken());
            int month = Integer.parseInt(st.nextToken());
            int date = Integer.parseInt(st.nextToken());
            return createTimestamp(year, month, date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param ts 日期类型
     * @return 传入日期的对应年数
     */
    public static int getYear(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.YEAR);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期的对应月份
     */
    public static int getMonth(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.MONTH) + 1;
    }

    /**
     * @param ts 日期类型
     * @return 传入日期的对应日期
     */
    public static int getDate(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应小时数
     */
    public static int getHour(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应分钟数
     */
    public static int getMinute(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.MINUTE);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应秒数
     */
    public static int getSecond(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.SECOND);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应秒中的毫秒数
     */
    public static int getMillisecond(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.MILLISECOND);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应毫秒数
     */
    public static long getMilliseconds(Timestamp ts) {
        return ts.getTime();
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应星期数，为数字1为星期天2为星期一依次类推
     */
    public static int getDay(Timestamp ts) {
        return convertTsToCal(ts).get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param ts 日期类型
     * @return 传入日期类型的对应星期几，返回为中文，如星期一星期二等
     */
    public static String getStrDay(Timestamp ts) {
        if (ts == null)
            return null;
        int day = getDay(ts);
        String weekDay = "";
        switch (day) {
            case 1: {
                weekDay = "星期天";
            }
            break;
            case 2: {
                weekDay = "星期一";
            }
            break;
            case 3: {
                weekDay = "星期二";
            }
            break;
            case 4: {
                weekDay = "星期三";
            }
            break;
            case 5: {
                weekDay = "星期四";
            }
            break;
            case 6: {
                weekDay = "星期五";
            }
            break;
            case 7: {
                weekDay = "星期六";
            }
            break;
            default:
                weekDay = "";
                break;
        }
        return weekDay;
    }

    /**
     * @param addpart为添加的部分可以为yy年数mm月份数dd天数hh小时数mi分钟数ss秒数
     * @param ts                                          需要改动的日期类型
     * @param addnum                                      添加数目 可以为负数
     * @return 改动后的日期类型
     */
    public static Timestamp dateAdd(String addpart, Timestamp ts, int addnum) {
        Calendar cal = Calendar.getInstance();
        cal = convertTsToCal(ts);
        if ("yy".equals(addpart)) {
            cal.add(Calendar.YEAR, addnum);
        } else if ("mm".equals(addpart)) {
            cal.add(Calendar.MONTH, addnum);
        } else if ("dd".equals(addpart)) {
            cal.add(Calendar.DATE, addnum);
        } else if ("hh".equals(addpart)) {
            cal.add(Calendar.HOUR, addnum);
        } else if ("mi".equals(addpart)) {
            cal.add(Calendar.MINUTE, addnum);
        } else if ("ss".equals(addpart)) {
            cal.add(Calendar.SECOND, addnum);
        } else if ("mini".equals(addpart)) {
            cal.add(Calendar.MILLISECOND, addnum);
        } else {
            return null;
        }
        return convertCalToTs(cal);
    }

    /**
     * @param diffpart                      比较部分YEAR为比较年份 MONTH为比较月份 DATE为比较天数 WEEK比较星期数
     * @param 如果传入的diffpart参数不为以上范围默认返回相差天数
     * @param ts1                           需要比较的日期
     * @param ts2                           需要比较的日期
     * @return 相差的大小
     */
    public static int dateDiff(String diffpart, Timestamp ts1, Timestamp ts2) {
        if ((ts1 == null) || (ts2 == null)) {
            return -1;
        }

        Date date1 = null;
        Date date2 = null;

        date1 = new Date(ts1.getTime());
        date2 = new Date(ts2.getTime());

        Calendar cal1 = null;
        Calendar cal2 = null;

        cal1 = Calendar.getInstance();
        cal2 = Calendar.getInstance();

        // different date might have different offset
        cal1.setTime(date1);
        long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET)
                + cal1.get(Calendar.DST_OFFSET);

        cal2.setTime(date2);
        long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET)
                + cal2.get(Calendar.DST_OFFSET);

        // Use integer calculation, truncate the decimals
        int hr1 = (int) (ldate1 / 3600000); // 60*60*1000
        int hr2 = (int) (ldate2 / 3600000);

        int days1 = (int) hr1 / 24;
        int days2 = (int) hr2 / 24;

        int dateDiff = days2 - days1;
        int weekOffset = (cal2.get(Calendar.DAY_OF_WEEK) - cal1
                .get(Calendar.DAY_OF_WEEK)) < 0 ? 1 : 0;
        int weekDiff = dateDiff / 7 + weekOffset;
        int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
        int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH)
                - cal1.get(Calendar.MONTH);

        if ("YEAR".equals(diffpart)) {
            return yearDiff;
        } else if ("MONTH".equals(diffpart)) {
            return monthDiff;
        } else if ("DATE".equals(diffpart)) {
            return dateDiff;
        } else if ("WEEK".equals(diffpart)) {
            return weekDiff;
        } else {
            return dateDiff;
        }
    }

    /**
     * @param ts
     * @return true表示ts为闰年 false表示ts不是闰年
     */
    public static boolean isLeapyear(Timestamp ts) {
        Calendar cal = Calendar.getInstance();
        boolean booleanleapYear = ((GregorianCalendar) cal)
                .isLeapYear(getYear(ts));
        return booleanleapYear;
    }

    /**
     * @param year 年
     * @return true表示year为闰年 false表示ts不是闰年
     */
    public static boolean isLeapyear(int year) {
        Calendar cal = Calendar.getInstance();
        boolean booleanleapYear = ((GregorianCalendar) cal).isLeapYear(year);
        return booleanleapYear;
    }

    /**
     * 比较传入的年月日是否与ts对应为同一天
     *
     * @param year  年份 传入－1表示不需要比较
     * @param month 月份 传入－1表示不需要比较
     * @param date  日期 传入－1表示不需要比较
     * @param ts    完成的Timestamp日期类型
     * @return
     */
    public static boolean isMatchDate(int year, int month, int date,
                                      Timestamp ts) {
        int year1 = getYear(ts);
        int month1 = getMonth(ts);
        int date1 = getDay(ts);

        if ((year != -1) && (year != year1)) {
            return false;
        }
        if ((month != -1) && (month != month1)) {
            return false;
        }
        if ((date != -1) && (date != date1)) {
            return false;
        }

        return true;
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 取得 与 [指定日期所在周] [相隔n周] 的那一周 的第一天
     *
     * @param date
     * @param nWeeks
     * @return
     */
    public static Date getFirstWeekDayByGivenDay(Date date, int nWeeks) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.add(Calendar.DATE, nWeeks * 7);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    /**
     * 去除时间后面的小时分秒，只取具体时间日期
     *
     * @param ts
     * @return
     */
    public static Timestamp formatToDate(Timestamp ts) {
        Calendar cd = Calendar.getInstance();
        cd.setTimeInMillis(ts.getTime());
        cd.set(Calendar.HOUR_OF_DAY, 0);
        cd.set(Calendar.MINUTE, 0);
        cd.set(Calendar.SECOND, 0);
        cd.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cd.getTime().getTime());
    }

    /**
     * 去除时间后面的小时分秒，只取具体时间日期
     *
     * @param dt
     * @return
     */
    public static Date formatToDate(Date dt) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(dt);
        cd.set(Calendar.HOUR_OF_DAY, 0);
        cd.set(Calendar.MINUTE, 0);
        cd.set(Calendar.SECOND, 0);
        cd.set(Calendar.MILLISECOND, 0);
        return cd.getTime();
    }

    /**
     * 去除时间后面的小时分秒，只取具体时间日期
     *
     * @param dt
     * @return
     */
    public static java.sql.Date formatToDate(java.sql.Date dt) {
        Calendar cd = Calendar.getInstance();
        cd.setTimeInMillis(dt.getTime());
        cd.set(Calendar.HOUR_OF_DAY, 0);
        cd.set(Calendar.MINUTE, 0);
        cd.set(Calendar.SECOND, 0);
        cd.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(cd.getTime().getTime());
    }

    /**
     * 传入Timestamp类型日期 返回中文大写日期 如贰零零肆年五月拾捌日
     *
     * @param ts Timestamp
     * @return String
     */
    public static String formatDateToCn(Timestamp ts) {
        if (ts == null)
            return "";
        int year = getYear(ts);
        int month = getMonth(ts);
        int date = getDate(ts);

        String hanDigiStr[] = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆",
                "柒", "捌", "玖", "拾"};

        String cnDate = "";

        String tempYear = String.valueOf(year);
        String tempMonth = String.valueOf(month);
        String tempDate = String.valueOf(date);

        if (tempYear.length() != 4)
            return "";

        for (int i = 0; i < tempYear.length(); i++) {
            cnDate += hanDigiStr[new Integer(tempYear.substring(i, i + 1))
                    .intValue()];
        }
        cnDate += "年";

        if (month < 1 || month > 12 || date < 1 || date > 31)
            return "";

        cnDate += StringUtil.positiveIntegerToHanStr(tempMonth);
        cnDate += "月";
        cnDate += StringUtil.positiveIntegerToHanStr(tempDate);
        cnDate += "日";

        return cnDate;
    }

    /**
     * 取得指定月的上个月。
     *
     * @return
     */
    public static String getLastMonth(String yearM) {
        String yymmdd = yearM + "01";
        Date date = DateUtil.valueOf(yymmdd, ISO_DATE_FORMAT);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, -1);
        //设置时间为0时
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);

        String lastMonth = dateToString(cal.getTime(), ISO_DATE_FORMAT);

        return lastMonth.substring(0, lastMonth.length() - 2);
    }

    /**
     * 取得指定月的前第十二个月。
     *
     * @return
     */
    public static String getBeforTwelveMonth(String yearM) {
        String yymmdd = yearM + "01";
        Date date = DateUtil.valueOf(yymmdd, ISO_DATE_FORMAT);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, -12);
        //设置时间为0时
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);

        String lastMonth = dateToString(cal.getTime(), ISO_DATE_FORMAT);

        return lastMonth.substring(0, lastMonth.length() - 2);
    }

    /**
     * 根据时间变量返回时间字符串 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return dateToString(date, ISO_EXPANDED_DATE_FORMAT);
    }

    /**
     * 根据时间变量返回时间字符串
     *
     * @param pattern 时间字符串样式
     * @param date    时间变量
     * @return 返回时间字符串
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return null;
        }

        try {
            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);
            return sfDate.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串转换为日期java.util.Date
     *
     * @param dateText 字符串
     */
    public static Date valueOf(String dateString) {

        return valueOf(dateString, ISO_EXPANDED_DATE_FORMAT, LENIENT_DATE);
    }

    /**
     * 字符串转换为日期java.util.Date
     *
     * @param dateText 字符串
     * @param format   日期格式
     * @return
     */
    public static Date valueOf(String dateString, String format) {

        return valueOf(dateString, format, LENIENT_DATE);
    }

    /**
     * 字符串转换为日期java.util.Date
     *
     * @param dateText 字符串
     * @param format   日期格式
     * @param lenient  日期越界标志
     * @return
     */
    public static Date valueOf(String dateText, String format,
                               boolean lenient) {

        if (dateText == null) {

            return null;
        }

        DateFormat df = null;

        try {

            if (format == null) {
                df = new SimpleDateFormat();
            } else {
                df = new SimpleDateFormat(format);
            }

            // setLenient avoids allowing dates like 9/32/2001
            // which would otherwise parse to 10/2/2001
            df.setLenient(false);

            return df.parse(dateText);
        } catch (ParseException e) {

            return null;
        }
    }

    /**
     * 根据当前月向前取月份。月份跨度由span指定
     *
     * @return 年月。For example:200907
     */
    public static String getBeforeMonth(Date currentDate, int span) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(currentDate);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, -span);

        // 设置时间为0时
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        String lastMonth = dateToString(cal.getTime(), "yyyyMM");

        return lastMonth;
    }

    /**
     * 根据当前月向前取月份。月份跨度由span指定
     *
     * @return 年月。For example:200907
     */
    public static String getAfterMonth(Date currentDate, int span) {

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(currentDate);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, span);

        // 设置时间为0时
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        String lastMonth = dateToString(cal.getTime(), "yyyyMM");

        return lastMonth;
    }

    /**
     * 根据当前向后 。月份跨度由span指定
     *
     * @return
     */
    public static Date getDateBeforeMonth(Date currentDate, int span) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MONTH, span);
        return cal.getTime();
    }
//

    /**
     * 将毫秒数换算成x天x时x分x秒x毫秒
     *
     * @param ms
     * @return
     */
    public static String format(long ms) {// 将毫秒数换算成x天x时x分x秒x毫秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;
        return strDay + "天," + strHour + "小时," + strMinute + "分钟," + strSecond + "秒";
    }

    /**
     * 将毫秒数换算成x时x分x秒x毫秒(劳动力管理算工时用)
     *
     * @param ms
     * @return
     */
    public static String Longtimeformat(long ms) {// 将毫秒数换算成x时x分x秒x毫秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;

        long hour = ms / hh;
        long minute = (ms - hour * hh) / mi;
        long second = (ms - hour * hh - minute * mi) / ss;
        long milliSecond = ms - hour * hh - minute * mi - second
                * ss;

        String strHour = "" + hour;
        String strMinute = "" + minute;
        String strSecond = "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;

        String result = strHour + "小时" + strMinute + "分钟" + strSecond + "秒";
        ;
        if (hour == 0) {
            result = strMinute + "分钟" + strSecond + "秒";
        }
        if (hour == 0 && minute == 0) {
            result = strSecond + "秒";
        }
        return result;
    }

    /**
     * 获取第i天得日期
     *
     * @param date
     * @param i
     */
    public static Date getDateAfterIDay(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        return c.getTime();
    }

    public static Date convertCalToTs(Date date, int hour, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);

        return c.getTime();
    }

    /**
     * 设置给定日期的时分秒
     *
     * @param c      需要调整的日期
     * @param hour   设定的小时值
     * @param minute 设定的分钟值
     * @param second 设定的秒值
     */
    public static void setHMS(Calendar c, int hour, int minute, int second) {
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
    }

    /**
     * 设置给定日期的时分秒毫秒
     *
     * @param c           需要调整的日期
     * @param hour        设定的小时值
     * @param minute      设定的分钟值
     * @param second      设定的秒值
     * @param milliSecond 设定的毫秒秒值
     */
    public static void setHMSM(Calendar c, int hour, int minute, int second, int milliSecond) {
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, milliSecond);
    }

    /**
     * 获取当天的几点钟的java.util.Date格式
     *
     * @param hour 24小时制
     * @return
     */
    public static Date getGivenTimeOfToday(int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * 获取距当前时间 dayCount天后的几点钟的java.util.Date格式
     *
     * @param hour 24小时制
     * @return
     */
    public static Date getGivenTimeAfterSomeDay(int hour, int minute, int second, int dayCount) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dayCount);// 把日期往后增加一天.整数往后推,负数往前移动
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * 获取距指定时间 dayCount天后的几点钟的java.util.Date格式
     *
     * @param hour 24小时制
     * @return
     */
    public static Date getGivenTimeAfterSomeDay(int hour, int minute, int second, Date date, int dayCount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, dayCount);// 把日期往后增加一天.整数往后推,负数往前移动
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * 是否是HH:mm:ss格式，48小时制，小时位数字不能超过47
     *
     * @param timeStr
     * @return
     */
    public static boolean isValidTime(String timeStr) {
        if (timeStr == null || timeStr.length() != 8) {
            return false;
        }
        if (!":".equals(timeStr.substring(2, 3)) || !":".equals(timeStr.substring(5, 6))) {
            return false;
        }
        try {
            int hour = Integer.parseInt(timeStr.substring(0, 2));
            if (hour < 0 || hour > 47) {
                return false;
            }
            int minute = Integer.parseInt(timeStr.substring(3, 5));
            if (minute < 0 || minute > 59) {
                return false;
            }
            int second = Integer.parseInt(timeStr.substring(6, 8));
            if (second < 0 || second > 59) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 是否是HH:mm:ss格式，24小时制<br/>
     * dateStrArr里若有一个不是正确格式，则返回false
     *
     * @param dateStr
     * @return
     */
    public static boolean isValidTime(String... dateStrArr) {
        if (dateStrArr == null || dateStrArr.length == 0) {
            return false;
        }
        for (String dateStr : dateStrArr) {
            if (!isValidTime(dateStr)) {
                return false;
            }
        }
        return true;
    }

    public static void checkTime(String testTime) throws Exception {
        if (!DateUtil.isValidTime(testTime)) {
            throw new Exception(testTime);
        }
    }

    /**
     * 如果是次日的时间，即超过午夜24点整的，考虑加前缀，比如“次日”<br/>
     * 不是HH:mm:ss格式则报错
     *
     * @param srcTime
     * @return
     * @throws Exception
     */
    public static String addTimePrefix(String srcTime, String prefix) throws Exception {
        if (StringUtil.isEmpty(srcTime)) {
            return srcTime;
        }
        String hour = srcTime.substring(0, 2);
        int hourInt = Integer.parseInt(hour);
        if (hourInt >= 24) {
            int newHourInt = hourInt - 24;
            if (newHourInt < 10) {
                prefix = prefix + "0";
            }
            return prefix + newHourInt + srcTime.substring(2);
        }
        return srcTime;
    }

    /**
     * 上月月初
     *
     * @return
     */
    public static Date getFirstDayOfPrevMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
                .get(Calendar.DATE), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 上月月末
     *
     * @return
     */
    public static Date getLastDayOfPrevMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
                .get(Calendar.DATE), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 根据当前月向前取月份。月份跨度由span指定
     *
     * @return 年月。For example:2009-07
     */
    public static String getAfterMonthWithSign(Date currentDate, int span) {

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(currentDate);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, span);

        // 设置时间为0时
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        String lastMonth = dateToString(cal.getTime(), "yyyy-MM");

        return lastMonth;
    }

    public static Integer getDayByMonthStr(String yyyyMM) {
        // 格式化日期--设置date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(yyyyMM));
        } catch (ParseException e) {
            return 0;
        }
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateFormat(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (pattern == null) {
            pattern = DATETIME_PATTERN;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 获取两个日期区间的所有天数字符串
     *
     * @param dateFrom
     * @param dateTo
     * @return
     */
    public static List<String> getEveryDay(Date dateFrom, Date dateTo) {
        if (dateFrom == null || dateTo == null) {
            return null;
        }
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(dateFrom);
        long diffDays = (dateTo.getTime() - dateFrom.getTime()) / 1000 / 60 / 60 / 24;
        List<String> dayList = new LinkedList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i <= diffDays; i++) {
            dayList.add(sdf.format(calFrom.getTime()));
            calFrom.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dayList;
    }

    public static String dateToString(Date date, String pattern, Locale locale) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat sfDate = new SimpleDateFormat(pattern, locale);
            sfDate.setLenient(false);
            return sfDate.format(date);
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * 得到星期几
     *
     * @param date
     * @return
     */
    public static int getWeekToNumber(Date date) {
        List<String> weekDays = Arrays.asList("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日");
        return weekDays.indexOf(dateFormatToString(date, "EEEE")) + 1;
    }

    public static String dateFormatToString(Date date, String format) {
        if (null == date)
            return null;
        if (null == format) {
            format = DATETIME_PATTERN;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static void checkHourSecTime(String testTime) throws Exception {
        if (!DateUtil.isValidHourSecTime(testTime)) {
            throw new Exception(testTime);
        }
    }

    /**
     * 是否是HH:mm:格式，24小时制
     *
     * @param timeStr
     * @return
     */
    public static boolean isValidHourSecTime(String timeStr) {
        if (timeStr == null || timeStr.length() != 5) {
            return false;
        }
        if (!":".equals(timeStr.substring(2, 3))) {
            return false;
        }
        try {
            int hour = Integer.parseInt(timeStr.substring(0, 2));
            if (hour < 0 || hour > 24) {
                return false;
            }
            int minute = Integer.parseInt(timeStr.substring(3, 5));
            if (minute < 0 || minute > 59) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Boolean timeStrFormat(String dateStr, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = ISO_EXPANDED_DATE_FORMAT;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.parse(dateStr);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Date日期的操作
     *
     * @param field 参考Calendar属性
     * @param amount 操作数
     */
    public static Date operating(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }
}
