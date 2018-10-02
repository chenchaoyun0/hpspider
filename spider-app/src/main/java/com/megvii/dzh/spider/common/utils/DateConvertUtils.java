package com.megvii.dzh.spider.common.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
/**
 * 
 * @author chenchaoyun
 * @date 2018/06/10
 */
public class DateConvertUtils {

  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_TIME_NO_SS = "yyyy-MM-dd HH:mm";
  public static final String DATE_TIME_SSSS = "yyyy-MM-dd HH:mm:ss:SSS";
  public static final String DATE_TIME_SSSS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public static final String DATE_TIME_UTC_SSSZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS Z"; // 转换yyyy-MM-ddTHH:mm:ss.SSS UTC
  public static final  long nd = 1000 * 24 * 60 * 60;
  public static final  long nh = 1000 * 60 * 60;
  public static final  long nm = 1000 * 60;

  /**
   * 日期格式化，默认格式yyyy-MM-dd.
   */
  public static String format(Date date) {
    return format(date, DATE_FORMAT);
  }

  /**
   * 日期格式化.
   */
  public static String format(Date date, String format) {
    if (date == null) {
      return null;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(date);
  }

  public static String formatDateTime(Date date) {
    return format(date, DATE_TIME_FORMAT);
  }

  public static String formatDateTimeToMillisecond(Date date) {
    return format(date, DATE_TIME_FORMAT);
  }

  /**
   * 字符串格式化为date.
   * @param date
   * @param format
   * @return
   */
  public static Date parse(String date, String format) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    if (StringUtils.isEmpty(date)) {
      return null;
    }
    try {
      return dateFormat.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 字符串格式化为date.
   * @param date
   * @return
   */
  public static Date parse(String date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    if (StringUtils.isEmpty(date)) {
      return null;
    }
    try {
      return dateFormat.parse(date);
    } catch (ParseException e) {
      e.getMessage();
    }
    return null;
  }

  /**
   * 计算日期相差时间.
   * @param t1
   * @param t2
   * @return
   */
  public static long diffMinutes(Date t1, Date t2) {
    long tms1 = t1.getTime();
    long tms2 = t2.getTime();

    return (tms2 - tms1) / (1000 * 60);
  }
  /**
   * 计算日期相差小时.
   * @param t1
   * @param t2
   * @return
   */
  public static long diffHours(Date t1, Date t2) {
    long diff = t2.getTime() - t1.getTime();
    // 计算差多少小时
    return  diff/ nh;
  }

  /**
   * 计算日期相差天数.
   * @param t1
   * @param t2
   * @return
   */
  public static long diffDayes(Date t1, Date t2) {
    long tms1 = t1.getTime();
    long tms2 = t2.getTime();

    return (tms2 - tms1) / (1000 * 60 * 60 * 24);
  }
  /**
   * 计算日期相差天数.带一位小数
   * @param t1
   * @param t2
   * @return
   */
  public static BigDecimal diffDayesBigDecimal(Date t1, Date t2,int scale) {
    long diffHours = diffHours(t1, t2);
    BigDecimal divide =
        new BigDecimal(diffHours).divide(new BigDecimal(24), scale, BigDecimal.ROUND_HALF_EVEN);
    return divide;
  }

  /**
   * 求开始截至日期之间的天数差.
   *
   * @param d1 开始日期
   * @param d2 截至日期
   * @return 返回相差天数
   */
  public static int getDaysInterval(Date d1, Date d2) {
    if (d1 == null || d2 == null) {
      return 0;
    }
    Date[] d = new Date[2];
    d[0] = d1;
    d[1] = d2;
    Calendar[] cal = new Calendar[2];
    for (int i = 0; i < cal.length; i++) {
      cal[i] = Calendar.getInstance();
      cal[i].setTime(d[i]);
      cal[i].set(Calendar.HOUR_OF_DAY, 0);
      cal[i].set(Calendar.MINUTE, 0);
      cal[i].set(Calendar.SECOND, 0);
    }
    long m = cal[0].getTime().getTime();
    long n = cal[1].getTime().getTime();
    int ret = (int) Math.abs((m - n) / 1000 / 3600 / 24);
    return ret;
  }

  /**
   * 指定日期加或减days天.
   */
  public static Date addDay(Date date, int days) {
    Calendar ins = Calendar.getInstance();
    ins.setTime(date);
    ins.add(Calendar.DAY_OF_YEAR, days);
    return ins.getTime();
  }

  /**
   * 指定日期加或减months月.
   */
  public static Date addMonth(Date date, int months) {
    Calendar ins = Calendar.getInstance();
    ins.setTime(date);
    ins.add(Calendar.MONTH, months);
    return ins.getTime();
  }

  /**
   * 获取日期所在月份第一天.
   */
  public static Date getFirstDayOfMonth(int year, int month) {
    Calendar cal = Calendar.getInstance();
    // 设置年份
    cal.set(Calendar.YEAR, year);
    // 设置月份
    cal.set(Calendar.MONTH, month - 1);
    // 设置天
    int firstDay = 1;
    // 设置日历中月份的天数
    cal.set(Calendar.DAY_OF_MONTH, firstDay);

    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    return cal.getTime();
  }

  /**
   * 获取日期所在月份第一天.
   */
  public static Date getFirstDayOfMonth(Date date) {
    Calendar calReal = Calendar.getInstance();
    calReal.setTime(date);

    Calendar cal = Calendar.getInstance();
    // 设置年份
    cal.set(Calendar.YEAR, calReal.get(Calendar.YEAR));
    // 设置月份
    cal.set(Calendar.MONTH, calReal.get(Calendar.MONTH));
    // 设置天
    int firstDay = 1;
    // 设置日历中月份的天数
    cal.set(Calendar.DAY_OF_MONTH, firstDay);

    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    return cal.getTime();
  }

  /**
   * 获取日期所在月份最后一天.
   */
  public static Date getLastDayOfMonth(int year, int month) {
    Calendar cal = Calendar.getInstance();
    // 设置年份
    cal.set(Calendar.YEAR, year);
    // 设置月份
    cal.set(Calendar.MONTH, month - 1);
    // 获取某月最大天数
    int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    // 设置日历中月份的最大天数
    cal.set(Calendar.DAY_OF_MONTH, lastDay);

    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    return cal.getTime();
  }

  /**
   * 获取日期所在月份最后一天.
   */
  public static Date getLastDayOfMonth(Date date) {
    Calendar calReal = Calendar.getInstance();
    calReal.setTime(date);

    Calendar cal = Calendar.getInstance();
    // 设置年份
    cal.set(Calendar.YEAR, calReal.get(Calendar.YEAR));
    // 设置月份
    cal.set(Calendar.MONTH, calReal.get(Calendar.MONTH));
    // 获取某月最大天数
    int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    // 设置日历中月份的最大天数
    cal.set(Calendar.DAY_OF_MONTH, lastDay);

    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    return cal.getTime();
  }

  /**
   * 获取日期所在月份总天数.
   */
  public static int getDaysOfMonth(int year, int month) {
    Calendar cal = Calendar.getInstance();
    // 设置年份
    cal.set(Calendar.YEAR, year);
    // 设置月份
    cal.set(Calendar.MONTH, month - 1);
    // 获取某月最大天数
    int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    return days;
  }

  /**
   * 获取日期所在月份总天数.
   */
  public static int getDaysOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    // 设置年份
    cal.setTime(date);
    // 获取某月最大天数
    int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    return days;
  }

  /**
   * 获取日期所在月份天数.
   */
  public static int getDay(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    // 获取某月最大天数
    int day = cal.get(Calendar.DAY_OF_MONTH);

    return day;
  }

  /**
   * 获取日期所在月份.
   */
  public static int getMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    int month = cal.get(Calendar.MONTH);

    return month;
  }

