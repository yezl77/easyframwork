/**
 * Copyright (C), 2015-2018，XXX人名
 * FileName: ParamUtil
 * Author:   Administrator
 * Date:     2018/4/3 12:36
 * Description: 方法参数注入
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.frame.util;

import com.frame.annotation.RequestParam;
import com.frame.bean.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 〈一句话功能简述〉<br>
 * 〈方法参数注入〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
public class ParamUtil {

    private static final Logger log = LoggerFactory.getLogger(ParamUtil.class);

    //基本数据类型
    static Set<Class<?>> classSet = new HashSet<Class<?>>();

    static {
        classSet.add(Byte.class);
        classSet.add(Character.class);
        classSet.add(String.class);
        classSet.add(Short.class);
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
    }

    public static Object setParameter(Object object, Method method, HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        //获取所有参数
        Parameter[] paramTypes = method.getParameters();
        Class<?>[] types = method.getParameterTypes();

        int size = paramTypes.length;
        int num = 0;
        Object[] list = new Object[size];
        Param parameter = getParameter(request);
        try {
            for (int i = 0; i < size; i++) {
                /* 参数的类型 */
                Class<?> param = types[i];
                /* 为了获取注释的类 */
                Parameter paramType = paramTypes[i];
                if (param.isAssignableFrom(Param.class)) {
                    method.invoke(object, parameter);
                } else if (classSet.contains(param)) {
                    if (paramType.isAnnotationPresent(RequestParam.class)) {
                        RequestParam rParam = paramType.getAnnotation(RequestParam.class);
                        String pKey = rParam.value();
                        String value = parameter.getString(pKey);
                        Object obj = getInstance(param, value);
                        list[num] = obj;
                        num++;
                    }
                } else if(param.isAssignableFrom(HttpServletRequest.class)){
                    list[num] = request;
                    num++;
                } else if(param.isAssignableFrom(HttpServletResponse.class)){
                    list[num] = response;
                    num++;
                } else {
                    Object instance = param.newInstance();
                    setInstanceParam(instance, param, parameter.getParamMap());
                    list[num] = instance;
                    num++;
                }
            }
           result =  method.invoke(object, list);
        } catch (Exception e) {
            log.error("ParamUtil.setParameter:invoke method failure!",e);
            throw new RuntimeException(e);
        }
        return  result;
    }

    public static Param getParameter(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, String[]> params = request.getParameterMap();
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                paramMap.put(key, value);
            }
        }
        Param param = new Param(paramMap);
        return param;

    }

    private static Object getInstance(Class<?> cls, String value) {
        Object object = null;
        try {
            Constructor constructor = cls.getConstructor(String.class);
            object = constructor.newInstance(value);
        } catch (Exception e) {
            log.error("ParamUtil.getInstance:get constructor failure",e);
            throw new RuntimeException(e);
        }
        return object;

    }

    private static void setInstanceParam(Object obj, Class<?> cls, Map<String, String> data) {
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            //如果是set方法或is方法
            if (methodName.startsWith("set") || methodName.startsWith("is")) {
                //获取参数名（原本格式）
                String paramName1 = methodName.replace("set", "");
                paramName1.replace("is", "");
                //获取参数名(全部小写)
                String paramName2 = methodName.replace("set", "").toLowerCase();
                //获取该方法的参数
                Class<?>[] types = method.getParameterTypes();
                paramName2.replace("is", "").toLowerCase();
                if (data.containsKey(paramName1)) {
                    //如果参数列表中存在此参数(原本格式)
                    try {
                        Object param = getInstance(types[0], data.get(paramName1));
                        method.invoke(obj, param);
                    } catch (Exception e) {
                        System.err.println("ParamUtil.setInstanceParam:invoke method failure");
                    }
                } else if (data.containsKey(paramName2) && !paramName1.equals(paramName2)) {
                    //如果参数列表中存在此参数(全部小写)
                    try {
                        Object param = getInstance(types[0], data.get(paramName2));
                        method.invoke(obj, param);
                    } catch (Exception e) {
                        log.error("ParamUtil.setInstanceParam:invoke method failure",e);
                        throw new RuntimeException(e);
                    }
                }
            }

        }
    }
}
