package com.plugtree.bi.cep;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CEPEvent implements Externalizable {

	public static final String TIME_KEY = "timestamp";
	public static final String TYPE_KEY = "type";
	public static final String SENDER_KEY = "sender";
	public static final String TTL_KEY = "ttl";
	public static final String CAUSALITY_KEY = "causality";
	public static final String UUID_KEY = "uuid";
	public static final String DEBUGINFO_KEY = "debuginfo";
	public static final String SECURITYINFO_KEY = "securityinfo";
	
	private final Map<String, Serializable> payload = new HashMap<String, Serializable>();
	private final Map<String, Serializable> metadata = new HashMap<String, Serializable>();

	private final long defaultTime = System.currentTimeMillis();
	
	public long getTime() {
		return Long.valueOf(String.valueOf(metadata.get(TIME_KEY) == null ? defaultTime : metadata.get(TIME_KEY)));
	}

	public void setTime(long time) {
		metadata.put(TIME_KEY, String.valueOf(time));
	}
	
	public long getTtl() {
		return Long.valueOf(String.valueOf(metadata.get(TTL_KEY)));
	}

	public Map<String, Serializable> getPayload() {
		return payload;
	}

	public Map<String, Serializable> getMetadata() {
		return metadata;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		payload.clear();
		int sizePayload = in.readInt();
		for (int index = 0; index < sizePayload; index++) {
			String key = in.readUTF();
			Serializable value = (Serializable) in.readObject();
			payload.put(key, value);
		}
		metadata.clear();
		int sizeMetadata = in.readInt();
		for (int index = 0; index < sizeMetadata; index++) {
			String key = in.readUTF();
			Serializable value = (Serializable) in.readObject();
			metadata.put(key, value);
		}
	}
	
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(payload.size());
		for (Map.Entry<String, Serializable> entry : payload.entrySet()) {
			out.writeUTF(entry.getKey());
			out.writeObject(entry.getValue());
		}
		out.writeInt(metadata.size());
		for (Map.Entry<String, Serializable> entry : metadata.entrySet()) {
			out.writeUTF(entry.getKey());
			out.writeObject(entry.getValue());
		}
	}
}
