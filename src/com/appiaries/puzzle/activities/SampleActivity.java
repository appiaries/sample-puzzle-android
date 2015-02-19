/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.activities;

import com.appiaries.puzzle.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appiaries.puzzle.common.UIHelper;
import com.appiaries.puzzle.games.*;
import com.appiaries.puzzle.games.CountDownTextView.CountDownTimerListener;

public class SampleActivity extends Activity implements CountDownTimerListener{

	private String imageId = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sample);
		final Bundle extras = getIntent().getExtras();		
		imageId = extras.getString("imageId");
		long timeCountDownRemain = extras.getLong("timeLimit");
		
		// Show Back Button
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Get Timer Layout
		final RelativeLayout timerLayout = (RelativeLayout) findViewById(R.id.timer_layout);

		// Initial CountDownTextView
		CountDownTextView countDownView = new CountDownTextView(
				SampleActivity.this, SampleActivity.this,timeCountDownRemain);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);

		// Add CountDown View into Main Layout
		timerLayout.addView(countDownView, params);

		// Get Sample Layout
		final LinearLayout sampleLayout = (LinearLayout) findViewById(R.id.puzzle_layout);

		SampleImageView sampleView = new SampleImageView(
				getApplicationContext(), imageId);

		// Add SampleImageView into Sample Layout
		sampleLayout.addView(sampleView);

		// Start Count Down
		countDownView.startCountDown();
		
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu items for use in the action bar MenuInflater inflater =
	 * getMenuInflater(); inflater.inflate(R.menu.play_menu, menu); return
	 * super.onCreateOptionsMenu(menu); }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTimeUp() {
		DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent i = new Intent(SampleActivity.this, TopActivity.class);

				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(i);

				finish();
			}
		};
		
		UIHelper.showNotifyDialog(SampleActivity.this, "残念！時間切れです", dialogListener);	
	}
}
