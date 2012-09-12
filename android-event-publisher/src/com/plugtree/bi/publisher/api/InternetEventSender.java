package com.plugtree.bi.publisher.api;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class InternetEventSender extends BaseEventSender {

	private final String url;
	
	public InternetEventSender(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	@Override
	public boolean popEvent(EventMessage msg) {
		if (msg == null || msg.getJson() == null) {
			return true; //discard message
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(getUrl() + "?userId=" + msg.getUserId()).openConnection();
			connection.setInstanceFollowRedirects(false);
            connection.setAllowUserInteraction(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:13.0) Gecko/20100101 Firefox/13.0.1");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "en-us,en;q=0.8");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            connection.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            
            PrintWriter writer = null;
            try {
            	writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"), true);
            	writer.append(msg.getJson());
            	writer.flush();
            } finally {
            	if (writer != null) writer.close();
            }
            if (connection.getResponseCode() == 200) {
            	return true;
            }
		} catch (IOException e) {
			Log.e(InternetEventSender.class.getName(), "Couldn't send REST event", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}
}
