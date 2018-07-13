package com.frame.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈寻找包下文件工具〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
public class ClassUtil {

    private static final Logger log = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     */
    public static Class<?> loadClass(String className, boolean isInit) {
        Class<?> myClass;
        try {
            myClass = Class.forName(className, isInit, getClassLoader());
        } catch (ClassNotFoundException e) {
            log.error("load class failure",e);
            throw new RuntimeException(e);
        }
        return myClass;
    }

    /**
     * 获取指定包下的所有类
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        //包名不能为空
        if (packageName == null) {
            throw new RuntimeException("package is null");
        }

        Set<Class<?>> classSet = new HashSet<Class<?>>();
        //包名对应的路径名称
        String packagePath = packageName.replace('.', '/');
        try {
            //获取可以查找到文件的资源定位符的迭代器
            Enumeration<URL> urls = getClassLoader().getResources(packagePath);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    //获取文件的类型
                    String protocal = url.getProtocol();
                    if ("file".equals(protocal)) {
                        System.out.println("file类型的扫描");
                        String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                        findClassByFile(packageName, filePath, classSet);
                    } else if ("jar".equals(protocal)) {
                        System.out.println("jar类型的扫描");
                    }
                }
            }
        } catch (IOException e) {
            log.error("get Class file failure!",e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void findClassByFile(String packageName, String filePath, Set<Class<?>> classSet) {
        File[] files = new File(filePath).listFiles(new FileFilter() {


            @Override
            public boolean accept(File file) {
                // 接受dir目录
                boolean acceptDir = file.isDirectory();
                // 接受class文件
                boolean acceptClass = file.getName().endsWith("class");
                return acceptDir || acceptClass;
            }
        });
        for (File file : files) {
            //文件名
            String fileName = file.getName();
            if (file.isDirectory()) {
                //子包名
                String subPackageName = packageName + "." + fileName;
                //子包对应的路径的名称
                String subFilePath = file.getAbsolutePath();
                findClassByFile(subPackageName, subFilePath, classSet);
            } else {
                //如果是文件，则加载该类，并存入集合中
                String className = packageName + "." + fileName.replace(".class", "").trim();
                Class<?> aClass = loadClass(className, false);
                classSet.add(aClass);
            }
        }

    }
}
