package com.example.test_swagger;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
class TestSwaggerApplicationTests {

    @Test
    void contextLoads() {
        List<String> dates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dates.add(sdf.format(date));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -16);
        date = calendar.getTime();
        dates.add(sdf.format(date));
        for (String s : dates) {
            System.err.println(s);
        }
    }

    @Test
    void tescs() {
        String su = "152827199411251212";
        if (su.length() >= 18) {
            StringBuffer s = new StringBuffer(su);
            System.err.println(s.replace(14, 18, "****"));
        }
    }

    @Test
    void testSplit() {
        String fileName = "姓名：辉辉，出生年月：1999 年 10 月 08 日，性别：1，联系电话：15999696969，身份证或其他有效证件号码：152630199910083636，住址：当事人联系地址，工作单位：工作单位。";
        StringBuilder newName = new StringBuilder();
        if (fileName.contains("性别")) {
            String[] split = fileName.split("，");
            for (String s : split) {
                if (!s.contains("性别")) {
                    newName.append(s).append("，");
                    continue;
                } else {
                    if (s.contains("1")) {
                        String a = s.replace("1", "男");
                        newName.append(a).append("，");
                    } else {
                        String b = s.replace("2", "女");
                        newName.append(b).append("，");
                    }
                }
            }
            String s = newName.toString();
            System.err.println(s.substring(0, s.length() - 1));
        }
    }

    @Test
    void testJia() throws ParseException {
        sso();
    }

    public Integer sso() throws ParseException {
        // 获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String end = sdf.format(new Date());
        String end = "2022-01-03";
        String begin = end.substring(0, end.length() - 2);
        int assessTime = 4;
        if (assessTime < 10) {
            begin = begin + "0" + assessTime;
        } else {
            begin = begin + assessTime;
        }
        Date bt = sdf.parse(end);
        Date et = sdf.parse(begin);
        if (bt.before(et)) {
            System.out.println(bt + "小于" + et + "不可以修改");
        } else {
            System.out.println(bt + "大于" + et + "可以修改");

        }
        return 1;
    }

}
