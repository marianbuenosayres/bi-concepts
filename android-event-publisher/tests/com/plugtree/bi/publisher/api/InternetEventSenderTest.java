package com.plugtree.bi.publisher.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class InternetEventSenderTest extends TestCase {

	private Server server;
	private MockHttpServlet service;
	private int availablePort;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		int initPort = 8080;
		while(!isAvailablePort(initPort)) {
			initPort++;
		}
		this.availablePort = initPort;
		this.server = new Server(this.availablePort);
		ServletContextHandler handler = new ServletContextHandler(
				ServletContextHandler.NO_SECURITY | ServletContextHandler.NO_SESSIONS);
		handler.setContextPath("/events");
		this.service = new MockHttpServlet();
		handler.addServlet(new ServletHolder(this.service), "/");
		server.setHandler(handler);
		server.start();
	}
	
	private boolean isAvailablePort(int port) {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
			socket.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) { }
			}
		}
	}
	
	
	public void testSendEventOKGps() throws Exception {
		
		InternetEventSender sender = new InternetEventSender("http://localhost:" + this.availablePort + "/events/");
		Map<String, float[]> data = new HashMap<String, float[]>();
		data.put("gpsAltitude", new float[] { .1f, 5.1f });
		data.put("gpsLatitude", new float[] { 53323.1f });
		data.put("gpsLongitude", new float[] { 1231232.1212f });
		String json = JSONConverter.toJsonFromValues(data);
		sender.sendEvent("usr", json);
		
		Thread.sleep(1000);
		
		Assert.assertNotNull(service.getBody());
		Assert.assertEquals(json, service.getBody());
	}
	
	protected void tearDown() throws Exception {
		this.server.stop();
		this.service = null;
		this.server = null;
		super.tearDown();
	}
	
	private static class MockHttpServlet extends HttpServlet {
		
		private static final long serialVersionUID = -697411505839328147L;
		
		private String body = null;
		
		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
			try {
				InputStream input = req.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				StringBuilder builder = new StringBuilder();
				while (reader.ready()) {
					builder.append(reader.readLine());
				}
				body = builder.toString();
			} catch (Exception e) {
				body = e.getLocalizedMessage();
			}
		}
		
		public String getBody() {
			return body;
		}
	}
}
