package com.plugtree.bi.publisher.api;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LogEventSender implements EventSender {

	private List<EventRow> events = new LinkedList<EventRow>();
	private final EventSender decorated;
	
	public LogEventSender(EventSender eventSender) {
		super();
		this.decorated = eventSender;
	}

	@Override
	public void sendEvent(String userId, String json) {
		Map<String, float[]> data = JSONConverter.toValuesFromJson(json);
		String key = data.keySet().iterator().next();
		events.add(new EventRow(userId, key, data));
		if (this.decorated != null) {
			this.decorated.sendEvent(userId, json);
		}
	}
	
	public List<EventRow> getEvents() {
		return this.events;
	}
}
