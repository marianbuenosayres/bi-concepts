package com.plugtree.bi.xml;

import java.util.ArrayList;
import java.util.Collection;

public class ProcessorDef extends EventNodeDef {

	private Collection<ConnectorDef> from;
	private Collection<ConnectorDef> to;

	public Collection<ConnectorDef> getFrom() {
		return from;
	}

	public void setFrom(Collection<ConnectorDef> from) {
		this.from = from;
	}

	public Collection<ConnectorDef> getTo() {
		return to;
	}

	public void setTo(Collection<ConnectorDef> to) {
		this.to = to;
	}
	
	@Override
	public void addFrom(ConnectorDef def) {
		if (from == null) {
			from = new ArrayList<ConnectorDef>();
		}
		from.add(def);
	}
	
	@Override
	public void addTo(ConnectorDef def) {
		if (to == null) {
			to = new ArrayList<ConnectorDef>();
		}
		to.add(def);
	}
}
