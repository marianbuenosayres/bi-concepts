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
	
	public EventProducerListener(EventPublisherConfig config) {
		this.config = config;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) { }

	public void onSensorChanged(SensorEvent event) {
		Map<String, float[]> data = new HashMap<String, float[]>();
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			if (hasChanged("sensorAccelerometer", event.values)) {
				data.put("sensorAccelerometer", event.values);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
			if (hasChanged("sensorGravity", event.values)) {
				data.put("sensorGravity", event.values);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			if (hasChanged("sensorGyroscope", event.values)) {
				data.put("sensorGyroscope", event.values);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			if (hasChanged("sensorLight", event.values)) {
				data.put("sensorLight", event.values);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			if (hasChanged("sensorAcceleration", event.values)) {
				data.put("sensorAcceleration", event.values);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			if (hasChanged("sensorMagneticField", event.values)) {
				data.put("sensorMagneticField", event.values);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			if (hasChanged("sensorOrientation", event.values)) {
				data.put("sensorOrientation", event.values);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
			if (hasChanged("sensorPressure", event.values)) {
				data.put("sensorPressure", event.values);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			if (hasChanged("sensorProximity", event.values)) {
				data.put("sensorProximity", event.values);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			if (hasChanged("sensorRotationVector", event.values)) {
				data.put("sensorRotationVector", event.values);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_TEMPERATURE) {
			if (hasChanged("sensorTemperature", event.values)) {
				data.put("sensorTemperature", event.values);
			}
		}
		if (!data.isEmpty()) {
			long time = System.currentTimeMillis();
			data.put("sensorTime", new float[] {Float.valueOf(time)});
			config.getEventSender().sendEvent(config.getUserId(), JSONConverter.toJsonFromValues(data));
		}
	}

	public void onLocationChanged(Location location) {
		Map<String, float[]> data = new HashMap<String, float[]>();
		if (hasChanged("gpsLatitude", new float[] { (float) location.getLatitude() })) {
			data.put("gpsLatitude", new float[] { (float) location.getLatitude() });
		}
		if (hasChanged("gpsLongitude", new float[] { (float) location.getLongitude() })) {
			data.put("gpsLongitude", new float[] { (float) location.getLongitude() });
		}
		if (location.hasAltitude() && hasChanged("gpsAltitude", new float[] { (float) location.getAltitude() })) {
			data.put("gpsAltitude", new float[] { (float) location.getAltitude() });
		}
		if (location.hasAccuracy() && hasChanged("gpsAccuracy", new float[] { location.getAccuracy() })) {
			data.put("gpsAccuracy", new float[] { location.getAccuracy() });
		}
		if (location.hasSpeed() && hasChanged("gpsSpeed", new float[] { location.getSpeed() })) {
			data.put("gpsSpeed", new float[] { location.getSpeed() });
		}
		if (location.hasBearing() && hasChanged("gpsBearing", new float[] { location.getBearing() })) {
			data.put("gpsBearing", new float[] { location.getBearing() });
		}
		if (!data.isEmpty()) {
			data.put("gpsTime", new float[] { location.getTime() });
			config.getEventSender().sendEvent(config.getUserId(), JSONConverter.toJsonFromValues(data));
		}
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
			
				if (newValue > oldValue + 0.0001 && newValue < oldValue - 0.0001) {
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

}
