//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.activities;

import com.appiaries.baas.sdk.AB;
import com.appiaries.baas.sdk.ABException;
import com.appiaries.baas.sdk.ABResult;
import com.appiaries.baas.sdk.ABStatus;
import com.appiaries.baas.sdk.ResultCallback;
import com.appiaries.puzzle.R;
import com.appiaries.puzzle.common.Validator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PasswordReminderActivity extends BaseActivity {

    private static class ViewHolder {
        public EditText email;
        public TextView errorMessages;
        public Button sendButton;
    }

    private ViewHolder mViewHolder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();
    }

    private void setupView() {
        setContentView(R.layout.activity_password_reminder);

        mViewHolder = new ViewHolder();
        mViewHolder.email         = (EditText) findViewById(R.id.edit_email);
        mViewHolder.errorMessages = (TextView) findViewById(R.id.text_error_messages);
        mViewHolder.sendButton    = (Button)   findViewById(R.id.button_send);
        mViewHolder.sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (validate()) {
                    // --------------------------------
                    //  Reset Password
                    // --------------------------------
                    String email = mViewHolder.email.getText().toString();
                    AB.UserService.resetPasswordForEmail(email, new ResultCallback<Void>() {
                        @Override
                        public void done(ABResult<Void> result, ABException e) {
                            if (e == null) {
                                int code = result.getCode();
                                if (code == ABStatus.NO_CONTENT) {
                                    createAndShowConfirmationDialog(
                                            R.string.password_reminder__reset_confirm_title,
                                            R.string.password_reminder__reset_confirm_message,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(PasswordReminderActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                    );
                                } else {
                                    showUnexpectedStatusCodeError(PasswordReminderActivity.this, code);
                                }
                            } else {
                                showError(PasswordReminderActivity.this, e);
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        int errorMessage = R.string.message_error__wrong_input;
        //>> Email
        String email = mViewHolder.email.getText().toString();
        if (!Validator.isValidEmail(email)) {
            mViewHolder.email.setBackgroundResource(R.drawable.focus_border_style);
            isValid = false;
        } else {
            mViewHolder.email.setBackgroundResource(R.drawable.edit_text_border_style);
        }

        if (!isValid) {
            mViewHolder.errorMessages.setText(errorMessage);
            mViewHolder.errorMessages.setVisibility(View.VISIBLE);
        }
        return isValid;
    }

}
