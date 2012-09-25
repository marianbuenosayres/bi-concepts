package com.plugtree.bi.publisher.api;

public class EventPublisherConfig {

	private static final EventPublisherConfig INSTANCE = new EventPublisherConfig();
	
	private EventPublisherConfig() {
		super();
	}
	
	public static EventPublisherConfig instance() {
		return INSTANCE;
	}
	
	private LogEventSender eventSender = new LogEventSender(null);
	private String userId;
	
	private String scheme;
	private String tcpHost;
	private String tcpPort;
	private String bluetoothServer;
	private String httpUrl;

	public String getUserId() {
		return userId;
	}

	public EventSender getEventSender() {
		return eventSender;
	}

	public String getScheme() {
		return scheme;
	}
	
	public String getHttpUrl() {
		return httpUrl;
	}
	
	public String getBluetoothServer() {
		return bluetoothServer;
	}
	
	public String getTcpHost() {
		return tcpHost;
	}
	
	public String getTcpPort() {
		return tcpPort;
	}
	
	public synchronized void setBluetoothServer(String bluetoothServer) {
		this.bluetoothServer = bluetoothServer;
	}
	
	public synchronized void setEventSender(EventSender eventSender) {
		LogEventSender oldSender = this.eventSender;
		this.eventSender = new LogEventSender(eventSender);
		this.eventSender.updateEvents(oldSender.getEvents());
	}
	
	public synchronized void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	
	public synchronized void setScheme(String scheme) {
		this.scheme = scheme;
	}
	
	public synchronized void setTcpHost(String tcpHost) {
		this.tcpHost = tcpHost;
	}
	
	public synchronized void setTcpPort(String tcpPort) {
		this.tcpPort = tcpPort;
	}
	
	public synchronized void setUserId(String userId) {
		this.userId = userId;
	}

	public LogEventSender getLogEventSender() {
		return this.eventSender;
	}
}
