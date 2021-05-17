package com.example.test_swagger.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.test_swagger.entity.PortrayQuery;
import org.apache.commons.collections4.map.HashedMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    /**
     * 根据日期获得所在周的日期
     * @param mdate
     * @return
     */
    @SuppressWarnings("deprecation")
    public static List<Date> dateToWeek(Date mdate) {
        int b = mdate.getDay();
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        Long fTime = mdate.getTime() - b * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a-1, fdate);
        }
        return list;
    }

    /**
     * 获得近一周的开始时间和结束时间
     *
     * @return
     */
    public static Map getDaySevenRange() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map condition = new HashedMap();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        condition.put("endDate", df.format(calendar.getTime()));
        calendar.set(Calendar.HOUR_OF_DAY, -168);
        condition.put("startDate", df.format(calendar.getTime()));
        return condition;
    }

    /**
     * 获取两个时间节点之间的月份列表
     **/
    private static List<String> getMonthBetween(String minDate, String maxDate) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月

            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

            Calendar curr = min;
            while (curr.before(max)) {
                result.add(sdf.format(curr.getTime()));
                curr.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 去除重复数据
     *
     * @param list
     * @return
     */
    public static List removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    /**
     * 获取近n月/年的时间跨度
     *
     * @return
     */
    public static String getBetweenTime(Integer n, String type) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //过去一月
        c.setTime(new Date());
        //判断查询类型
        switch (type) {
            //月
            case "0":
                c.add(Calendar.MONTH, -n);
                break;
            //年
            case "1":
                c.add(Calendar.YEAR, -n);
                break;
            default:
        }
        Date m = c.getTime();
        String result = format.format(m);
        return result;
    }

    public static PortrayQuery decideStatisticType(PortrayQuery query) {
        query.setDateEnd(getNowString());
        //判断查询类型
        String dateStart = getBetweenTime(1, "1");
        query.setDateStart(dateStart);
        return query;
    }

    public static String getNowString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(new Date());
        return result;
    }

    /**
     * 判断时间是否在某个区间内
     *
     * @param nowTime
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
