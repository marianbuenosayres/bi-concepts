package com.plugtree.bi.startup;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.plugtree.bi.rest.EventsServlet;

public class WebServerMain {

	private final Server server;
	
	protected WebServerMain() {
		this.server = new Server(8080);
		ServletContextHandler handler = new ServletContextHandler(
				ServletContextHandler.NO_SECURITY | ServletContextHandler.NO_SESSIONS);
		handler.setContextPath("/events");
		EventsServlet servlet = new EventsServlet();
		ServletHolder holder = new ServletHolder(servlet);
		handler.addServlet(holder, "/");
		server.setHandler(handler);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new WebServerMain();
	}
	
	@Override
	protected void finalize() throws Throwable {
		server.stop();
		super.finalize();
	}
}
