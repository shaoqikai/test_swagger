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
     * @return 格式化后的日期
     */
    public static long getHours() {
        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), midnight);
        if (hours <= 0L) {
            return 1L;
        }
        return hours;
    }

    /**
     * @return 获取当前时间去年同月月初
     */
    public static String getLastYear() {
        boolean leapYear = DateUtil.isLeapYear(DateUtil.year(new Date()) - 1);
        int year = -365;
        if (leapYear) {
            year = -366;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateTime newDate3 = cn.hutool.core.date.DateUtil.offsetDay(cn.hutool.core.date.DateUtil.beginOfMonth(new Date()), year);
        return format.format(newDate3);
    }

    /**
     * string时间转为Date
     * @param time String 时间
     * @param type 0-年月日时分秒 1-年月日
     * @return 返回Date类型时间
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
     * @param date1 开始时间
     * @param date2 结束时间
     * @return 在区间内返回true
     */
    public static boolean judgmentDate(Date date1, Date date2) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long cha = date2.getTime() - date1.getTime();
        if (cha < 0) {
            return false;
        }

        double result = cha * 1.0 / (1000 * 60 * 60);

        return result <= 24;
    }

    /**
     * @param mdate 入参时间
     * @return 根据日期获得所在周的日期
     */
    @SuppressWarnings("deprecation")
    public static List<Date> dateToWeek(Date mdate) {
        int b = mdate.getDay();
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        long fTime = mdate.getTime() - (long) b * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a - 1, fdate);
        }
        return list;
    }

    /**
     * @return 获得近一周的开始时间和结束时间
     */
    public static Map<String, Object> getDaySevenRange() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> condition = new HashedMap<>();
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

            while (min.before(max)) {
                result.add(sdf.format(min.getTime()));
                min.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 去除重复数据
     * @param list 入参
     * @return 返回去重后数据
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
     * 获取近n月/年的时间跨度  例如获取当前时间往前推2个月   n = 2  type = 0
     * @param n    代表跨度
     * @param type 0是月 1是年
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
        return format.format(m);
    }

    /**
     * @param query type = 0 往前推一个月   type = 1 往前推一年
     * @return 返回开始时间和结束时间
     */
    public static PortrayQuery decideStatisticType(PortrayQuery query) {
        query.setDateEnd(getNowString());
        //判断查询类型
        String dateStart = getBetweenTime(1, query.getType());
        query.setDateStart(dateStart);
        return query;
    }

    /**
     * @return 把当前时间转为String
     */
    public static String getNowString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    /**
     * 判断时间是否在某个区间内
     * @param nowTime   现在时间
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @return 在区间内就返回true
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

        return date.after(begin) && date.before(end);
    }

    /**
     * @param num 往前推几个月num就是几
     * @return 获取月份列表
     */
    public static List<String> getMonthList(Integer num) {
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (int i = num; i > 0; i--) {
            Calendar calendar = Calendar.getInstance();
            //获取当前时间的前6个月
            calendar.add(Calendar.MONTH, -i);
            //将calendar装换为Date类型
            Date date = calendar.getTime();
            System.out.println(sdf.format(date));
            list.add(sdf.format(date));
        }
        return list;
    }
}
