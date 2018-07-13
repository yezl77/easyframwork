package com.frame.bean;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

	//路径
	private String path;

	//数据
	private Map<String, Object> model;

	public ModelAndView() {
		model = new HashMap<String,Object>();
	}

	public void add(String key,Object value) {
		model.put(key, value);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}
}
