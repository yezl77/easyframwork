/**
 * Copyright (C), 2015-2018，XXX人名
 * FileName: ClassHelper
 * Author:   Administrator
 * Date:     2018/4/3 13:17
 * Description: 创建包下所有类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.frame.helper;



import com.frame.annotation.Controller;
import com.frame.annotation.ProxyAOP;
import com.frame.annotation.Service;
import com.frame.util.ClassUtil;
import com.frame.util.PropertiesUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈创建包下所有类〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
public class ClassHelper {

    private static final Set<Class<?>> CLASS_SET;

    static {
        String packageName = PropertiesUtil.getBasePackage();
        CLASS_SET = ClassUtil.getClassSet(packageName);
    }

    // 获取所有的类
    public static Set<Class<?>> getAllClass() {
        return CLASS_SET;
    }

    // 获取所有注释Service的类
    public static Set<Class<?>> getServiceClass() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> myClass : CLASS_SET) {
            if (myClass.isAnnotationPresent(Service.class)) {
                classSet.add(myClass);
            }
        }
        return classSet;
    }

    // 获取所有注释Controller的类
    public static Set<Class<?>> getControllerClass() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> myClass : CLASS_SET) {
            if (myClass.isAnnotationPresent(Controller.class)) {
                classSet.add(myClass);
            }
        }
        return classSet;
    }

    // 获取所有注释ProxyAOP的类
    public static Set<Class<?>> getProxyClass() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> myClass : CLASS_SET) {
            if (myClass.isAnnotationPresent(ProxyAOP.class)) {
                classSet.add(myClass);
            }
        }
        return classSet;
    }

    // 获取所有的Bean类
    public static Set<Class<?>> getBeanClass() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        classSet.addAll(getControllerClass());
        classSet.addAll(getServiceClass());
        classSet.addAll(getProxyClass());
        return classSet;
    }
}
