package com.plugtree.bi.api;

import java.util.HashMap;
import java.util.Map;

public class Event {

	private final Map<String, float[]> data = new HashMap<String, float[]>();
	private String userId;

	public Map<String, float[]> getData() {
		return data;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
}

