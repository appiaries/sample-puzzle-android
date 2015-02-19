/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.activities;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import com.appiaries.APISException;
import com.appiaries.APISResult;
import com.appiaries.puzzle.R;
import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.managers.PlayerManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author ntduc
 * 
 */
@SuppressWarnings("unchecked")
public class SignUpActivity extends Activity {
	PlanetHolder planetHolder;
	ProgressDialog progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		CreateView();
	}

	private void CreateView() {

		planetHolder = new PlanetHolder();

		EditText txtId = (EditText) findViewById(R.id.txtId);
		EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
		EditText txtNickName = (EditText) findViewById(R.id.txtNickName);
		EditText txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
		TextView txtValidation = (TextView) findViewById(R.id.validation);

		Button btnOk = (Button) findViewById(R.id.btnOk);

		planetHolder.txtValidation = txtValidation;
		planetHolder.txtEmailAddress = txtEmailAddress;
		planetHolder.txtId = txtId;
		planetHolder.txtPassword = txtPassword;
		planetHolder.txtNickName = txtNickName;
		planetHolder.btnOk = btnOk;

		planetHolder.btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (validationFrom(planetHolder)) {
					String strLoginId = planetHolder.txtId.getText().toString();
					String strPassword = planetHolder.txtPassword.getText()
							.toString();
					String strEmail = planetHolder.txtEmailAddress.getText()
							.toString();
					String strNickName = planetHolder.txtNickName.getText()
							.toString();

					HashMap<String, String> data = new HashMap<String, String>();
					data.put("loginId", strLoginId);
					data.put("password", strPassword);
					data.put("password", strPassword);
					data.put("email", strEmail);
					data.put("nickname", strNickName);
					SendUserRegisterAsyncTask task = new SendUserRegisterAsyncTask();
					task.execute(data);
				} else {
					planetHolder.txtValidation.setText(R.string.mesError);
					planetHolder.txtValidation.setVisibility(View.VISIBLE);
				}

			}
		});

	}

	/**
	 * validation From
	 * 
	 * @param planetHolder
	 */
	private Boolean validationFrom(final PlanetHolder planetHolder) {
		Boolean flg = true;
		final String txtId = planetHolder.txtId.getText().toString();
		if (!isNullOrEmpty(txtId) || txtId.trim().length() < 3
				|| txtId.trim().length() > 20) {
			planetHolder.txtId
					.setBackgroundResource(R.drawable.focus_border_style);
			flg = false;
		} else {
			planetHolder.txtId
					.setBackgroundResource(R.drawable.edit_text_border_style);
		}

		final String pass = planetHolder.txtPassword.getText().toString();
		if (!isNullOrEmpty(pass) || pass.trim().length() < 6) {
			planetHolder.txtPassword
					.setBackgroundResource(R.drawable.focus_border_style);
			flg = false;
		} else {
			planetHolder.txtPassword
					.setBackgroundResource(R.drawable.edit_text_border_style);
		}
		final String emailAddress = planetHolder.txtEmailAddress.getText()
				.toString();
		if (!isValidEmail(emailAddress)) {
			planetHolder.txtEmailAddress
					.setBackgroundResource(R.drawable.focus_border_style);
			flg = false;
		} else {
			planetHolder.txtEmailAddress
					.setBackgroundResource(R.drawable.edit_text_border_style);
		}
		final String nickName = planetHolder.txtNickName.getText().toString();
		if (!isNullOrEmpty(emailAddress) || nickName.trim().length() < 3) {
			planetHolder.txtNickName
					.setBackgroundResource(R.drawable.focus_border_style);
			flg = false;
		} else {
			planetHolder.txtNickName
					.setBackgroundResource(R.drawable.edit_text_border_style);
		}
		return flg;
	}

	/**
	 * validating emails id
	 * 
	 * @param email
	 * @return
	 */
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * validating password and user id not null
	 * 
	 * @param val
	 * @return false with val null else true
	 */
	private boolean isValidPassword(String val) {
		if (val != null && val.trim().length() > 0) {
			return true;
		}
		return false;
	}

	private boolean isNullOrEmpty(String val) {
		if (val != null && val.trim().length() > 0) {
			return true;
		}
		return false;
	}

	/* *********************************
	 * We use the holder pattern It makes the view faster and avoid finding the
	 * component *********************************
	 */
	private static class PlanetHolder {

		public TextView txtValidation;
		public EditText txtId;
		public EditText txtPassword;
		public EditText txtEmailAddress;
		public EditText txtNickName;
		public Button btnOk;
	}

	private class SendUserRegisterAsyncTask extends
			AsyncTask<HashMap<String, String>, Void, String> {

		@SuppressWarnings("unused")
		@Override
		protected String doInBackground(HashMap<String, String>... params) {
			try {
				APISResult responseObject;
				String jsonString = Constants.BLANK_STRING;

				HashMap<String, String> data = params[0];
				String strLoginId = data.get("loginId");
				String strPassword = data.get("password");
				String strEmail = data.get("email");
				HashMap<String, Object> dataObj = new HashMap<String, Object>();

				dataObj.put("nickname", data.get("nickname"));
				dataObj.put("auto_login", true);
				APISResult responseObj = PlayerManager.getInstance()
						.registerUser(strLoginId, strPassword, strEmail,
								dataObj);
				if (responseObj != null) {
					Intent i = new Intent(SignUpActivity.this,
							IntroductionActivity.class);
					startActivity(i);

					return "Ok";
				}

			} catch (JSONException ex) {
				Log.d("input", ex.getMessage());
				if (ex.getMessage().contains("409")) {
					return "409";
				}
			} catch (APISException ex) {
				Log.d("input", ex.getMessage());
				if (ex.getMessage().contains("409")) {
					return "409";
				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressBar.dismiss();
			if (result.equals("409")) {
				planetHolder.txtValidation.setText(R.string.mesError409);
				planetHolder.txtValidation.setVisibility(View.VISIBLE);
			} else {
				planetHolder.txtValidation.setText(R.string.mesError);
				planetHolder.txtValidation.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar = new ProgressDialog(SignUpActivity.this);
			progressBar.setMessage("Loading....");
			progressBar.show();
			progressBar.setCancelable(false);
		}

	}
}
