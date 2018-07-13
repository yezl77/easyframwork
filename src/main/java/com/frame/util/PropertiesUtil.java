package com.frame.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

	//放置配置项
	private enum WebProperties{
		FILE("frame.properties"),
		BASE_PACKAGE("frame.webapp.base_package"),
		JSP_PATH("frame.webapp.jsp_path");

		private String value;

		private WebProperties(String value){
			this.value = value;
		}

		public String value(){
			return value;
		}

	}

	private static Properties p;

	static{
		p = load(WebProperties.FILE.value());
	}

	//加载配置
	public static Properties load(String file){
		Properties p=new Properties();
		InputStream is=ClassUtil.getClassLoader().getResourceAsStream(file);
		try {
			p.load(is);
			is.close();
		} catch (IOException e) {
			log.error("PropertiesUtil:load properties failure",e);
			throw new RuntimeException(e);
		}
		return p;
	}

	//获取需要扫描的包的路径
	public static String getBasePackage() {
		String value = "";
		String key = WebProperties.BASE_PACKAGE.value();
		if (p.containsKey(key)) {
			value = p.getProperty(key);
		}
		return value;
	}

	//获取有JSP页面的包的路径
	public static String getJspPath() {
		String value = "";
		String key = WebProperties.JSP_PATH.value();
		if (p.containsKey(key)) {
			value = p.getProperty(key);
		}
		return value;
	}
}
