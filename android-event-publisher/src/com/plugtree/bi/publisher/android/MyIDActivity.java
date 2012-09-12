package com.plugtree.bi.publisher.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.plugtree.bi.publisher.api.EventPublisherConfig;

public class MyIDActivity extends Activity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.my_id_activity);
        
        final EditText userIdText = (EditText) findViewById(R.id.userIdText);
        userIdText.setText(EventPublisherConfig.instance().getUserId());
        
        Button submitButton = (Button) findViewById(R.id.userDataSubmitButton);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String userId = userIdText.getText().toString();
				EventPublisherConfig.instance().setUserId(userId);
				endWithResult();
			}
		});
		Button backButton = (Button) findViewById(R.id.userDataBackButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endWithResult();
			}
		});
    }

	protected void endWithResult() {
		setResult(RESULT_OK, new Intent(new Intent(MyIDActivity.this, MainMenuActivity.class)));
		finish();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_id_activity, menu);
        return true;
    }
}
