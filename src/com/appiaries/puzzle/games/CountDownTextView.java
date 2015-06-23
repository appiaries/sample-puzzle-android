//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.games;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.appiaries.puzzle.R;

public class CountDownTextView extends TextView {
    private static final String TAG = CountDownTextView.class.getSimpleName();

    private RemainingTimeCountDownTimer mCountDownTimer;
    private CountDownTimerListener mCountDownListener;
    private long mRemainingTime = 0;
    private long mLimitTime = 0;
    private String mTimerText;
    private static final long sINTERVAL = (long) 1 * 1000;
    private boolean mCountingDown;
    private String mLabel;

    public CountDownTextView(Context context, CountDownTimerListener listener, long limitTime) {
        super(context);

        mLimitTime = limitTime;
        mRemainingTime = limitTime;

        mCountDownTimer = new RemainingTimeCountDownTimer(limitTime * 1000, sINTERVAL);
        mTimerText = context.getResources().getString(R.string.count_down__time);
        setText(mTimerText + String.valueOf(mLimitTime));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        setTextColor(Color.parseColor("#000000"));
        mCountDownListener = listener;
        mLabel = mCountDownListener.getClass().getSimpleName();
    }

    public CountDownTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public long getRemainingTime() {
        return mRemainingTime;
    }

    public long getElapsedTime() {
        return mLimitTime - mRemainingTime;
    }

    public void startCountDown() {
        mCountDownTimer.start();
        mCountingDown = true;
    }

    public void stopCountDown() {
        mCountDownTimer.cancel();
        mCountingDown = false;
    }

    public boolean isCountingDown() {
        return mCountingDown;
    }

    public class RemainingTimeCountDownTimer extends CountDownTimer {
        public RemainingTimeCountDownTimer(long startTime, long interval) {
            super(startTime, interval - 500); // fix CountDownTimer bug by increase 500ms of interval value.
        }

        @Override
        public void onFinish() {
            // Raise TimeUp event for PlayActivity
            mCountDownListener.onTimeUp();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mRemainingTime = millisUntilFinished / 1000;
            setText(mTimerText + mRemainingTime);
            /* Log.d(TAG, "---- onTick() <"  + mLabel + "> : millisUntilFinished=" + millisUntilFinished + ", text=" + getText()); */
        }

    }

    public interface CountDownTimerListener {
        void onTimeUp();
    }

}
