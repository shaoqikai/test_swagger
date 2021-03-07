package com.example.test_swagger.config.impl;

import com.example.test_swagger.config.CacheResult;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;


/**
 * @author shaoqk
 * @create 2021-01-25 15:48
 * ApplicationContextAware  Spring核心容器对象，实现后具备容器能力，获取上下文
 * ApplicationListener  Spring监听
 * ContextRefreshedEvent    Spring中的事件，容器重启后发出的事件
 */
public class AnnotationContext implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;


    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Class<CacheResult> crClass = CacheResult.class;
        // 从容器中获取加了CacheResult注解的所有bean
        Map<String, Object> annotationBeans = applicationContext.getBeansWithAnnotation(crClass);
        Set<Map.Entry<String, Object>> entries = annotationBeans.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String beanId = entry.getKey(); // 获取bean的名字
            // 获取bean的类对象
            Class<? extends Object> clazz = entry.getValue().getClass();
            System.out.println(beanId);
            System.out.println(clazz.getName());

            // 获取类上面的注解对象
            CacheResult cr = AnnotationUtils.findAnnotation(clazz, crClass);
            System.out.println(cr.key());

            // 循环当前类的方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                CacheResult methodCr = AnnotationUtils.findAnnotation(clazz, crClass);
                if (methodCr != null) {
                    System.out.println(methodCr.key());
                }
            }
        }
    }


}
