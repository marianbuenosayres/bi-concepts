package com.plugtree.bi.api.handlers;

import com.plugtree.bi.api.Event;

public interface EventConsumer {

	Event receiveEvent() throws ConsumeEventException;
	
}
