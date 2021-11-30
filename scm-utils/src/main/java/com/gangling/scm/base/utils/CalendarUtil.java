package com.gangling.scm.base.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: dengxincheng
 * @Date: 2021/7/12 15:04
 * @Des:
 */
public class CalendarUtil {


    /**
     * 获取当前时间的前几天
     * @param date
     * @param diff
     * @return
     */
    public static Date getDateBeforeDiff(Date date, Integer diff) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, -diff);
        return cal.getTime();
    }


    /**
     * 判断当前时间是不是大于当日八点钟
     * @return
     */
    public static Boolean hasAfter8Clock() {
        Calendar eightClock = Calendar.getInstance();
        eightClock.set(Calendar.HOUR_OF_DAY, 8);
        eightClock.set(Calendar.MINUTE, 0);
        eightClock.set(Calendar.SECOND, 0);
        Calendar now = Calendar.getInstance();
        return now.after(eightClock);
    }

    /**
     * 获取传入日期的前一天凌晨0点
     * @param date
     * @return
     */
    public static Date getYesterdayWee(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);//减1天
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getYesterdayLast(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);//减1天
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getToDayWee(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取明日凌晨
     * @param date
     * @return
     */
    public static Date getToDayLast(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
