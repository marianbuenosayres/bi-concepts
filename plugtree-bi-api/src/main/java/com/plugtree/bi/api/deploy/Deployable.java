package com.plugtree.bi.api.deploy;

import java.util.List;

import com.plugtree.bi.api.handlers.EventConsumer;
import com.plugtree.bi.api.handlers.EventProducer;

/**
 * Anything that can be deployed on an environment.
 * 
 * @author marianbuenosayres
 *
 */
public interface Deployable {

	enum Type { JAR, SAR, WAR, APK }; 
	
	/**
	 * @return a list of EventConsumers (could be null or empty) 
	 */
	List<EventConsumer> getConsumers();

	/**
	 * @return a list of EventProducers (could be null or empty) 
	 */
	List<EventProducer> getProducers();
	
	/**
	 * @return the file type of the deployable (jar, war, sar, apk...)
	 */
	Type getType();
	
	/**
	 * @return the binary content of the file of the deployable
	 */
	byte[] getContent();
	
	/**
	 * Used to start the deployable component
	 */
	void start() throws StartException;
}
