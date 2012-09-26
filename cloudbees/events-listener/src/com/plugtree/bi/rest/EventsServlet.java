package com.plugtree.bi.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.plugtree.bi.api.Event;
import com.plugtree.bi.api.Row;
import com.plugtree.bi.dao.EventDAO;

public class EventsServlet extends HttpServlet {

	private static final long serialVersionUID = 4298606551005801508L;
	
	/*private String dbname = "eventsdb";
	private String password = "eventsusr";
	private String user = "eventspwd";
	private String server = "ec2-23-21-211-172.compute-1.amazonaws.com";
	private int port = 3306;*/
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		new EventDAO().init();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			String userId = req.getParameter("userId");
			System.out.println("USER ID = " + userId);
			String content = readInput(req);
			System.out.println("CONTENT = " + content);
			Map<String, float[]> data = parse(content, req.getHeader("Content-Type"));
			System.out.println("DATA = " + data);
			Event event = asEvent(userId, data);
			System.out.println("EVENT = " + event);
			new EventDAO().store(event);
			System.out.println("STORED");
		} catch (Exception e) {
			System.out.println("Problem storing event");
			e.printStackTrace(System.out);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Row> rows = new EventDAO().list();
		req.setAttribute("rows", rows);
		req.getRequestDispatcher("/list.jsp").forward(req, resp);
	}
	
	private String readInput(HttpServletRequest req) throws IOException {
		return IOUtils.toString(req.getInputStream());
	}
	
	private Map<String, float[]> parse(String content, String type) throws Exception {
		if (type != null && type.toLowerCase().startsWith("application/json")) {
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
	
	protected Event asEvent(String userId, Map<String, float[]> data) {
		Event event = new Event();
		event.setUserId(userId);
		event.getData().putAll(data);
		return event;
	}
}
