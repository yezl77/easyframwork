/**
 * Copyright (C), 2015-2018，XXX人名
 * FileName: AbstractProxy
 * Author:   Administrator
 * Date:     2018/4/3 12:32
 * Description: 代理类的抽象父类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.frame.bean;

/**
 * 〈一句话功能简述〉<br> 
 * 〈代理类的抽象父类〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
public abstract class AbstractProxy {
    //前置增强方法
    public void before() {}
    //后置增强方法
    public void after() {}
}
