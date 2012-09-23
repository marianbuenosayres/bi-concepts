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
        registerSensor(listener, Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GRAVITY, 
        		Sensor.TYPE_GYROSCOPE, Sensor.TYPE_LIGHT, Sensor.TYPE_LINEAR_ACCELERATION, 
        		Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_ORIENTATION, Sensor.TYPE_PRESSURE, 
        		Sensor.TYPE_PROXIMITY, Sensor.TYPE_ROTATION_VECTOR, 
        		Sensor.TYPE_TEMPERATURE);
        registerGps(listener);
	}
	
	public boolean isStarted() {
		return started;
	}
	
    protected void registerSensor(EventProducerListener listener, int... sensorTypes) {
    	SensorManager manager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
    	
    	int delay = SensorManager.SENSOR_DELAY_NORMAL;
    	if (sensorTypes != null) {
    		for (int type : sensorTypes) {
    			manager.registerListener(listener, manager.getDefaultSensor(type), delay);
    		}
    	}
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
		// TODO Auto-generated method stub
		
	}
}
