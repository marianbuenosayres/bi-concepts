package com.plugtree.bi.publisher.api;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class BaseEventSender implements EventSender, Runnable {

	private final LinkedBlockingQueue<EventMessage> eventGroup = new LinkedBlockingQueue<EventMessage>();
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
		while (true) {
			try {
				EventMessage msg = eventGroup.take();
				while(!popEvent(msg)) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {	}
				}
			} catch (InterruptedException e) {	}
		}
	}
	
	public void sendEvent(String userId, String json) {
		this.eventGroup.add(new EventMessage(userId, json));
	}
	
	public abstract boolean popEvent(EventMessage msg);
	
}
