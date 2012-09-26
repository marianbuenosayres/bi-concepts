package com.plugtree.bi.publisher.android;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;

import com.plugtree.bi.publisher.api.BluetoothEventSender;
import com.plugtree.bi.publisher.api.EventPublisherConfig;
import com.plugtree.bi.publisher.api.InternetEventSender;
import com.plugtree.bi.publisher.api.TCPEventSender;

public class SendEventService extends Service {

	private boolean started;
	private EventProducerListener listener = null;
	
	public SendEventService() {
		super();
		this.started = false;
	}
	
	/**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class MBinder extends Binder {
    	SendEventService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SendEventService.this;
        }
    };
    
    private final IBinder mBinder = new MBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.listener = new EventProducerListener(EventPublisherConfig.instance());
		start(this.listener);
        int retval = super.onStartCommand(intent, flags, startId);
        this.started = true;
		return retval;
	}
	
	public void start(EventProducerListener listener) {
		registerSensor(listener, Sensor.TYPE_ACCELEROMETER, 0.1f, 0.1f, 0.1f); //10% accuracy on each axis
		registerSensor(listener, Sensor.TYPE_GRAVITY, 0.3f, 0.3f, 0.3f); //30% accuracy on each axis
		registerSensor(listener, Sensor.TYPE_LIGHT, 0.1f); //10% accuracy for luminaries
		registerSensor(listener, Sensor.TYPE_LINEAR_ACCELERATION, 0.5f, 0.5f, 0.5f); //50% accuracy on each axis
		registerSensor(listener, Sensor.TYPE_MAGNETIC_FIELD, 100f, 0.1f, 0.2f); //any value for z axis, 10% and 20% for other axis
		registerSensor(listener, Sensor.TYPE_ORIENTATION, 0.05f, 0.05f, 0.05f); //5% accuracy on each axis
		registerSensor(listener, Sensor.TYPE_ROTATION_VECTOR, 1f, 1f, 1f); //100% accuracy on each axis (almost nothing
		registerSensor(listener, Sensor.TYPE_TEMPERATURE, 0,01f); //1% accuracy for degrees
		registerSensor(listener, Sensor.TYPE_GYROSCOPE, 0.3f, 0.3f, 0.3f); //30% accuracy on each axis
		registerSensor(listener, Sensor.TYPE_PRESSURE, 0.2f);//20% accuracy (few models handle this variable anyway)
		registerSensor(listener, Sensor.TYPE_PROXIMITY, 0.2f); //20% accuracy
        registerGps(listener);
	}
	
	public boolean isStarted() {
		return started;
	}
	
	protected void registerSensor(EventProducerListener listener, int sensorType, float... accuracies) {
		SensorManager manager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
		listener.setAccuracies(sensorType, accuracies);
		manager.registerListener(listener, manager.getDefaultSensor(sensorType), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
    protected void registerGps(EventProducerListener listener) {
    	LocationManager manager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
    	manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
    }

	public void updateEventSender() {
		EventPublisherConfig config = EventPublisherConfig.instance();
		if (config.getScheme().equalsIgnoreCase("HTTP")) {
			config.setEventSender(new InternetEventSender(config.getHttpUrl()));
		} else if (config.getScheme().equalsIgnoreCase("TCP socket")) {
			config.setEventSender(new TCPEventSender(config.getTcpHost(), Integer.valueOf(config.getTcpPort())));
		} else if (config.getScheme().equalsIgnoreCase("Bluetooth")) {
			config.setEventSender(new BluetoothEventSender(config.getBluetoothServer()));
		}
	}

	public void restart() {
		EventPublisherConfig.instance().getLogEventSender().getEvents().clear();
		updateEventSender();
	}
}
