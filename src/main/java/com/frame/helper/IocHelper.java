/**
 * Copyright (C), 2015-2018，XXX人名
 * FileName: IocHelper
 * Author:   Administrator
 * Date:     2018/4/3 13:18
 * Description: 依赖注入实现
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.frame.helper;


import com.frame.annotation.Autowired;
import com.frame.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈依赖注入实现〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
public class IocHelper {

    private static final Map<Class<?>, Object> BEAN_MAP;

    /**
     * 动态加载
     */
    static {
        ProxyHelper proxyHelper = new ProxyHelper();
        BEAN_MAP = proxyHelper.getProxyInstance();
        for (Map.Entry<Class<?>, Object> entry : BEAN_MAP.entrySet()) {
            //获取class和实例
            Class<?> beanClass = entry.getKey();
            Object beanInstance = entry.getValue();
            //获取所有属性
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                //判断当前属性是否有Autowired注解
                if (field.isAnnotationPresent(Autowired.class)) {
                    Class<?> fieldClass = field.getType();
                    Object fieldInstance = BEAN_MAP.get(fieldClass);
                    if (fieldInstance != null) {
                        ReflectUtil.setField(beanInstance, field, fieldInstance);
                    }
                }
            }
        }
    }

    /**
     * 获取完成动态加载后的Map
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }
}
