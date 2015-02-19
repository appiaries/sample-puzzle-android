/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.activities;

import com.appiaries.APIS;
import com.appiaries.puzzle.R;
import com.appiaries.puzzle.common.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		APIS.initialize(Constants.APIS_DATASTORE_ID, Constants.APIS_APP_ID,
				Constants.APIS_APP_TOKEN, getApplicationContext());		
				
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// Session is still valid => Open Main Activity
//				Intent i = new Intent(SplashActivity.this, TimeRankingActivity.class);

				Intent i = new Intent(SplashActivity.this, LoginActivity.class);
				
				startActivity(i);
				finish();
			}
		}, 3000);
	}
}
