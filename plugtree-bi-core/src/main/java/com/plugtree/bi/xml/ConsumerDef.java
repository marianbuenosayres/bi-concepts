package com.plugtree.bi.xml;

import java.util.ArrayList;
import java.util.Collection;

public class ConsumerDef extends EventNodeDef {

	private Collection<ConnectorDef> from;

	public Collection<ConnectorDef> getFrom() {
		return from;
	}

	public void setFrom(Collection<ConnectorDef> from) {
		this.from = from;
	}
	
	@Override
	public void addFrom(ConnectorDef def) {
		if (from == null) {
			from = new ArrayList<ConnectorDef>();
		}
		from.add(def);
	}
}
