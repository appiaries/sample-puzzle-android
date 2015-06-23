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
import com.appiaries.puzzle.common.Validator;
import com.appiaries.puzzle.models.Player;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends BaseActivity {

    private static class ViewHolder {
        public TextView errorMessages;
        public EditText loginId;
        public EditText password;
        public EditText email;
        public EditText nickname;
        public Button signUpButton;
    }

    ViewHolder mViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();
    }

    private void setupView() {
        setContentView(R.layout.activity_signup);

        mViewHolder = new ViewHolder();
        mViewHolder.errorMessages = (TextView) findViewById(R.id.text_error_messages);
        mViewHolder.email         = (EditText) findViewById(R.id.edit_email);
        mViewHolder.loginId       = (EditText) findViewById(R.id.edit_login_id);
        mViewHolder.password      = (EditText) findViewById(R.id.edit_password);
        mViewHolder.nickname      = (EditText) findViewById(R.id.edit_nickname);
        mViewHolder.signUpButton  = (Button)   findViewById(R.id.button_signup);
        mViewHolder.signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (validate()) {

                    Player player = new Player();
                    player.setLoginId(mViewHolder.loginId.getText().toString());
                    player.setPassword(mViewHolder.password.getText().toString());
                    player.setEmail(mViewHolder.email.getText().toString());
                    player.setNickname(mViewHolder.nickname.getText().toString());

                    // --------------------------------
                    //  Sign Up
                    // --------------------------------
                    final ProgressDialog progress = createAndShowProgressDialog(R.string.progress__processing);
                    player.signUp(new ResultCallback<Player>() {
                        @Override
                        public void done(ABResult<Player> result, ABException e) {
                            progress.dismiss();
                            if (e == null) {
                                if (result.getCode() == ABStatus.CREATED) {
                                    createAndShowConfirmationDialog(
                                            R.string.signup__signup_confirm_title,
                                            R.string.signup__signup_confirm_message,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(SignUpActivity.this, IntroductionActivity.class);
                                                    startActivity(intent);
//                                                    finish();
                                                }
                                            }
                                    );
                                }
                            } else {
                                if (e.getCode() == ABStatus.CONFLICT) {
                                    mViewHolder.errorMessages.setText(R.string.message_error__duplicated_id);
                                    mViewHolder.errorMessages.setVisibility(View.VISIBLE);
                                } else {
                                    mViewHolder.errorMessages.setText(R.string.message_error__wrong_input);
                                    mViewHolder.errorMessages.setVisibility(View.VISIBLE);
                                }
                                showError(SignUpActivity.this, e);
                            }
                        }
                    }, AB.UserSignUpOption.LOGIN_AUTOMATICALLY);
                }
            }
        });

        // for Debug
        mViewHolder.loginId.setText(R.string.signup__default_login_id);
        mViewHolder.email.setText(R.string.signup__default_email);
        mViewHolder.password.setText(R.string.signup__default_password);
        mViewHolder.nickname.setText(R.string.signup__default_nickname);
    }

    private boolean validate() {
        boolean isValid = true;
        int errorMessage = R.string.message_error__wrong_input;
        //>> Login ID
        String loginId = mViewHolder.loginId.getText().toString();
        if (TextUtils.isEmpty(loginId) || loginId.trim().length() < Constants.ID_MIN_LENGTH || loginId.trim().length() > Constants.ID_MAX_LENGTH) {
            mViewHolder.loginId.setBackgroundResource(R.drawable.focus_border_style);
            isValid = false;
        } else {
            mViewHolder.loginId.setBackgroundResource(R.drawable.edit_text_border_style);
        }
        //>> Password
        String password = mViewHolder.password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mViewHolder.password.setBackgroundResource(R.drawable.focus_border_style);
            isValid = false;
        } else if (password.trim().length() < Constants.PASSWORD_MIN_LENGTH || password.trim().length() > Constants.PASSWORD_MAX_LENGTH) {
            mViewHolder.password.setBackgroundResource(R.drawable.focus_border_style);
            errorMessage = R.string.message_error__invalid_password;
            isValid = false;
        } else {
            mViewHolder.password.setBackgroundResource(R.drawable.edit_text_border_style);
        }
        //>> Email
        String email = mViewHolder.email.getText().toString();
        if (!Validator.isValidEmail(email)) {
            mViewHolder.email.setBackgroundResource(R.drawable.focus_border_style);
            isValid = false;
        } else {
            mViewHolder.email.setBackgroundResource(R.drawable.edit_text_border_style);
        }
        //>> Nickname
        String nickname = mViewHolder.nickname.getText().toString();
        if (TextUtils.isEmpty(nickname) || nickname.trim().length() < Constants.NICKNAME_MIN_LENGTH || nickname.trim().length() > Constants.NICKNAME_MAX_LENGTH) {
            mViewHolder.nickname.setBackgroundResource(R.drawable.focus_border_style);
            isValid = false;
        } else {
            mViewHolder.nickname.setBackgroundResource(R.drawable.edit_text_border_style);
        }

        if (!isValid) {
            mViewHolder.errorMessages.setText(errorMessage);
            mViewHolder.errorMessages.setVisibility(View.VISIBLE);
        }
        return isValid;
    }

}
