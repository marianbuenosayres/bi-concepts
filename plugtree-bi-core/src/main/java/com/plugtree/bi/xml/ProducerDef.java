package com.plugtree.bi.xml;

import java.util.Collection;

public class ProducerDef extends EventNodeDef {

	private Collection<ConnectorDef> to;

	public Collection<ConnectorDef> getTo() {
		return to;
	}

	public void setTo(Collection<ConnectorDef> to) {
		this.to = to;
	}

}
