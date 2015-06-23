//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.activities;

import com.appiaries.puzzle.R;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.games.*;
import com.appiaries.puzzle.games.CountDownTextView.CountDownTimerListener;

public class SampleActivity extends BaseActivity implements CountDownTimerListener{

    CountDownTextView mCountDownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();

        // Start Count Down
        mCountDownView.startCountDown();
    }

    @Override
    protected void onDestroy() {
        if (mCountDownView.isCountingDown()) {
            mCountDownView.stopCountDown();
        }
        super.onDestroy();
    }

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
        createAndShowConfirmationDialog(
                R.string.sample__timed_up_confirm_title,
                R.string.sample__timed_up_confirm_message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SampleActivity.this, TopActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void setupView() {
        setContentView(R.layout.activity_sample);

        // Show Back Button
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String filename = intent.getStringExtra(Constants.EXTRA_KEY_IMAGE_FILENAME);
        long remainingTime = intent.getLongExtra(Constants.EXTRA_KEY_TIME_LIMIT, 0);

        // Initialize CountDownTextView
        mCountDownView = new CountDownTextView(SampleActivity.this, SampleActivity.this, remainingTime);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        // Add CountDown View into Main Layout
        final RelativeLayout timerLayout = (RelativeLayout) findViewById(R.id.timer_layout);
        timerLayout.addView(mCountDownView, params);

        // Add SampleImageView into Sample Layout
        final LinearLayout sampleLayout = (LinearLayout) findViewById(R.id.puzzle_layout);
        SampleImageView sampleView = new SampleImageView(getApplicationContext(), filename);
        sampleLayout.addView(sampleView);
    }

}
