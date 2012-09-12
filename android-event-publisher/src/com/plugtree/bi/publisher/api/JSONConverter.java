package com.plugtree.bi.publisher.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONConverter {

	public static String toJson(Map<String, Object> data) {
		JSONObject jsonObj = new JSONObject();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			try {
				jsonObj.put(entry.getKey(), entry.getValue());
			} catch (JSONException e) {
				Log.w(JSONConverter.class.getName(), "Couldn't write property " + entry.getKey(), e);
			}
		}
		String json = jsonObj.toString();
		return json;
	}

	public static Map<String, float[]> toValuesFromJson(String json) {
		Map<String, float[]> retval = new HashMap<String, float[]>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			for (Iterator<?> it = jsonObj.keys(); it.hasNext(); ) {
				String key = (String) it.next();
				JSONArray jvalues = jsonObj.getJSONArray(key);
				float[] values = new float[jvalues.length()];
				for (int index = 0; index < jvalues.length(); index++) {
					values[index] = Double.valueOf(jvalues.getDouble(index)).floatValue();
				}
				retval.put(key, values);
			}
		} catch (JSONException e) {
			//TODO
		}
		return retval;
	}
	
	public static String toJsonFromValues(Map<String, float[]> data) {
		JSONObject jsonObj = new JSONObject();
		for (Map.Entry<String, float[]> entry : data.entrySet()) {
			try {
				JSONArray array = new JSONArray();
				float[] value = entry.getValue();
				if (value != null) {
					for (float val : value) {
						array.put(val);
					}
				}
				jsonObj.put(entry.getKey(), array);
			} catch (JSONException e) {
				Log.w(JSONConverter.class.getName(), "Couldn't write property " + entry.getKey(), e);
			}
		}
		String json = jsonObj.toString();
		return json;
	}

}
