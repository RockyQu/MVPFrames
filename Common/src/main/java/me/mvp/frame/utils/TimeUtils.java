package me.mvp.frame.utils;

import me.mvp.frame.utils.base.BaseUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期时间工具类
 */
public class TimeUtils extends BaseUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat TIME = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public TimeUtils() {
        super();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return 格式化后的时间
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return 格式化后的时间
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return 格式化后的当前时间
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return String
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 获取月的天数
     *
     * @param year  Year
     * @param month Month
     * @return Returns the maximum value of the given field for the current
     * date. For example, the maximum number of days in the current
     * month.
     */
    public static int getMonthOfDayCount(String year, String month) {
        Calendar calendar = new GregorianCalendar();
        try {
            Date date = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse(year + "-" + month);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 格式化日期
     *
     * @param rule 格式规则
     * @param time 时间戳
     * @return 格式化后的日期
     */
    public static String formatDate(String rule, long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return new SimpleDateFormat(rule, Locale.getDefault()).format(calendar.getTime());
    }

    /**
     * 格式化当前日期
     *
     * @param rule 格式规则
     * @return 格式化后的日期
     */
    public static String formatDate(String rule) {
        return new SimpleDateFormat(rule, Locale.getDefault()).format(new Date());
    }

    /**
     * Get Calendar
     *
     * @return Calendar
     */
    public static int getCalendar(int field) {
        return Calendar.getInstance().get(field);
    }

    /**
     * 获取当前年份
     *
     * @return 年份
     */
    public static int getYear() {
        return getCalendar(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return 月份
     */
    public static int getMonth() {
        return getCalendar(Calendar.MONTH) + 1;
    }

    /**
     * 获取本月第N天
     *
     * @return 月份
     */
    public static int getDayOfMonth() {
        return getCalendar(Calendar.DAY_OF_MONTH);
    }
}
