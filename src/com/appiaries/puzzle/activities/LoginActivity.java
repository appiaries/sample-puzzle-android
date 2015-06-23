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
import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.common.PreferenceHelper;
import com.appiaries.puzzle.common.Validator;
import com.appiaries.puzzle.models.Player;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class LoginActivity extends BaseActivity {

    private static class ViewHolder {
        public TextView errorMessages;
        public EditText loginId;
        public EditText password;
        public TextView forgotPassword;
        public Button loginButton;
        public Button signUpButton;
    }

    private ViewHolder mViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();
    }

    private void setupView() {
        setContentView(R.layout.activity_login);

        mViewHolder = new ViewHolder();
        mViewHolder.errorMessages  = (TextView) findViewById(R.id.text_error_messages);
        mViewHolder.loginId        = (EditText) findViewById(R.id.edit_login_id);
        mViewHolder.password       = (EditText) findViewById(R.id.edit_password);
        mViewHolder.forgotPassword = (TextView) findViewById(R.id.text_forgot_password);
        mViewHolder.forgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View rootView) {
                Intent intent = new Intent(LoginActivity.this, PasswordReminderActivity.class);
                startActivity(intent);
            }
        });
        mViewHolder.loginButton    = (Button)   findViewById(R.id.button_login);
        mViewHolder.loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (validate()) {
                    String loginId = mViewHolder.loginId.getText().toString();
                    String password = mViewHolder.password.getText().toString();

                    Player player = new Player();
                    player.setLoginId(loginId);
                    player.setPassword(password);

                    // --------------------------------
                    //  Log In
                    // --------------------------------
                    final ProgressDialog progress = createAndShowProgressDialog(R.string.progress__processing);
                    player.logIn(new ResultCallback<Player>() {
                        @Override
                        public void done(ABResult<Player> result, ABException e) {
                            progress.dismiss();
                            if (e == null) {
                                int code = result.getCode();
                                if (code == ABStatus.CREATED) {
                                    Context context = getApplicationContext();
                                    Player sessionPlayer = AB.Session.getUser();
                                    PreferenceHelper.savePlayerId(context, sessionPlayer.getID());
                                    PreferenceHelper.saveToken(context, AB.Session.getToken());
                                    PreferenceHelper.saveNickname(context, sessionPlayer.getNickname());

                                    Intent intent = new Intent(LoginActivity.this, TopActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showUnexpectedStatusCodeError(LoginActivity.this, code);
                                }
                            } else {
                                mViewHolder.errorMessages.setVisibility(View.VISIBLE);
                                if (e.getCode() == ABStatus.UNPROCESSABLE_ENTITY) {
                                    mViewHolder.errorMessages.setText(R.string.message_error__authentication_failure);
                                } else {
                                    mViewHolder.errorMessages.setText(R.string.message_error__wrong_input);
                                }
                                showError(LoginActivity.this, e);
                            }
                        }
                    });

                } else {
                    mViewHolder.errorMessages.setVisibility(View.VISIBLE);
                }
            }
        });
        mViewHolder.signUpButton  = (Button) findViewById(R.id.button_signup);
        mViewHolder.signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View rootView) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // for Debug
        mViewHolder.loginId.setText(R.string.login__default_login_id);
        mViewHolder.password.setText(R.string.login__default_password);
    }

    private boolean validate() {
        boolean isValid = true;
        final String loginId = mViewHolder.loginId.getText().toString();
        if (TextUtils.isEmpty(loginId) || loginId.trim().length() < Constants.ID_MIN_LENGTH || loginId.trim().length() > Constants.ID_MAX_LENGTH) {
            mViewHolder.loginId.setBackgroundResource(R.drawable.focus_border_style);
            isValid = false;
        } else {
            mViewHolder.loginId.setBackgroundResource(R.drawable.edit_text_border_style);
        }

        final String password = mViewHolder.password.getText().toString();
        if (!isValidPassword(password) || password.trim().length() < 6) {
            mViewHolder.password.setBackgroundResource(R.drawable.focus_border_style);
            isValid = false;
        } else {
            mViewHolder.password.setBackgroundResource(R.drawable.edit_text_border_style);
        }
        return isValid;
    }

    private boolean isValidPassword(String val) {
        boolean isValid = false;
        if (!Validator.isNotEmpty(val)) {
            mViewHolder.errorMessages.setVisibility(View.VISIBLE);
            mViewHolder.errorMessages.setText(R.string.message_error__wrong_input);
        } else if (!Validator.isValidPassword(val)) {
            mViewHolder.errorMessages.setVisibility(View.VISIBLE);
            mViewHolder.errorMessages.setText(R.string.message_error__invalid_password);
        } else {
            isValid = true;
        }
        return isValid;
    }

}
