/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.games;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class CountDownTextView extends TextView{

	private CountDownTimer countDownTimer;
	
	private CountDownTimerListener countDownListener;

	private long elapsedTime = 0;

	private long startTime = 0;

	private final long interval = 1 * 1000;
	
	private final String timerText = "Time: ";

	public CountDownTextView(Context context, CountDownTimerListener listener, long timeCountDown) {
		super(context);
		
		this.startTime = timeCountDown;
		
		timeCountDown = timeCountDown *1000;
		
		countDownTimer = new MyCountDownTimer(timeCountDown, interval);
		
		setText(timerText+ String.valueOf(timeCountDown / 1000));
		
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
		
		setTextColor(Color.parseColor("#000000"));
		
		countDownListener = listener;
	}
	
	public CountDownTextView(Context context, AttributeSet attributeSet)
	{
	    super(context, attributeSet);
	}
	
	public void startCountDown()
	{
		countDownTimer.start();
	}
	
	public long getElaspedTime()
	{
		return this.elapsedTime;
	}
	
	public long getSpentTime()
	{
		return this.startTime - this.elapsedTime;
	}
	
	public void stopCountDown(){
		countDownTimer.cancel();
	}
	public class MyCountDownTimer extends CountDownTimer {

		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval - 500); // fix CountDownTimer bug by increase 500ms of interval value.
		}

		@Override
		public void onFinish() {
			// Raise TimeUp event for PlayActivity
			countDownListener.onTimeUp();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			elapsedTime = millisUntilFinished / 1000;
			setText(timerText + elapsedTime);
		}

	}
	
	public interface CountDownTimerListener{
		void onTimeUp();

	}
}
