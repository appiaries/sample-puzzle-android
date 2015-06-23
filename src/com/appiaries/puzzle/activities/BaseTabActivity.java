//
// Copyright (c) 2015 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.appiaries.baas.sdk.ABException;
import com.appiaries.puzzle.R;

public class BaseTabActivity extends TabActivity {
    private static final String TAG = BaseTabActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("UnCaughtException", paramThrowable.getLocalizedMessage());
                paramThrowable.printStackTrace();

                Intent intent = new Intent(BaseTabActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                System.exit(0);
            }
        });
    }

    protected ProgressDialog createAndShowProgressDialog(int stringId) {
        return createAndShowProgressDialog(getString(stringId));
    }
    protected ProgressDialog createAndShowProgressDialog(String message) {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        return progress;
    }

    protected void createAndShowConfirmationDialog(int titleId, int messageId, DialogInterface.OnClickListener onClickListener) {
        createAndShowConfirmationDialog(getString(titleId), getString(messageId), onClickListener);
    }
    protected void createAndShowConfirmationDialog(String title, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //>> Custom Title
        TextView textTitle = new TextView(this);
        textTitle.setText(title);
        textTitle.setPadding(10, 10, 10, 10);
        textTitle.setGravity(Gravity.CENTER);
        textTitle.setTextSize(20);
        builder.setCustomTitle(textTitle);
        //>> Custom Message
        TextView textMessage = new TextView(this);
        textMessage.setPadding(10, 40, 10, 40);
        textMessage.setText(message);
        textMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        textMessage.setTextSize(20);
        builder.setView(textMessage);
        //>> OK Button Handler
        builder.setPositiveButton(R.string.base__ok_button, onClickListener);

        builder.show();
    }

    protected void showMessage(Context context, int resourceId) {
        showMessage(context, getString(resourceId));
    }
    protected void showMessage(Context context, String format, Object... args) {
        String message = String.format(format, args);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.i(TAG, message);
    }
    protected void showError(Context context, ABException e) {
        String message;
        if (e.getDetailCodes() != null) {
            message = String.format("[ERROR] code:%d, detail:%s, reason:%s", e.getCode(), e.getDetailCodes(), e.getMessage());
        } else {
            message = String.format("[ERROR] code:%d, reason:%s", e.getCode(),  e.getMessage());
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, message);
        e.printStackTrace();
    }
    protected void showUnexpectedStatusCodeError(Context context, int code) {
        String message = String.format("[ERROR] Unexpected status code: %d", code);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, message);
    }

}
