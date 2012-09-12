package com.plugtree.bi.publisher.api;


public class BestChoiceEventSender extends BaseEventSender {

	private final BaseEventSender internet;
	private final BaseEventSender bluetooth;
	private final boolean bluetoothPreference;
	
	public BestChoiceEventSender(BaseEventSender internet, BaseEventSender bluetooth, boolean bluetoothPreference) {
		super();
		this.internet = internet;
		this.bluetooth = bluetooth;
		this.bluetoothPreference = bluetoothPreference;
	}

	@Override
	public boolean popEvent(EventMessage msg) {
		boolean retval = false;
		if (bluetoothPreference && bluetooth != null) {
			retval = bluetooth.popEvent(msg);
		}
		if (!retval) {
			retval = internet.popEvent(msg);
		}
		return retval;
	}

}
