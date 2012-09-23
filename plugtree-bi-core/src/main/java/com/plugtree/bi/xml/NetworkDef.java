package com.plugtree.bi.xml;

import java.util.ArrayList;
import java.util.Collection;

public class NetworkDef extends EventNodeDef {

	private Collection<NodeDef> nodes = new ArrayList<NodeDef>();
	private Collection<EventNodeDef> elements = new ArrayList<EventNodeDef>();
	private Collection<ConnectorDef> connectors = new ArrayList<ConnectorDef>();

	public Collection<NodeDef> getNodes() {
		return nodes;
	}

	public void setNodes(Collection<NodeDef> nodes) {
		this.nodes = nodes;
	}

	public void addNode(NodeDef node) {
		nodes.add(node);
		if (node instanceof EventNodeDef) {
			elements.add((EventNodeDef) node);
		} else if (node instanceof ConnectorDef) {
			connectors.add((ConnectorDef) node);
		}
	}
	
	public void removeNode(NodeDef node) {
		nodes.remove(node);
		if (node instanceof EventNodeDef) {
			elements.remove((EventNodeDef) node);
		} else if (node instanceof ConnectorDef) {
			connectors.remove((ConnectorDef) node);
		}
	}

	public Collection<ConnectorDef> getConnectors() {
		return connectors;
	}
	
	public Collection<EventNodeDef> getElements() {
		return elements;
	}

	public EventNodeDef findElementById(String id) {
		EventNodeDef retval = null;
		for (EventNodeDef def : elements) {
			if (def.getId().equals(id)) {
				retval = def;
				break;
			}
		}
		return retval;
	}

	@Override
	public void addTo(ConnectorDef def) {
		throw new UnsupportedOperationException("Operation supported only for producers, channels and processors");
	}

	@Override
	public void addFrom(ConnectorDef def) {
		throw new UnsupportedOperationException("Operation supported only for consumers, channels and processors");
	}
}
