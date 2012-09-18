package com.plugtree.bi.publisher.android;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.plugtree.bi.publisher.api.EventPublisherConfig;
import com.plugtree.bi.publisher.api.EventRow;
import com.plugtree.bi.publisher.api.LogEventSender;

public class BackgroundTask extends AsyncTask<Object, Object, Object> {

	private final Context context;
	private final IsVisibleActivity view;
	private final NotificationManager manager; 
	private final EventPublisherConfig config = EventPublisherConfig.instance();
	private int size = 0;
	
	private static final int NOTIFICATION_ID = 1;
	
	public BackgroundTask(Context mContext, IsVisibleActivity view) {
		this.context = mContext;
		this.view = view;
		this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		LogEventSender logger = config.getLogEventSender();
		List<EventRow> events = logger.getEvents();
		this.size = events.size();
	}

	@Override
	protected Object doInBackground(Object... values) {
		while (!view.isVisible()) {
			try {
				Thread.sleep(100);
				LogEventSender logger = config.getLogEventSender();
				List<EventRow> events = logger.getEvents();
				if (events.size() != size) {
					publishProgress(events.size());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	
	@Override
	protected void onProgressUpdate(Object... values) {
		//This method runs on the UI thread, it receives progress updates
        //from the background thread and publishes them to the status bar
		int newSize = Integer.valueOf(String.valueOf(values[0]));
		String contentTitle = context.getString(R.string.new_events);
		int delta = newSize - this.size;
		String contentText = String.valueOf(delta) + " new message";
		if (delta > 1) {
			contentText +="s";
		}
		
		Intent notificationIntent = new Intent();
		int icon = android.R.drawable.stat_sys_upload;
		String tickerText = context.getString(R.string.app_name); //Initial text that appears in the status bar
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        manager.notify(NOTIFICATION_ID, notification);
	}
	
	protected void onPostExecute(Void result)    {
		//The task is complete, tell the status bar about it
		manager.cancel(NOTIFICATION_ID);
	}

}
