package com.plugtree.bi.publisher.api;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;

public abstract class BaseEventSender implements EventSender, Runnable {

	private final Queue<EventMessage> eventGroup = new LinkedList<EventMessage>();
	private final Thread service;
	
	public BaseEventSender() {
		super();
		this.service = Executors.defaultThreadFactory().newThread(this);
		service.start();
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (this.service != null && this.service.isAlive()) {
			this.service.interrupt();
			this.eventGroup.clear();
		}
		super.finalize();
	}

	public void run() {
		EventMessage msg = eventGroup.poll();
		while(!popEvent(msg)) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {	}
		}
	}
	
	public void sendEvent(String userId, String json) {
		this.eventGroup.add(new EventMessage(userId, json));
	}
	
	public abstract boolean popEvent(EventMessage msg);
	
}
