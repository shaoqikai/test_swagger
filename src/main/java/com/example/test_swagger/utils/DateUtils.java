package com.example.test_swagger.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author shaoqk
 * @create 2021-01-20 15:29
 * 时间工具类
 */
public class DateUtils {

    /**
     * 距离12点还有多长时间的方法
     * 返回当前日期格式化后的字符串
     *
     * @return 格式化后的日期
     */
    public static long getHours() {
        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(),midnight);
        if(hours<=0L){
            return 1L;
        }
        return hours;
    }

    /**
     * 获取当前时间去年同月月初
     */
    public static String getLastYear(){
        boolean leapYear = DateUtil.isLeapYear(DateUtil.year(new Date())-1);
        int year = -365;
        if(leapYear){
            year = -366;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        DateTime newDate3 = cn.hutool.core.date.DateUtil.offsetDay(cn.hutool.core.date.DateUtil.beginOfMonth(new Date()), year);
        String beginDate = format.format(newDate3);
        return beginDate;
    }

    /**
     * string时间转为Date
     *
     * @param time
     * @param type 0-年月日时分秒 1-年月日
     * @return
     */
    public static Date StringChangeDate(String time, String type) {
        SimpleDateFormat sdf;
        if (type.equals("0")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 判断时间是否超过24小时
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static boolean judgmentDate(Date date1, Date date2) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date start = date1;
        Date end = date2;
        long cha = end.getTime() - start.getTime();
        if (cha < 0) {
            return false;
        }

        double result = cha * 1.0 / (1000 * 60 * 60);

        if (result <= 24) {
            return true;
        } else {
            return false;
        }
    }
}
