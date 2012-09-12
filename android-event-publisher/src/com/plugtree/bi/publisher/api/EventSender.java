package com.plugtree.bi.publisher.api;

public interface EventSender {

	void sendEvent(String userId, String json);

}
