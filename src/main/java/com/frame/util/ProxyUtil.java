/**
 * Copyright (C), 2015-2018，XXX人名
 * FileName: ProxyUtil
 * Author:   Administrator
 * Date:     2018/4/3 13:16
 * Description: 实现代理
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.frame.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;


/**
 * 〈一句话功能简述〉<br>
 * 〈实现代理〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
public class ProxyUtil implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ProxyUtil.class);

    private List<Class<?>> proxyList;
    private int num;

    public ProxyUtil(List<Class<?>> proxyList) {
        this.proxyList = proxyList;
        this.num = proxyList.size();
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<?> cls) {
        return (T) Enhancer.create(cls, this);
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        before();
        Object result = proxy.invokeSuper(target, args);
        after();
        return result;
    }

    private void before() {
        for (Class<?> cls : proxyList) {
            Method method = getMethod(cls, "before");
            Object proxyObject = getInstance(cls);
            try {
                method.invoke(proxyObject, null);
            } catch (Exception e) {
                log.error("ProxyHander:method invoke failure",e);
                throw new RuntimeException(e);
            }
        }
    }

    private void after() {
        for (Class<?> cls : proxyList) {
            Method method = getMethod(cls, "after");
            Object proxyObject = getInstance(cls);
            try {
                method.invoke(proxyObject, null);
            } catch (Exception e) {
                log.error("ProxyHander:method invoke failure",e);
                throw new RuntimeException(e);
            }
        }
    }

    private Method getMethod(Class<?> cls, String methodName) {
        //获取增强类中的特定方法
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (methodName.equals(name)) {
                return method;
            }
        }
        return null;
    }

    private Object getInstance(Class<?> cls) {
        //获取增强类的实例
        Object object = null;
        try {
            object = cls.newInstance();
        } catch (Exception e) {
            log.error("ProxyHandler.getInstance:failure",e);
            throw new RuntimeException(e);
        }
        return object;
    }

}
