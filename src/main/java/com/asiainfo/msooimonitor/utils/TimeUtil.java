package com.asiainfo.msooimonitor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author H
 * @Date 2019/1/10 10:29
 * @Desc
 **/
public class TimeUtil {

    private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_DAY_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT_SQL = "yyyyMMddHHmmss";
    public static final String DATE_HOUR_FORMAT_SQL = "yyyyMMddHH";
    public static final String DATE_DAY_FORMAT_SQL = "yyyyMMdd";
    public static final String DATE_MONTH_FORMAT_SQL = "yyyyMM";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String LONG_TIME_FORMAT = "yyyyMMddHHmmss";


    /**
     * 字符串转日期格式
     * @param str
     * @return Date
     */
    public static Date strToDate(String str){
        SimpleDateFormat format = new SimpleDateFormat(DATE_DAY_FORMAT_SQL);
        Date date=null;
        try {
            date= format.parse(str);
        } catch (ParseException e) {
            logger.error("日期转换失败{}!!!!",e.getMessage());
        }
        return date;
    }

    /**
     *将日期格式转化为yyyyMMddHHmmssSSS
     * @param date
     * @return
     */
    public static String getLongSeconds(Date date){
        SimpleDateFormat format = new SimpleDateFormat(LONG_TIME_FORMAT);

        return format.format(date);

    }

    public static String getDateTimeFormat(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);

        return format.format(date);

    }

    /**
     * 将日期格式转化为yyyyMMdd
     * @param date
     * @return
     */
    public static String getDaySql(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_DAY_FORMAT_SQL);

        return format.format(date);
    }

    /**
     * 获取获取指定日期前一天
     * @param date
     * @return
     */
    public static String getLastDaySql(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_DAY_FORMAT_SQL);

        return format.format(new Date(date.getTime() - 1 * 24 * 60 * 60 * 1000));
    }

    /**
     * 获取指定日期前两天
     * @param date
     * @return
     */
    public static String getTwoDaySql(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_DAY_FORMAT_SQL);

        return format.format(new Date(date.getTime() - 2 * 24 * 60 * 60 * 1000));
    }

    /**
     * 获取指定日期三天前
     * @param date
     * @return
     */
    public static String getThreeDaySql(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_DAY_FORMAT_SQL);

        return format.format(new Date(date.getTime() - 3 * 24 * 60 *60 * 1000));

    }

    /**
     * 获取当前月份yyyyMM
     * @param date
     * @return
     */
    public static String getMonthSql(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_MONTH_FORMAT_SQL);

        return format.format(date);
    }

    /**
     * 获取指定日期前一个月
     * @param date
     * @return
     */
    public static String getLastMonthSql(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_MONTH_FORMAT_SQL);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,-1);

        Date time = calendar.getTime();

        return format.format(time);
    }

    public static String getTwoMonthSql(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_MONTH_FORMAT_SQL);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,-2);
        Date time = calendar.getTime();

        return format.format(time);

    }

    /**
     * 字符串转时间
     * @param str 要转化的字符串
     * @param type 日期格式
     * @return
     */
    public static Date str2Date(String str, String type){
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date date=null;
        try {
             date = format.parse(str);
        } catch (ParseException e) {
            logger.error("字符串转换时间失败！！！！");
        }

        return date;
    }



    /**
     * 判断今天是几号
     * @return
     */
    public static int getDay(){
        Calendar instance = Calendar.getInstance();

        int i = instance.get(Calendar.DATE);
        return i;
    }

    public static int getWeek(){
        Calendar instance = Calendar.getInstance();
        int i = instance.get(Calendar.DAY_OF_WEEK);
        return i;
    }

    public static void main(String[] args) {
        System.out.println(getWeek());
    }
}
