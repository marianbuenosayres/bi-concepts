package com.plugtree.bi.publisher.api;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class InternetEventSenderTest extends TestCase {

	private Server server;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.server = new Server(8080);
		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SECURITY | ServletContextHandler.NO_SESSIONS);
		handler.setContextPath("/");
		
		HttpServlet mockServlet = EasyMock.createMock(HttpServlet.class);
		
		handler.addServlet(new ServletHolder(mockServlet), "/");
		
		server.start();

	}
	
	public void testSendEventOK() throws Exception {
		
		
		InternetEventSender sender = new InternetEventSender(url);
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
}
