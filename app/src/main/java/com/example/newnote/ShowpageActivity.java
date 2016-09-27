package com.example.newnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShowpageActivity extends Activity {

	private TextView titleText;
	private TextView contentText;
	private TextView timeText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpage);
		titleText = (TextView)findViewById(R.id.click_title);
		contentText = (TextView)findViewById(R.id.click_content);
		timeText = (TextView)findViewById(R.id.click_time);
		
		Intent intent = getIntent();
		
	
		String title = intent.getStringExtra("title");
		String content = intent.getStringExtra("content");
		String time = intent.getStringExtra("time");
		

		

		
		this.titleText.setText(title);
		this.contentText.setText(content);
	
	}

}

