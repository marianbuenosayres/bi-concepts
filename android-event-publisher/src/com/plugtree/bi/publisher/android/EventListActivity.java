package com.plugtree.bi.publisher.android;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.plugtree.bi.publisher.api.EventPublisherConfig;
import com.plugtree.bi.publisher.api.EventRow;
import com.plugtree.bi.publisher.api.LogEventSender;

public class EventListActivity extends Activity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.event_list_activity);
        
        final TableLayout table = (TableLayout) findViewById(R.id.eventListTable);
        loadData(table);
        
        Button backButton = (Button) findViewById(R.id.eventListGoBackButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				endWithResult();
			}
		});
		Button reloadButton = (Button) findViewById(R.id.eventListReloadButton); 
		reloadButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				loadData(table);
			}
		});
    }

	private void loadData(final TableLayout table) {
		LogEventSender eventSender = EventPublisherConfig.instance().getLogEventSender();
		table.removeAllViews();
        if (eventSender != null && eventSender.getEvents() != null && !eventSender.getEvents().isEmpty()) {
        	table.addView(createHeader());
        	List<EventRow> events = eventSender.getEvents();
        	
        	TableLayout.LayoutParams params = new TableLayout.LayoutParams();
        	params.width = TableLayout.LayoutParams.FILL_PARENT;
        	params.height = TableLayout.LayoutParams.WRAP_CONTENT;
        	params.rightMargin = 3;
        	params.leftMargin = 3;
        	
	        for (final EventRow event : events) {
	        	TableRow row = new TableRow(getApplicationContext());
	        	row.setLayoutParams(params);
	        	
	        	TextView userId = new TextView(getApplicationContext());
	        	userId.setText(event.getUserId());
	        	TextView key = new TextView(getApplicationContext());
	        	key.setText(event.getKey());
	        	Button button = new Button(getApplicationContext());
	        	button.setText(R.string.event_list_databutton_text);
	        	button.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						showAlert("Data", event.getData());
					}
				});
	        	row.addView(userId);
	        	row.addView(key);
	        	row.addView(button);
	        	table.addView(row);
	        }
        } else {
        	TextView noEvents = new TextView(this);
        	ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
        			ViewGroup.LayoutParams.FILL_PARENT, 
        			ViewGroup.LayoutParams.WRAP_CONTENT
        	);
        	noEvents.setLayoutParams(params);
        	noEvents.setText(R.string.no_events_to_display);
        	table.addView(noEvents);
        }
	}

	private TableRow createHeader() {
		TableRow header = new TableRow(getApplicationContext());
		TableRow.LayoutParams params = new TableRow.LayoutParams();
		header.setBackgroundColor(getResources().getColor(R.color.silver));
		params.leftMargin = 3;
		params.rightMargin = 3;
		TextView header1 = new TextView(getApplicationContext());
		header1.setText(R.string.event_list_userId_column);
		header1.setLayoutParams(params);
		TextView header2 = new TextView(getApplicationContext());
		header2.setText(R.string.event_list_key_column);
		header2.setLayoutParams(params);
		TextView header3 = new TextView(getApplicationContext());
		header3.setText(R.string.event_list_databutton_column);
		header3.setLayoutParams(params);
		header.addView(header1);
		header.addView(header2);
		header.addView(header3);
		return header;
	}

    protected void showAlert(String title, Map<String, float[]> data) {
    	StringBuilder message = new StringBuilder("");
    	for (Map.Entry<String, float[]> entry : data.entrySet()) {
    		message.append(entry.getKey() + ": ");
    		float[] values = entry.getValue();
        	for (int index = 0; index < values.length; index++) {
        		message.append(values[index]);
        		if (index + 1 < values.length) {
        			message.append(", ");
        		}
        	}
        	message.append(". ");
    	}
    	new AlertDialog.Builder(this).
    		setTitle(title).
    		setMessage(message.toString()).
    		setPositiveButton("OK", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.cancel();
    			}
    		}).create().show();
    }
    
	protected void endWithResult() {
		setResult(RESULT_OK, new Intent(EventListActivity.this, MainMenuActivity.class));
		finish();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_list_activity, menu);
        return true;
    }

}
