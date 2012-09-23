package com.plugtree.bi.xml;


public class ConnectorDef extends NodeDef {

	private EventNodeDef from;
	private EventNodeDef to;
	private ScriptDef expression;
	private boolean fetch = false;
	private long delay;
	
	public EventNodeDef getFrom() {
		return from;
	}
	
	public void setFrom(EventNodeDef from) {
		this.from = from;
	}
	
	public EventNodeDef getTo() {
		return to;
	}
	
	public void setTo(EventNodeDef to) {
		this.to = to;
	}
	
	public void setExpression(ScriptDef expression) {
		this.expression = expression;
	}
	
	public ScriptDef getExpression() {
		return expression;
	}
	
	public void setFetch(boolean fetch) {
		this.fetch = fetch;
	}
	
	public boolean getFetch() {
		return fetch;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
}
