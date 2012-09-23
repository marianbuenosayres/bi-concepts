package com.plugtree.bi.api.deploy.impl;

import java.util.List;

import com.plugtree.bi.api.deploy.Deployable;
import com.plugtree.bi.api.deploy.StartException;
import com.plugtree.bi.api.handlers.EventConsumer;
import com.plugtree.bi.api.handlers.EventProducer;

public class JarOSGIDeployable implements Deployable {

	public List<EventConsumer> getConsumers() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<EventProducer> getProducers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Type getType() {
		return Type.JAR;
	}

	public byte[] getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public void start() throws StartException {
		// TODO Auto-generated method stub

	}

}
