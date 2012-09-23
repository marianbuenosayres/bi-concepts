package com.plugtree.bi.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.plugtree.bi.api.Event;
import com.plugtree.bi.api.Row;

public class EventsServlet extends HttpServlet {

	private static final long serialVersionUID = 4298606551005801508L;
	
	/*private String dbname = "eventsdb";
	private String password = "eventsusr";
	private String user = "eventspwd";
	private String server = "ec2-23-21-211-172.compute-1.amazonaws.com";
	private int port = 3306;*/
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		initSQL();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			String userId = req.getParameter("userId");
			String content = readInput(req);
			Map<String, float[]> data = parse(content, req.getHeader("Content-Type"));
			Event event = asEvent(userId, data);
			store(event);
		} catch (Exception e) {
			//print error
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Row> rows = readTable();//TODO??
		req.setAttribute("rows", rows);
		req.getRequestDispatcher("/list.jsp").forward(req, resp);
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
	
	private void initSQL() {
		String sqlCreate = "create table if not exists `eventsdb`.`EVENT_DATA` (" +
			"`ID` bigint not null auto_increment, `KEY` varchar(40) not null, " +
			"`VALUE_1` float not null, `VALUE_2` float, `VALUE_3` float, " +
			"`VALUE_4` float, `VALUE_5` float, `VALUE_6` float, PRIMARY KEY (`ID')" +
		")";
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = openConnection();
			stmt = conn.createStatement();
			stmt.execute(sqlCreate);
		} catch (NamingException e) {
			//TODO??
		} catch (SQLException e) {
			//TODO??
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					//TODO??
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//TODO??
				}
			}
		}
	}
	
	private void store(Event event) {
		Connection conn = null;
		try {
			conn = openConnection();
			for (Map.Entry<String, float[]> entry : event.getData().entrySet()) {
				String key = entry.getKey();
				float[] values = entry.getValue();
				int size = values == null ? 0 : values.length;
				PreparedStatement stmt = null;
				try {
					stmt = conn.prepareStatement(
						"insert into `eventsdb`.`EVENT_DATA` (`ID`, `KEY`, `VALUE_1`, `VALUE_2`, " +
						"`VALUE_3`, `VALUE_4`, `VALUE_5`, `VALUE_6`) values ('0', ?, ?, ?, ?, ?, ?, ?)");
					stmt.setString(1, key);
					if (size > 0) stmt.setFloat(2, values[0]); else stmt.setNull(2, Types.FLOAT);
					if (size > 1) stmt.setFloat(3, values[1]); else stmt.setNull(3, Types.FLOAT);
					if (size > 2) stmt.setFloat(4, values[2]); else stmt.setNull(4, Types.FLOAT);
					if (size > 3) stmt.setFloat(5, values[3]); else stmt.setNull(5, Types.FLOAT);
					if (size > 4) stmt.setFloat(6, values[4]); else stmt.setNull(6, Types.FLOAT);
					if (size > 5) stmt.setFloat(7, values[5]); else stmt.setNull(7, Types.FLOAT);
					stmt.execute();
				} catch (SQLException e) {
					//TODO??
				} finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (SQLException e) {
							//TODO??
						}
					}
				}
			}
		} catch (NamingException e) {
			//TODO??
		} catch (SQLException e) {
			//TODO??
		} finally  {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//TODO??
				}
			}
		}
	}
	
	private List<Row> readTable() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Row> retval = new ArrayList<Row>();
		try {
			conn = openConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select `ID`, " +
					"`KEY`, `VALUE_1`, `VALUE_2`, `VALUE_3`, " +
					"`VALUE_4`, `VALUE_5`, `VALUE_6` " +
					"from `eventsdb`.`EVENT_DATA` order by `ID`");
			while (rs.next()) {
				Row row = new Row();
				row.setId(rs.getLong(1));
				row.setKey(rs.getString(2));
				row.setValue1(rs.getFloat(3));
				row.setValue2(rs.getFloat(4));
				row.setValue3(rs.getFloat(5));
				row.setValue4(rs.getFloat(6));
				row.setValue5(rs.getFloat(7));
				row.setValue6(rs.getFloat(8));
				retval.add(row);
			}
		} catch (NamingException e) {
			//TODO??
		} catch (SQLException e) {
			//TODO??
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					//TODO??
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					//TODO??
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						//TODO??
					}
				}
			}
		}
		return retval;
	}

	protected Event asEvent(String userId, Map<String, float[]> data) {
		Event event = new Event();
		event.setUserId(userId);
		event.getData().putAll(data);
		return event;
	}
	
	private Connection openConnection() throws NamingException, SQLException {
		Connection conn;
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/eventsdb");
		conn = ds.getConnection();
		return conn;
	}
}
