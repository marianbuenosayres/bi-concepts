package com.plugtree.bi.api;

public class Row {

	private long id;
	private String key;
	private float value1 = Float.MIN_VALUE;
	private float value2 = Float.MIN_VALUE;
	private float value3 = Float.MIN_VALUE;
	private float value4 = Float.MIN_VALUE;
	private float value5 = Float.MIN_VALUE;
	private float value6 = Float.MIN_VALUE;

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public float getValue1() {
		return value1;
	}
	
	public void setValue1(float value1) {
		this.value1 = value1;
	}
	
	public float getValue2() {
		return value2;
	}
	
	public void setValue2(float value2) {
		this.value2 = value2;
	}
	
	public float getValue3() {
		return value3;
	}
	
	public void setValue3(float value3) {
		this.value3 = value3;
	}
	
	public float getValue4() {
		return value4;
	}
	
	public void setValue4(float value4) {
		this.value4 = value4;
	}
	
	public float getValue5() {
		return value5;
	}
	
	public void setValue5(float value5) {
		this.value5 = value5;
	}
	
	public float getValue6() {
		return value6;
	}
	
	public void setValue6(float value6) {
		this.value6 = value6;
	}
}
