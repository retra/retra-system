package cz.softinel.uaf.spring.web.controller;

import java.util.HashMap;
import java.util.Map;


public class Model {

	private Map<String, Object> model;
	
	public Model() {
		model = new HashMap<String, Object>();
	}
	
	public Object get(String key) {
		return model.get(key);
	}
	
	public void set(String key, Object value) {
		model.put(key, value);
	}

	public void put(String key, Object value) {
		set(key, value);
	}

	public void putAll(Map<String, Object> map) {
		// TODO: Is this realy good idea???
		model.putAll(map);
	}
	
	public Map getMap() {
		return model;
	}
	
}
