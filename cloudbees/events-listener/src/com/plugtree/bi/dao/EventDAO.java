package com.plugtree.bi. dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.plugtree.bi.api.Event;
import com.plugtree.bi.api.Row;

public class EventDAO implements Runnable {

	private static Thread thread = null;
	private static final Object obj = new Object();
	
	@Override
	public void run() {
		while (true) {
			int cant = count();
			if (cant > 50000) {
				deleteOldest(50000);
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {	}
		}
	}
	
	public int count() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select count(*) from eventsdb.EVENT_DATA");
			rs.next();
			return rs.getInt(1);
		} catch (NamingException e) {
			log("Problem opening connection", e);
		} catch (SQLException e) {
			log("Problem counting tuples", e);
		} finally {
			close(conn, stmt, rs);
		}
		return -1;
	}
	
	public void deleteOldest(int maxAmount) {
		Connection conn = null;
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select ID from eventsdb.EVENT_DATA limit " + maxAmount + ", 1");
			rs.next();
			int id = rs.getInt(1);
			stmt2 = conn.createStatement();
			stmt2.execute("delete from eventsdb.EVENT_DATA where ID < " + id);
		} catch (NamingException e) {
			log("Problem opening connection", e);
		} catch (SQLException e) {
			log("Problem counting tuples", e);
		} finally {
			close(null, stmt, rs);
			close(conn, stmt2, null);
		}
	}
	
	public EventDAO() {
		synchronized(obj) {
			if (thread == null) {
				thread = Executors.defaultThreadFactory().newThread(this);
				thread.start();
			}
		}
	}
	
	private void log(String msg, Exception err) {
		System.out.println(msg);
		err.printStackTrace(System.out);
	}
	
	private void close(Connection conn, Statement stmt, ResultSet rs) {
		SQLException err = null;
		if (rs != null) {
			try { rs.close();
			} catch (SQLException e) { err = e; }
		}
		if (stmt != null) {
			try { stmt.close();
			} catch (SQLException e) { err = e; }
		}
		if (conn != null) {
			try { conn.close();
			} catch (SQLException e) { err = e; }
		}
		if (err != null) {
			log("Problem closing connnections", err);
		}
	}
	
	public void init() {
		String sqlCreate = "create table if not exists eventsdb.EVENT_DATA (" +
			"ID bigint not null auto_increment, KEYSTRING varchar(40) not null, " +
			"VALUE_1 float not null, VALUE_2 float, VALUE_3 float, " +
			"VALUE_4 float, VALUE_5 float, VALUE_6 float, PRIMARY KEY (ID)" +
		")";
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = openConnection();
			stmt = conn.createStatement();
			stmt.execute(sqlCreate);
		} catch (NamingException e) {
			log("Problem resolving connection", e);
		} catch (SQLException e) {
			log("Problem creating tables", e);
		} finally {
			close(conn, stmt, null);
		}
	}

	public void store(Event event) {
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
						"insert into eventsdb.EVENT_DATA (ID, KEYSTRING, VALUE_1, VALUE_2, " +
						"VALUE_3, VALUE_4, VALUE_5, VALUE_6) values ('0', ?, ?, ?, ?, ?, ?, ?)");
					stmt.setString(1, key);
					if (size > 0) stmt.setFloat(2, values[0]); else stmt.setNull(2, Types.FLOAT);
					if (size > 1) stmt.setFloat(3, values[1]); else stmt.setNull(3, Types.FLOAT);
					if (size > 2) stmt.setFloat(4, values[2]); else stmt.setNull(4, Types.FLOAT);
					if (size > 3) stmt.setFloat(5, values[3]); else stmt.setNull(5, Types.FLOAT);
					if (size > 4) stmt.setFloat(6, values[4]); else stmt.setNull(6, Types.FLOAT);
					if (size > 5) stmt.setFloat(7, values[5]); else stmt.setNull(7, Types.FLOAT);
					stmt.execute();
				} catch (SQLException e) {
					log("Problem storing event " + entry, e);
				} finally {
					close(null, stmt, null);
				}
			}
		} catch (NamingException e) {
			log("Problem resolving connection", e);
		} catch (SQLException e) {
			log("Problem storing events", e);
		} finally  {
			close(conn, null, null);
		}
	}
	
	public List<Row> list() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Row> retval = new ArrayList<Row>();
		try {
			conn = openConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select ID, " +
					"KEYSTRING, VALUE_1, VALUE_2, VALUE_3, " +
					"VALUE_4, VALUE_5, VALUE_6 " +
					"from eventsdb.EVENT_DATA order by ID desc");
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
			log("Problem resolving connection", e);
		} catch (SQLException e) {
			log("Problem querying events", e);
		} finally {
			close(conn, stmt, rs);
		}
		return retval;
	}
	
	public Row popOldest() {
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet rs = null;
		Row row = null;
		try {
			conn = openConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select ID, KEYSTRING, VALUE_1, VALUE_2, VALUE_3, " +
					"VALUE_4, VALUE_5, VALUE_6 from eventsdb.EVENT_DATA " +
					"where ID = (select min(ID) from eventsdb.EVENT_DATA)");
			if (rs.next()) {
				row = new Row();
				row.setId(rs.getLong(1));
				row.setKey(rs.getString(2));
				row.setValue1(rs.getFloat(3));
				row.setValue2(rs.getFloat(4));
				row.setValue3(rs.getFloat(5));
				row.setValue4(rs.getFloat(6));
				row.setValue5(rs.getFloat(7));
				row.setValue6(rs.getFloat(8));
				stmt2 = conn.createStatement();
				stmt2.execute("delete from eventsdb.EVENT_DATA where ID = " + row.getId());
			}
		} catch (NamingException e) {
			log("Problem resolivng connection", e);
		} catch (SQLException e) {
			log("Problem poping oldest event", e);
		} finally {
			close(null, stmt, rs);
			close(conn, stmt2, null);
		}
		return row;
	}

	private Connection openConnection() throws NamingException, SQLException {
		Connection conn;
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/eventsdb");
		conn = ds.getConnection();
		return conn;
	}
}
