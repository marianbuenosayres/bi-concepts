package com.plugtree.bi.publisher.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothEventSender extends BaseEventSender {

	private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
	private final String serverName;
	private BluetoothDevice serverDevice = null;
	
	public BluetoothEventSender(String bluetoothServerName) {
		this.serverName = bluetoothServerName;
		startServerDeviceConn();
	}

	private void startServerDeviceConn() {
		Set<BluetoothDevice> devices = adapter.getBondedDevices();
		for (BluetoothDevice device : devices) {
			if (device.getName().equals(this.serverName)) {
				this.serverDevice = device;
				break;
			}
		}
	}
	
	@Override
	public boolean popEvent(EventMessage msg) {
		if (msg == null || msg.getJson() == null) {
			return true; //discard message
		}
		//if serverDevice exists, open connection and send data.
		if (this.serverDevice != null) {
			UUID uuid = UUID.nameUUIDFromBytes(msg.getUserId().getBytes());
			BluetoothSocket socket = null;
			try {
				socket = this.serverDevice.createRfcommSocketToServiceRecord(uuid);
				PrintWriter writer = null;
				try {
					socket.connect();
					writer = new PrintWriter(socket.getOutputStream());
					writer.println(msg.getJson());
					writer.flush();
				} finally {
					if (writer != null) writer.close();
				}
				return true;
			} catch (IOException e) {
				Log.e(BluetoothEventSender.class.getName(), "Couldn't send bluetooth event", e);
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) { }
				}
			}
		}
		return false;
	}
}
