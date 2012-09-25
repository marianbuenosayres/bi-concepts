package com.plugtree.bi.publisher.api;

import java.util.Iterator;
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

	public void sendEvent(String userId, String json) {
		Map<String, float[]> data = JSONConverter.toValuesFromJson(json);
		Iterator<String> it = data.keySet().iterator();
		String key = it.next();
		if (key.startsWith("gps")) {
			key = "gps";
		} else if (key.startsWith("sensor")) {
			if ("startTime".equals(key)) {
				key = it.next();
			}
			key = key.replace("sensor", "").toLowerCase();
		}
		events.add(new EventRow(userId, key, data));
		if (this.decorated != null) {
			this.decorated.sendEvent(userId, json);
		}
	}
	
	public List<EventRow> getEvents() {
		return this.events;
	}

	public void updateEvents(List<EventRow> oldEvents) {
		if (this.decorated != null) {
			for (EventRow oldEvent : oldEvents) {
				String json = JSONConverter.toJsonFromValues(oldEvent.getData());
				this.decorated.sendEvent(oldEvent.getUserId(), json);
			}
		}
		this.events.addAll(oldEvents);
	}
}
