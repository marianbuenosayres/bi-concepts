package com.plugtree.bi.publisher.api;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class TCPEventSender extends BaseEventSender {

	private final String address;
	private final int port;
	
	public TCPEventSender(String address, int port) {
		super();
		this.address = address;
		this.port = port;
	}
	
	@Override
	public boolean popEvent(EventMessage msg) {
		Socket socket = null;
		try {
			socket = new Socket(this.address, this.port);
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.writeInt(msg.getUserId() == null ? 0 : msg.getUserId().length());
			if (msg.getUserId() != null) {
				output.write(msg.getUserId().getBytes());
			}
			Map<String, float[]> data = JSONConverter.toValuesFromJson(msg.getJson());
			for (Map.Entry<String, float[]> entry : data.entrySet()) {
				String key = entry.getKey();
				output.writeInt(key == null ? 0 : key.length());
				if (key != null) {
					output.write(key.getBytes());
				}
				float[] values = entry.getValue();
				output.writeInt(values == null ? 0 : values.length);
				for (int index = 0; values != null && index < values.length; index ++) {
					output.writeFloat(values[index]);
				}
			}
			return true;
		} catch (IOException e) {
			//TODO log error
			return false;
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) { }
			}
		}
	}
		

}
