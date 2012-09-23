package com.plugtree.bi.xml;

import java.util.ArrayList;
import java.util.Collection;

public class ChannelDef extends EventNodeDef {

	private Collection<ConnectorDef> origins = new ArrayList<ConnectorDef>();
	private Collection<ConnectorDef> destinations = new ArrayList<ConnectorDef>();

	public Collection<ConnectorDef> getOrigins() {
		return origins;
	}

	public void setOrigins(Collection<ConnectorDef> origins) {
		this.origins = origins;
	}

	public Collection<ConnectorDef> getDestinations() {
		return destinations;
	}

	public void setDestinations(Collection<ConnectorDef> destinations) {
		this.destinations = destinations;
	}

	@Override
	public void addTo(ConnectorDef def) {
		if (destinations == null) {
			destinations = new ArrayList<ConnectorDef>();
		}
		destinations.add(def);
	}

	@Override
	public void addFrom(ConnectorDef def) {
		if (origins == null) {
			origins = new ArrayList<ConnectorDef>();
		}
		origins.add(def);
	}
}
