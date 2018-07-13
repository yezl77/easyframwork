/**
 * Copyright (C), 2015-2018，XXX人名
 * FileName: ReflectUtil
 * Author:   Administrator
 * Date:     2018/4/3 12:12
 * Description: 反射工具
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.frame.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 〈一句话功能简述〉<br>
 * 〈反射工具〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
public class ReflectUtil {

    private static final Logger log = LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * 创建实例
     */
    public static Object getInstance(Class<?> myClass) {
        Object instance = null;
        try {
            instance = myClass.newInstance();
        } catch (Exception e) {
            log.error("new Instance faliure",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法
     */
    public static Object invoke(Object object, Method method, Object... args) {
        Object result = null;
        try {
            method.setAccessible(true);
            result = method.invoke(object, args);
        } catch (Exception e) {
            log.error("method invoke is failure",e);
            throw new RuntimeException(e);
        }
        return result;

    }

    /**
     * 设置成员变量
     */
    public static void setField(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            log.error("setField is failure",e);
            throw new RuntimeException(e);
        }
    }
}
