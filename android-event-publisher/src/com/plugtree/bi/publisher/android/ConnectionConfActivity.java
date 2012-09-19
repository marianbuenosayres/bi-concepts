package com.plugtree.bi.publisher.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.plugtree.bi.publisher.api.EventPublisherConfig;

public class ConnectionConfActivity extends Activity implements AdapterView.OnItemSelectedListener {

	private static final String SCHEME_BLUETOOTH_TEXT = "Bluetooth";
	private static final String SCHEME_TCP_TEXT = "TCP socket";
	private static final String SCHEME_HTTP_TEXT = "HTTP";
	private static final String SCHEME_BLUETOOTH_ID = "bluetooth";
	private static final String SCHEME_TCP_ID = "tcp";
	private static final String SCHEME_HTTP_ID = "http";
	private static final int TCP_IP_SERVER_TEXT_ID = 0x7fff0001;
	private static final int TCP_IP_PORT_TEXT_ID = 0x7fff0002;
	private static final int BLUETOOTH_SERVER_TEXT_ID = 0x7fff0003;
	private static final int HTTP_URL_TEXT_ID = 0x7fff0004;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection_conf_activity);
		Spinner combo = (Spinner) findViewById(R.id.connectionShemeSpinner);
		ArrayAdapter<CharSequence> comboArray = ArrayAdapter.createFromResource(getApplicationContext(), 
				R.array.connection_scheme_array, android.R.layout.simple_spinner_item);
		combo.setAdapter(comboArray);
		combo.setOnItemSelectedListener(this);
		
		if (EventPublisherConfig.instance().getScheme() != null) {
			String scheme = EventPublisherConfig.instance().getScheme();
			if (SCHEME_HTTP_ID.equals(scheme)) {
				combo.setSelection(comboArray.getPosition(SCHEME_HTTP_TEXT));
			} else if (SCHEME_TCP_ID.equals(scheme)) {
				combo.setSelection(comboArray.getPosition(SCHEME_TCP_TEXT));
			} else if (SCHEME_BLUETOOTH_ID.equals(scheme)) {
				combo.setSelection(comboArray.getPosition(SCHEME_BLUETOOTH_TEXT));
			}
			start(scheme);
		}
	
        Button backButton = (Button) findViewById(R.id.connectionConfGoBackButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				endWithResult();
			}
		});
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		String selectedItem = parent.getSelectedItem().toString();
		start(selectedItem);
	}

	private void start(String selectedItem) {
		if (SCHEME_BLUETOOTH_TEXT.equalsIgnoreCase(selectedItem)) {
        	startBluetoothLayout();
        } else if (SCHEME_TCP_TEXT.equalsIgnoreCase(selectedItem)) {
        	startTcpLayout();
        } else if (SCHEME_HTTP_TEXT.equalsIgnoreCase(selectedItem)) {
        	startHttpLayout();
        }
	}
	
	public void onNothingSelected(AdapterView<?> parent) { }

	public void startBluetoothLayout() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.contentLayout);
		layout.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout horiz = new LinearLayout(getApplicationContext());
		horiz.setOrientation(LinearLayout.HORIZONTAL);
		horiz.setLayoutParams(params);
		
		TextView serverLabel = new TextView(getApplicationContext());
		final EditText serverText = new EditText(getApplicationContext());
		serverText.setLayoutParams(params);
		serverText.setId(BLUETOOTH_SERVER_TEXT_ID);
		serverText.setInputType(InputType.TYPE_CLASS_TEXT);
		if (EventPublisherConfig.instance().getBluetoothServer() != null) {
			serverText.setText(EventPublisherConfig.instance().getBluetoothServer());
		}
		serverLabel.setText(R.string.bluetooth_server_param);
		
		Button submitButton = new Button(getApplicationContext());
		submitButton.setLayoutParams(params);
		submitButton.setText(R.string.bluetooth_submit_button);
		submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String bluetoothServer = serverText.getText().toString();
				EventPublisherConfig.instance().setScheme(SCHEME_BLUETOOTH_ID);
				EventPublisherConfig.instance().setBluetoothServer(bluetoothServer);
				endWithResult();
			}
		});

		horiz.addView(serverLabel);
		horiz.addView(serverText);
		layout.addView(horiz);
		layout.addView(submitButton);
	}
	
	public void startTcpLayout() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.contentLayout);
		layout.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout horiz1 = new LinearLayout(getApplicationContext());
		horiz1.setOrientation(LinearLayout.HORIZONTAL);
		horiz1.setLayoutParams(params);
		
		LinearLayout horiz2 = new LinearLayout(getApplicationContext());
		horiz2.setOrientation(LinearLayout.HORIZONTAL);
		horiz2.setLayoutParams(params);
		
		TextView serverLabel = new TextView(getApplicationContext());
		final EditText serverText = new EditText(getApplicationContext());
		serverText.setLayoutParams(params);
		serverText.setId(TCP_IP_SERVER_TEXT_ID);
		serverText.setInputType(InputType.TYPE_CLASS_TEXT);
        if (EventPublisherConfig.instance().getTcpHost() != null) {
        	serverText.setText(EventPublisherConfig.instance().getTcpHost());
        }
		serverLabel.setText(R.string.tcp_ip_server_param);
		
		TextView portLabel = new TextView(getApplicationContext());
		final EditText portText = new EditText(getApplicationContext());
		portText.setLayoutParams(params);
		serverText.setId(TCP_IP_PORT_TEXT_ID);
		portText.setInputType(InputType.TYPE_CLASS_NUMBER);
		if (EventPublisherConfig.instance().getTcpPort() != null) {
        	portText.setText(EventPublisherConfig.instance().getTcpPort());
        }
		portLabel.setText(R.string.tcp_ip_port_param);
		
		Button submitButton = new Button(getApplicationContext());
		submitButton.setLayoutParams(params);
		submitButton.setText(R.string.tcp_submit_button);
		submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String tcpServer = serverText.getText().toString();
				String tcpPort = portText.getText().toString();
				EventPublisherConfig.instance().setScheme(SCHEME_TCP_ID);
				EventPublisherConfig.instance().setTcpHost(tcpServer);
				EventPublisherConfig.instance().setTcpPort(tcpPort);
				endWithResult();
			}
		});
		
		horiz1.addView(serverLabel);
		horiz1.addView(serverText);
		horiz2.addView(portLabel);
		horiz2.addView(portText);
		layout.addView(horiz1);
		layout.addView(horiz2);
		layout.addView(submitButton);
	}
	
	public void startHttpLayout() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.contentLayout);
		layout.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout horiz = new LinearLayout(getApplicationContext());
		horiz.setOrientation(LinearLayout.HORIZONTAL);
		horiz.setLayoutParams(params);
		
		TextView urlLabel = new TextView(getApplicationContext());
		final EditText urlText = new EditText(getApplicationContext());
		urlText.setLayoutParams(params);
		urlText.setId(HTTP_URL_TEXT_ID);
		urlText.setInputType(InputType.TYPE_CLASS_TEXT);
        if (EventPublisherConfig.instance().getHttpUrl() != null) {
        	urlText.setText(EventPublisherConfig.instance().getHttpUrl());
        }
		urlLabel.setText(R.string.http_url_param);
		
		Button submitButton = new Button(getApplicationContext());
		submitButton.setLayoutParams(params);
		submitButton.setText(R.string.http_submit_button);
		submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String url = urlText.getText().toString();
				EventPublisherConfig.instance().setScheme(SCHEME_HTTP_ID);
				EventPublisherConfig.instance().setHttpUrl(url);
				endWithResult();
			}
		});
		
		horiz.addView(urlLabel);
		horiz.addView(urlText);
		layout.addView(horiz);
		layout.addView(submitButton);
	}
	
	protected void endWithResult() {
		setResult(RESULT_OK, new Intent(ConnectionConfActivity.this, MainMenuActivity.class));
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.connection_conf_activity, menu);
        return true;
	}
}
