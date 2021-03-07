package com.example.test_swagger.config.impl;

import com.example.test_swagger.config.CacheResult;
import com.example.test_swagger.service.KaiService;

import java.lang.reflect.Method;

/**
 * @author shaoqk
 * @create 2021-01-25 15:49
 */
public class CacheResultContent {
    /**
     * 通过反射获取注解信息
     */
    public void getAnno() {
        Class<KaiService> classzz = KaiService.class;
        Class<CacheResult> annoClass = CacheResult.class;
        if (classzz.isAnnotationPresent(annoClass)) {// 是否有这个注解
            CacheResult annotation = classzz.getAnnotation(annoClass);
            System.out.println(annotation.key());
        }

        Method[] methods = classzz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annoClass)) {
                CacheResult annotation = method.getAnnotation(annoClass);
                System.out.println(annotation.key());
            }
        }
    }
}
