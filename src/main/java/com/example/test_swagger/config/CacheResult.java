package com.example.test_swagger.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shaoqk
 * @create 2021-01-25 15:11
 */
@Retention(RetentionPolicy.RUNTIME)// 设置生命周期
@Target({ElementType.TYPE, ElementType.METHOD})// 设置使用位置
public @interface CacheResult {
    String key();

    boolean needKey() default false;
}