  /**
   * 获取日期所在年.
   */
  public static int getYear(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    int year = cal.get(Calendar.YEAR);

    return year;
  }

  /**
   * 获取凌晨时间.
   */
  public static Date getMidnight(Date date) {
    Calendar calReal = Calendar.getInstance();
    calReal.setTime(date);

    Calendar cal = Calendar.getInstance();
    // 设置年份
    cal.set(Calendar.YEAR, calReal.get(Calendar.YEAR));
    // 设置月份
    cal.set(Calendar.MONTH, calReal.get(Calendar.MONTH));
    // 设置日历中月份的天数
    cal.set(Calendar.DAY_OF_MONTH, calReal.get(Calendar.DAY_OF_MONTH));

    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    return cal.getTime();
  }

  /**
   * 获取一天的开始时间和结束时间.
   */

  public static Timestamp[] getDayBeginAndEndTime(Date date) {
    final Timestamp[] timestamps = new Timestamp[2];

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    timestamps[0] = new Timestamp(calendar.getTimeInMillis());

    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    timestamps[1] = new Timestamp(calendar.getTimeInMillis());
    return timestamps;
  }

  /**
   * 获取一天的开始时间和结束时间.
   */
  public static Timestamp[] getDayBeginAndEndTime(String date) {

    return getDayBeginAndEndTime(parse(date));
  }

  /**
   * 转换格式为2018-01-15T08:40:59.125Z为日期
   */

  public static Date parseUTCTime(String utcTime) {
    Date date = new Date();
    utcTime = utcTime.replace("Z", " UTC"); //注意UTC前有空格
    SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_UTC_SSSZ_FORMAT);//注意格式化的表达式
    try {
      date = format.parse(utcTime);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }
  /**
   * 获取日期所在周的星期一 00:00:00
   */
  public static Date getThisWeekMonday(Date date) {  
    Calendar cal = Calendar.getInstance();  
    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
    return cal.getTime(); 
  }
  
  public static void main(String[] args) {
    Date date = new Date();
    Date parse = parse("2018-03-20 10:00:00",DATE_TIME_FORMAT);
    BigDecimal diffDayesBigDecimal = diffDayesBigDecimal(parse, date,2);
    System.out.println(diffDayesBigDecimal);
  }
  
}
