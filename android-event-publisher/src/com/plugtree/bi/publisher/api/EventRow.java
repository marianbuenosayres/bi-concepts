package com.plugtree.bi.publisher.api;

import java.util.Map;

public class EventRow{

	private String userId;
	private String key;
	private Map<String, float[]> data;
	
	public EventRow() {
		super();
	}
	
	public EventRow(String userId, String key, Map<String, float[]> data) {
		this();
		this.userId = userId;
		this.key = key;
		this.data = data;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, float[]> getData() {
		return data;
	}

	public void setData(Map<String, float[]> data) {
		this.data = data;
	}
}
