/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.activities;

import java.util.HashMap;

import org.json.JSONException;

import com.appiaries.puzzle.R;
import com.appiaries.puzzle.common.APIHelper;
import com.appiaries.puzzle.jsonmodels.Player;
import com.appiaries.puzzle.managers.PlayerManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * 
 * @author ntduc
 * 
 */
@SuppressWarnings("unchecked")
public class LoginActivity extends Activity {
	PlanetHolder planetHolder;
	ProgressDialog progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		CreateView();
	}

	private void CreateView() {

		planetHolder = new PlanetHolder();

		EditText txtId = (EditText) findViewById(R.id.txtId);
		EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
		TextView txtValidation = (TextView) findViewById(R.id.validation);

		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		Button btnSignUp = (Button) findViewById(R.id.btnSignUp);

		planetHolder.txtValidation = txtValidation;
		planetHolder.txtId = txtId;
		planetHolder.txtPassword = txtPassword;
		planetHolder.btnLogin = btnLogin;
		planetHolder.btnSignUp = btnSignUp;

		planetHolder.btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (validationFrom(planetHolder)) {
					String loginId = planetHolder.txtId.getText().toString();
					String password = planetHolder.txtPassword.getText()
							.toString();
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("loginId", loginId);
					data.put("password", password);

					new SendUserLoginAsynTask().execute(data);

				} else {
					planetHolder.txtValidation.setVisibility(View.VISIBLE);
				}
			}
		});

		planetHolder.btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View rootView) {

				Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(i);
			}
		});

	}

	/**
	 * 
	 * @param planetHolder
	 * @return
	 */
	private boolean validationFrom(final PlanetHolder planetHolder) {
		boolean flg = true;
		final String txtId = planetHolder.txtId.getText().toString();
		if (!isValidPassword(txtId) || txtId.trim().length() < 3) {
			planetHolder.txtId
					.setBackgroundResource(R.drawable.focus_border_style);
			flg = false;
		} else {
			planetHolder.txtId
					.setBackgroundResource(R.drawable.edit_text_border_style);
		}

		final String pass = planetHolder.txtPassword.getText().toString();
		if (!isValidPassword(pass) || pass.trim().length() < 6) {
			planetHolder.txtPassword
					.setBackgroundResource(R.drawable.focus_border_style);
			flg = false;
		} else {
			planetHolder.txtPassword
					.setBackgroundResource(R.drawable.edit_text_border_style);
		}
		return flg;
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

	/* *********************************
	 * We use the holder pattern It makes the view faster and avoid finding the
	 * component *********************************
	 */
	private static class PlanetHolder {

		public TextView txtValidation;
		public EditText txtId;
		public EditText txtPassword;
		public TextView txtPasswordReminder;
		public Button btnLogin;
		public Button btnSignUp;
	}

	private class SendUserLoginAsynTask extends
			AsyncTask<HashMap<String, String>, Void, String> {

		@Override
		protected String doInBackground(HashMap<String, String>... params) {
			HashMap<String, String> data = params[0];
			String loginId = data.get("loginId");
			String password = data.get("password");
			try {
				return PlayerManager.getInstance().doLogin(loginId, password,
						true, getApplicationContext());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			
			if (result != null && !result.equals("")) {
				APIHelper.setStringToLocalStorage(getApplicationContext(),
						"user_id", result);
				new getPlayerInfoAsynTask().execute();
			} else {
				progressBar.dismiss();
				planetHolder.txtValidation.setVisibility(View.VISIBLE);
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar = new ProgressDialog(LoginActivity.this);
			progressBar.setMessage("Loading....");
			progressBar.show();
			progressBar.setCancelable(false);
		}
	}

	private class getPlayerInfoAsynTask extends AsyncTask<Void, Void, Player> {
		@Override
		protected Player doInBackground(Void... params) {
			try {
				return PlayerManager.getInstance().getInformationList();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Player result) {
			super.onPostExecute(result);
			progressBar.dismiss();
			if (result != null) {
				APIHelper.setStringToLocalStorage(getApplicationContext(),
						"nickname", result.getNickname());
				Intent i = new Intent(LoginActivity.this, TopActivity.class);
				startActivity(i);
				finish();
			}
		}

	}
}
