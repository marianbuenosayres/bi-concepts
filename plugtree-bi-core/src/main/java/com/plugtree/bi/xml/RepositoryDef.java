package com.plugtree.bi.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class RepositoryDef extends EventNodeDef {

	private Collection<ConnectorDef> from;
	private Collection<ConnectorDef> to;

	private String type; //SQL, NoSQL, File...
	private Properties connectionProperties = new Properties();

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

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public Properties getConnectionProperties() {
		return connectionProperties;
	}
	
	public void setConnectionProperties(Properties connectionProperties) {
		this.connectionProperties = connectionProperties;
	}
	
	@Override
	public void addFrom(ConnectorDef def) {
		if (from == null) {
			from = new ArrayList<ConnectorDef>();
		}
		from.add(def);
	}
}
