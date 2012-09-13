package com.plugtree.bi.publisher.api;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class EventMessage implements Externalizable {

	private String userId;
	private String json;

	public EventMessage() {
		super();
	}
	
	public EventMessage(String userId, String json) {
		this();
		this.userId = userId;
		this.json = json;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void readExternal(ObjectInput input) throws IOException {
		boolean hasValue = input.readBoolean();
		this.userId = hasValue ? input.readUTF() : null;
		hasValue = input.readBoolean();
		this.json = hasValue ? input.readUTF() : null;
	}

	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeBoolean(this.userId != null);
		if (this.userId != null) {
			output.writeUTF(this.userId);
		}
		output.writeBoolean(this.json != null);
		if (this.json != null) {
			output.writeUTF(this.json);
		}
	}

}
