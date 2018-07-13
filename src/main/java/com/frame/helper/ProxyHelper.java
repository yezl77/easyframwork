/**
 * Copyright (C), 2015-2018，XXX人名
 * FileName: ProxyHelper
 * Author:   Administrator
 * Date:     2018/4/3 13:19
 * Description: 动态代理实现
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.frame.helper;


import com.frame.annotation.ProxyAOP;
import com.frame.util.ProxyUtil;
import com.frame.util.RelationUtil;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈动态代理实现〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
public class ProxyHelper {

    private final static Map<Class<?>, Object> BEAN_MAP;
    private final static Set<Class<?>> PROXY_BEAN;
    private Map<Class<?>, List<Class<?>>> proxyMap;

    static {
        PROXY_BEAN = ClassHelper.getProxyClass();
        BEAN_MAP = RelationUtil.getBeanMap();
    }


    public Map<Class<?>, Object> getProxyInstance() {

        proxyMap = getProxyMap();
        for (Map.Entry<Class<?>, List<Class<?>>> entry : proxyMap.entrySet()) {
            Class<?> target = entry.getKey();
            List<Class<?>> proxyList = entry.getValue();
            Object result = getProxy(target, proxyList);
            BEAN_MAP.put(target, result);
        }
        return BEAN_MAP;
    }

    private Object getProxy(Class<?> target, List<Class<?>> list) {
        ProxyUtil handler = new ProxyUtil(list);
        return handler.getProxy(target);
    }

    private Map getProxyMap() {
        Map<Class<?>, List<Class<?>>> map = new HashMap<Class<?>, List<Class<?>>>();
        for (Class<?> cls : PROXY_BEAN) {
            ProxyAOP proxy = cls.getAnnotation(ProxyAOP.class);
            /* 要代理的类 */
            Class<?> value = proxy.value();
            if (map.containsKey(value)) {
                map.get(value).add(cls);
            } else {
                List<Class<?>> proxyList = new ArrayList<Class<?>>();
                proxyList.add(cls);
                map.put(value, proxyList);
            }
        }
        return map;
    }
}
