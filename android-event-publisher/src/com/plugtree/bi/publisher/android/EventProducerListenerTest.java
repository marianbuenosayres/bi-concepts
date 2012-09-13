package com.plugtree.bi.publisher.android;

import java.util.List;

import junit.framework.Assert;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.test.AndroidTestCase;

import com.plugtree.bi.publisher.api.EventPublisherConfig;
import com.plugtree.bi.publisher.api.EventRow;
import com.plugtree.bi.publisher.api.EventSender;

public class EventProducerListenerTest extends AndroidTestCase {

	public EventProducerListenerTest() {
		super();
	}
	
	public void testEventProducerListenerGps() throws Exception {
		EventPublisherConfig config = EventPublisherConfig.instance();
		MockEventSender sender = new MockEventSender();
		config.setEventSender(sender);
		sender.expectCallsAmount(2);
		SendEventService service = new SendEventService();
		
		LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
		Location location1 = newLocation(1.0f, 0.5f, 3.4f, 0.0, 12322.0233, 22321.3320);
		Location location2 = newLocation(1.0f, 0.5f, 3.0f, 0.0, 12322.2233, 22321.3320);
		
		sender.replay();
		
		service.onStartCommand(new Intent(), 0, 1);
		manager.setTestProviderLocation("test", location1);
		manager.setTestProviderLocation("test", location2);
		
		sender.verify();
		
		List<EventRow> events = config.getLogEventSender().getEvents();
		Assert.assertNotNull(events);//TODO continue
	}

	private Location newLocation(
			float accuracy, float bearing, float speed,
			double altitude, double latitude, double longitude) {
		Location retval = new Location("test");
		retval.setAccuracy(accuracy);
		retval.setAltitude(altitude);
		retval.setBearing(bearing);
		retval.setLatitude(latitude);
		retval.setLongitude(longitude);
		retval.setSpeed(speed);
		retval.setTime(System.currentTimeMillis());
		return retval;
	}
	
	private static class MockEventSender implements EventSender {
		
		private int callAmount = 0;
		private int callExpected = -1;
		
		public void sendEvent(String userId, String json) {
			callAmount++;
		}
		
		public void expectCallsAmount(int amount) {
			if (amount < 0) {
				throw new IllegalArgumentException("expectCallsAmount should be called with values greater or equal to 0.");
			}
			callExpected = amount;
		}
		
		public void replay() {
			if (callExpected < 0) {
				throw new IllegalArgumentException("expectCallsAmount never called.");
			}
			callAmount = 0;
		}
		
		public void verify() {
			if (callAmount != callExpected) {
				throw new IllegalArgumentException("Should be called " + callExpected + " times but was called" + callAmount + " times.");
			}
		}
	}
}
