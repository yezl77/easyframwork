/**
 * Copyright (C), 2015-2018，XXX人名
 * FileName: RelationUtil
 * Author:   Administrator
 * Date:     2018/4/3 12:14
 * Description: 映射关系工具
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.frame.util;

import com.frame.annotation.Controller;
import com.frame.annotation.RequestMapping;
import com.frame.helper.ClassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈映射关系工具〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
public class RelationUtil {

    private static final Logger log = LoggerFactory.getLogger(RelationUtil.class);

    /**
     * 建立Bean类与实例的映射关系
     */
    public static Map getBeanMap() {
        Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();
        Set<Class<?>> beanSet = ClassHelper.getBeanClass();
        for (Class<?> myClass : beanSet) {
            Object object = ReflectUtil.getInstance(myClass);
            beanMap.put(myClass, object);
        }
        return beanMap;
    }

    /**
     * 建立方法与注释中value的映射关系
     */
    public static Map getMethodMap() {
        Map<String, Method> methodMap = new HashMap<String, Method>();
        Set<Class<?>> ControllerSet = ClassHelper.getControllerClass();
        for (Class<?> myClass : ControllerSet) {
            //获取Controller注释
            Controller controller = (Controller) myClass.getAnnotation(Controller.class);
            //获取注释上的value值
            String cValue = controller.value();
            //获取所有的方法
            Method[] methods = myClass.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping rm = (RequestMapping) method.getAnnotation(RequestMapping.class);
                    String rValue = rm.value();
                    methodMap.put("/" + cValue + "/" + rValue, method);
                } else {
                    continue;
                }
            }
        }
        return methodMap;
    }
}
