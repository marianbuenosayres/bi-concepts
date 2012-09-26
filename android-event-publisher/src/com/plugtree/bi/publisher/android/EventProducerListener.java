package com.plugtree.bi.publisher.android;

import java.util.HashMap;
import java.util.Map;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.plugtree.bi.publisher.api.EventPublisherConfig;
import com.plugtree.bi.publisher.api.JSONConverter;

public class EventProducerListener implements SensorEventListener, LocationListener {

	private final EventPublisherConfig config;
	
	private Map<String, float[]> lastEvents = new HashMap<String, float[]>();
	private Map<String, float[]> accuracies = new HashMap<String, float[]>();
	private Map<String, Long> lastOcurrence = new HashMap<String, Long>();
	
	public EventProducerListener(EventPublisherConfig config) {
		this.config = config;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) { }

	protected String getSensorKey(int sensorType) {
		String retval = null;
		switch (sensorType) {
		case Sensor.TYPE_ACCELEROMETER: retval = "sensorAccelerometer"; break;
		case Sensor.TYPE_GRAVITY: retval = "sensorGravity"; break;
		case Sensor.TYPE_GYROSCOPE: retval = "sensorGyroscope"; break;
		case Sensor.TYPE_LIGHT: retval = "sensorLight"; break;
		case Sensor.TYPE_LINEAR_ACCELERATION: retval = "sensorLinearAcceleration"; break;
		case Sensor.TYPE_MAGNETIC_FIELD: retval = "sensorMagneticField"; break;
		case Sensor.TYPE_ORIENTATION: retval = "sensorOrientation"; break;
		case Sensor.TYPE_PRESSURE: retval = "sensorPressure"; break;
		case Sensor.TYPE_PROXIMITY: retval = "sensorProximity"; break;
		case Sensor.TYPE_ROTATION_VECTOR: retval = "sensorRotationVector"; break;
		case Sensor.TYPE_TEMPERATURE: retval = "sensorTemperature"; break;
		}
		return retval;
	}
	
	public void onSensorChanged(SensorEvent event) {
		Map<String, float[]> data = new HashMap<String, float[]>();
		String key = getSensorKey(event.sensor.getType());
		if (key != null && hasChanged(key, event.values)) {
			data.put(key, event.values);
		}
		long time = System.currentTimeMillis();
		if (!data.isEmpty() && timeChanged(key, time)) {
			data.put("sensorTime", new float[] {Float.valueOf(time)});
			config.getEventSender().sendEvent(config.getUserId(), JSONConverter.toJsonFromValues(data));
		}
	}

	public void onLocationChanged(Location location) {
		Map<String, float[]> data = new HashMap<String, float[]>();
		data.put("gpsLatitude", new float[] { (float) location.getLatitude() });
		data.put("gpsLongitude", new float[] { (float) location.getLongitude() });
		if (location.hasAltitude()) {
			data.put("gpsAltitude", new float[] { (float) location.getAltitude() });
		}
		if (location.hasAccuracy()) {
			data.put("gpsAccuracy", new float[] { location.getAccuracy() });
		}
		if (location.hasSpeed()) {
			data.put("gpsSpeed", new float[] { location.getSpeed() });
		}
		if (location.hasBearing()) {
			data.put("gpsBearing", new float[] { location.getBearing() });
		}
		if (!data.isEmpty() && timeChanged("gps", location.getTime())) {
			data.put("gpsTime", new float[] { location.getTime() });
			config.getEventSender().sendEvent(config.getUserId(), JSONConverter.toJsonFromValues(data));
		}
	}
	
	/**
	 * Only pay attention if time of the reading has changed
	 */
	protected boolean timeChanged(String key, long time) {
		boolean retval = false;
		Long lastTime = lastOcurrence.get(key);
		if (lastTime == null) {
			retval = true;
		} else if (time != lastTime) {
			retval = true;
		}
		if (retval) {
			lastOcurrence.put(key, time);
		}
		return retval;
		
	}
	
	/**
	 * if any of the values change by 0.1%, it is considered as changed 
	 */
	protected boolean hasChanged(String key, float[] values) {
		boolean retval = false;
		float[] oldValues = lastEvents.get(key);
		if (oldValues == null) {
			retval = true;
		} else {
			for (int index = 0; index < values.length; index++) {
				if (oldValues.length <= index) {
					retval = true;
					break;
				}
				float newValue = (values[index]*2f) / (values[index] + oldValues[index]);
				float oldValue = (oldValues[index]*2f) / (values[index] + oldValues[index]);
				float[] accuracies = this.accuracies.get(key);
				float accuracy = (accuracies.length > index) ? accuracies[index] : 0.0001f;
				//If value has changed by more than expected accuracy (0.1% being default), consider it changed
				if (newValue > oldValue + accuracy || newValue < oldValue - accuracy) {
					retval = true;
					break;
				}
			}
		}
		if (retval) {
			lastEvents.put(key, values.clone());
		}
		return retval;
	}
	
	public void onProviderDisabled(String provider) { }

	public void onProviderEnabled(String provider) { }

	public void onStatusChanged(String provider, int status, Bundle extras) { }

	/* for quicktest purposes only */
	public static void main(String[] args) {
		EventProducerListener listener = new EventProducerListener(EventPublisherConfig.instance());
		
		float[] values1 = new float[] {1.0f, 2.0f, 3.0f};
		float[] values2 = new float[] {1.0f, 2.0001f, 3.0f};
		float[] values3 = new float[] {1.1f, 2.0f, 3.0f};
		
		boolean change1 = listener.hasChanged("prueba", values1);
		boolean change2 = listener.hasChanged("prueba", values2);
		boolean change3 = listener.hasChanged("prueba", values3);
		
		System.out.println("change1 tiene que ser true y es " + change1);
		System.out.println("change2 tiene que ser false y es " + change2);
		System.out.println("change3 tiene que ser true y es " + change3);
	}

	public void setAccuracies(int sensorType, float[] accuracies) {
		String key = getSensorKey(sensorType);
		this.accuracies.put(key, accuracies);
	}

}
