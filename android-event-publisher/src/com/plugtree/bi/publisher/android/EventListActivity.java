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
import android.view.ViewGroup.LayoutParams;
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
        
        LogEventSender eventSender = EventPublisherConfig.instance().getLogEventSender();
        if (eventSender != null) {
        	table.addView(createHeader());
        	List<EventRow> events = eventSender.getEvents();
	        for (final EventRow event : events) {
	        	TableRow row = new TableRow(this);
	        	TextView userId = new TextView(this);
	        	userId.setText(event.getUserId());
	        	TextView key = new TextView(this);
	        	key.setText(event.getKey());
	        	Button button = new Button(this);
	        	button.setText(R.string.event_list_databutton_text);
	        	button.setOnClickListener(new View.OnClickListener() {
					@Override
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
        	noEvents.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        	noEvents.setText(R.string.no_events_to_display);
        	table.addView(noEvents);
        }
        
        Button backButton = (Button) findViewById(R.id.eventListGoBackButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				endWithResult();
			}
		});
    }

	private TableRow createHeader() {
		TableRow header = new TableRow(this);
		TextView header1 = new TextView(this);
		header1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		header1.setText(R.string.event_list_userId_column);
		TextView header2 = new TextView(this);
		header2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		header2.setText(R.string.event_list_key_column);
		TextView header3 = new TextView(this);
		header3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		header3.setText(R.string.event_list_databutton_column);
		header.addView(header1);
		header.addView(header2);
		header.addView(header3);
		return header;
	}

    protected void showAlert(String title, Map<String, float[]> data) {
    	StringBuilder message = new StringBuilder("values: ");
    	float[] values = data.values().iterator().next();
    	for (int index = 0; index < values.length; index++) {
    		message.append(values[index]);
    		if (index + 1 < values.length) {
    			message.append(", ");
    		}
    	}
    	new AlertDialog.Builder(this).setTitle(title).setMessage(message.toString()).
    		setPositiveButton("OK", new DialogInterface.OnClickListener() {
    		@Override
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
