package com.plugtree.bi.publisher.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.plugtree.bi.publisher.api.EventPublisherConfig;

public class MainMenuActivity extends Activity implements ServiceConnection {

	private static final int USER_DATA_REQUEST = 1;
	private static final int CONNECTION_CONF_REQUEST = 2;
	private static final int EVENT_LIST_REQUEST = 3;

	
	private EventPublisherConfig config = EventPublisherConfig.instance();
	private SendEventService senderService = null;
	private boolean serviceBound = false;
	
	@Override
	public void onServiceDisconnected(ComponentName name) {
		this.senderService = null;
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		SendEventService.MBinder binder = (SendEventService.MBinder) service;
		this.senderService = binder.getService();
		if (!this.senderService.isStarted()) {
			startService(new Intent(MainMenuActivity.this, SendEventService.class));
		}
		this.serviceBound = true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_activity);
    	if (savedInstanceState != null) {
    		super.onCreate(savedInstanceState);
			config.setUserId(savedInstanceState.getString("userId"));
			config.setTcpHost(savedInstanceState.getString("tcpHost"));
			config.setTcpPort(savedInstanceState.getString("tcpPort"));
			config.setBluetoothServer(savedInstanceState.getString("bluetoothServer"));
			config.setHttpUrl(savedInstanceState.getString("httpUrl"));
			config.setScheme(savedInstanceState.getString("scheme"));
    	}

		Button configUserDataButton = (Button) findViewById(R.id.configUserDataButton);
		configUserDataButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startUserData();
			}
		});
		Button configConnectionButton = (Button) findViewById(R.id.configConnectionButton);
		configConnectionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startConnection();
			}
		});
		Button eventListButton = (Button) findViewById(R.id.eventListButton);
		eventListButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startEventList();
			}
		});
        Button minimize = (Button) findViewById(R.id.minimizeButton);
        minimize.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveTaskToBack(true);
			}
		});
        
        bindService(new Intent(MainMenuActivity.this, SendEventService.class), this, BIND_AUTO_CREATE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_activity, menu);
        return true;
	}
	
	public void startUserData() {
		startActivityForResult(new Intent(MainMenuActivity.this, MyIDActivity.class), USER_DATA_REQUEST);
	}
	
	public void startConnection() {
		startActivityForResult(new Intent(MainMenuActivity.this, ConnectionConfActivity.class), CONNECTION_CONF_REQUEST);
	}
	
	public void startEventList() {
		startActivityForResult(new Intent(MainMenuActivity.this, EventListActivity.class), EVENT_LIST_REQUEST);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("userId", config.getUserId());
		outState.putString("scheme", config.getScheme());
		outState.putString("bluetoothServer", config.getBluetoothServer());
		outState.putString("tcpHost", config.getTcpHost());
		outState.putString("tcpPort", config.getTcpPort());
		outState.putString("httpUrl", config.getHttpUrl());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CONNECTION_CONF_REQUEST) {
			if (serviceBound) {
				senderService.updateEventSender();
			} else {
				showAlert("Error", "Couldn't find sender service");
			}
		}
	}
	
	private void showAlert(String title, String message) {
		 new AlertDialog.Builder(this).setTitle(title).setMessage(message).
         setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                	 dialog.cancel();
                 }
         }).show();
	}
}