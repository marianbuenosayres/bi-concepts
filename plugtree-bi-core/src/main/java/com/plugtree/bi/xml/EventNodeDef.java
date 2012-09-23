package com.plugtree.bi.xml;

public abstract class EventNodeDef extends NodeDef {

	public void addTo(ConnectorDef def) {
		throw new UnsupportedOperationException("Operation only supported for producers, channels and processors");
	}
	
	public void addFrom(ConnectorDef def) {
		throw new UnsupportedOperationException("Operation only supported for consumers, channels and processors");
	}
}
