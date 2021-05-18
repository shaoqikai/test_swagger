package com.example.test_swagger.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号验证工具类
 *
 * @author shaoqk
 * @create 2021-05-18 11:06
 */
@Component
public class PhoneUtils {
    public static boolean isPhone(String str) {
        if (StringUtils.isBlank(str)) {
            throw new ArithmeticException("请输入手机号码或电话");
        }
        boolean flag = false;
        Matcher m = null;
        // 手机号11位
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        // 验证带区号的
        Pattern p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");
        // 验证不带区号的
        Pattern p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");

        if (str.length() < 9) {// 验证不带区号的
            m = p2.matcher(str);
            flag = m.matches();
        } else if (str.length() == 11) {
            m = p.matcher(str);
            flag = m.matches();
        } else {
            m = p1.matcher(str);
            flag = m.matches();
        }
        return flag;
    }
}
