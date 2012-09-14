package com.plugtree.bi.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.plugtree.bi.cep.CEPEvent;
import com.plugtree.bi.cep.CEPService;

public class EventsServlet extends HttpServlet {

	private static final long serialVersionUID = 4216290291183412495L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String userId = req.getParameter("userId");
    	String content = readInput(req);
        String type = req.getHeader("Content-Type");
        try {
        	Map<String, float[]> data = parse(content, type);
        	CEPEvent event = asEvent(userId, data);
        	CEPService.getInstance().insert(event);
        } catch (Exception e) {
        	resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

	private String readInput(HttpServletRequest req) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
    	StringBuilder builder = new StringBuilder();
    	while (reader.ready()) {
    		builder.append(reader.readLine());
    	}
    	String retval = builder.toString();
		return retval;
	}
	
	private Map<String, float[]> parse(String content, String type) throws Exception {
		if ("application/json".equals(type)) {
			Map<String, float[]> retval = new HashMap<String, float[]>();
			JsonParser parser = new JsonParser();
			JsonElement json = parser.parse(content);
			if (json.isJsonObject()) {
				JsonObject obj = json.getAsJsonObject();
				for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
					String key = entry.getKey();
					float[] values = null;
					if (entry.getValue().isJsonArray()) {
						JsonArray array = entry.getValue().getAsJsonArray();
						values = new float[array.size()];
						for (int index = 0; index < array.size(); index++) {
							JsonElement elem = array.get(index);
							values[index] = elem.getAsJsonPrimitive().getAsFloat();
						}
					} else {
						JsonPrimitive prim = entry.getValue().getAsJsonPrimitive();
						values = new float[] { prim.getAsFloat() };
					}
					retval.put(key, values);
				}
			}
			return retval;
		} else {
			throw new Exception("Unkown type " + type);
		}
	}
	
	protected CEPEvent asEvent(String userId, Map<String, float[]> data) {
		CEPEvent event = new CEPEvent();
		event.put("userId", userId);
		boolean timeSet = false;
		if (data != null) {
			for (Map.Entry<String, float[]> entry : data.entrySet()) {
				String key = entry.getKey();
				if (key.endsWith("Time")) {
					timeSet = true;
					event.setTime(Float.valueOf(entry.getValue()[0]).longValue());
				}
				if (entry.getValue() != null) {
					if (entry.getValue().length == 0) {
						event.put(key, null);
					} else if (entry.getValue().length == 1) {
						Float value = Float.valueOf(entry.getValue()[0]);
						event.put(key, value);
					} else {
						for (int index = 0; index < entry.getValue().length; index++) {
							Float value = Float.valueOf(entry.getValue()[index]);
							event.put(key + index, value);
						}
					}
				}
			}
		}
		if (!timeSet) {
			event.setTime(System.currentTimeMillis());			
		}
		return event;
	}
}
