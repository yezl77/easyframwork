/**
 * Copyright (C), 2015-2018，XXX人名
 * FileName: Param
 * Author:   Administrator
 * Date:     2018/4/3 12:32
 * Description: 参数列表
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.frame.bean;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈参数列表〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
public class Param {
    Map<String, String> paramMap;

    public Param(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public String getString(String name) {
        return paramMap.get(name);
    }

    public int getInt(String name) {
        String valueS = paramMap.get(name);
        int value = Integer.valueOf(valueS).intValue();
        return value;
    }

    public double getDouble(String name) {
        String valueS = paramMap.get(name);
        double value = Double.valueOf(valueS).doubleValue();
        return value;
    }
}
