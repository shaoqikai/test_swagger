package com.example.test_swagger.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 反射工具类
 * @author shaoqk
 * @create 2022-01-10 11:13
 */
@Slf4j
public class ClassFS {

    private ConfigurableApplicationContext applicationContext;


    /**
     * service.executiveBusiness(MethodTypeEnum.UPDATE_CASE.getMethodName(), acceptCaseVo, AcceptCaseService.class);
     *
     * @param stage 方法名称
     * @param data  数据源
     * @param clazz 方法所在类名称
     * @return 反射执行方法
     */
    public Object executiveBusiness(String stage, Map<String, Object> data, Class clazz) throws InvocationTargetException, IllegalAccessException {
        // 参数判断
        if (data.isEmpty()) {
            throw new ArithmeticException(data + "parameter cannot be empty");
        }
        Map beans;
        try {
            // 解析map类型
            beans = applicationContext.getBeansOfType(clazz);
        } catch (BeansException e) {
            log.info("{}-类无法解析类型", clazz.getName());
            throw e;
        }
        Object object = null;
        for (Object aClass : beans.values()) {
            if (!aClass.getClass().isInterface()) {
                String simpleName = aClass.getClass().getSimpleName();
                String firstLowerName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
                Method[] method = ClassUtils.getClassMethods(aClass.getClass());
                String beanName = (firstLowerName.contains("$")) ? firstLowerName.substring(0, firstLowerName.indexOf("$")) : firstLowerName;
                for (Method value : method) {
                    if (stage.equals(value.getName())) {
                        object = excObject(data, SpringContextUtil.getBean(beanName, aClass.getClass()), value, beanName);
                    }
                }
            }
        }
        return object;
    }

    private Object excObject(Map<String, Object> data, Object bean, Method method, String beanName) throws InvocationTargetException, IllegalAccessException {
        Object object = null;
        log.info("> [反射调用]执行方法 -> bean:{},method:{},arguments:{}", bean, method, data);
        if (bean == null || method == null || data == null) {
            log.error("> [反射调用]执行方法遇到错误，请确认被调用的bean(beanName={})在容器中是否存在？或者被调用的方法不对？或者是参数为空？", beanName);
        } else {
            object = method.invoke(bean, data);
        }
        return object;
    }
}
