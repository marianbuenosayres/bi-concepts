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
	
	public EventProducerListener(EventPublisherConfig config) {
		this.config = config;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) { }

	@Override
	public void onSensorChanged(SensorEvent event) {
		Map<String, float[]> data = new HashMap<String, float[]>();
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			data.put("sensorAccelerometer", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
			data.put("sensorGravity", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
			data.put("sensorGravity", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			data.put("sensorGyroscope", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			data.put("sensorLight", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			data.put("sensorAcceleration", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			data.put("sensorMagneticField", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			data.put("sensorOrientation", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
			data.put("sensorPressure", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			data.put("sensorProximity", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			data.put("sensorRotationVector", event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_TEMPERATURE) {
			data.put("sensorTemperature", event.values);
		}
        long time = System.currentTimeMillis();
        data.put("sensorTime", new float[] {Float.valueOf(time)});
        config.getEventSender().sendEvent(config.getUserId(), JSONConverter.toJsonFromValues(data));
	}

	@Override
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
		data.put("gpsTime", new float[] { location.getTime() });
        config.getEventSender().sendEvent(config.getUserId(), JSONConverter.toJsonFromValues(data));
	}

	@Override
	public void onProviderDisabled(String provider) { }

	@Override
	public void onProviderEnabled(String provider) { }

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) { }

}
