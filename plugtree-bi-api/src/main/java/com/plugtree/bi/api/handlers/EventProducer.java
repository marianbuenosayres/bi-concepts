package com.plugtree.bi.api.handlers;

import com.plugtree.bi.api.Event;

public interface EventProducer {

	void sendEvent(Event event) throws SendEventException;
	
}
