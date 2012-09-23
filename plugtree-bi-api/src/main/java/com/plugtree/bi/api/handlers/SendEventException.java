package com.plugtree.bi.api.handlers;

import com.plugtree.bi.api.Event;

public class SendEventException extends RuntimeException {

	private static final long serialVersionUID = -8502307500734589532L;

	private final Event event;
	
	public SendEventException(String message, Throwable cause) {
		super(message, cause);
		this.event = null;
	}

	public SendEventException(String message) {
		super(message);
		this.event = null;
	}

	public SendEventException(Throwable cause) {
		super(cause);
		this.event = null;
	}

	public SendEventException(Event event, String message, Throwable cause) {
		super(message, cause);
		this.event = event;
	}

	public SendEventException(Event event, String message) {
		super(message);
		this.event = event;
	}

	public SendEventException(Event event, Throwable cause) {
		super(cause);
		this.event = event;
	}
	
	public Event getEvent() {
		return event;
	}
}
