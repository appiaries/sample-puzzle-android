/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.activities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appiaries.puzzle.R;
import android.app.Activity;
import android.os.Bundle;
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
public class PasswordReminderActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_reminder);
		CreateView();
	}

	private void CreateView() {

		final PlanetHolder planetHolder = new PlanetHolder();

		EditText txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);

		Button btnSend = (Button) findViewById(R.id.btnSend);

		planetHolder.txtEmailAddress = txtEmailAddress;
		TextView txtValidation = (TextView) findViewById(R.id.validation);

		planetHolder.txtValidation = txtValidation;
		planetHolder.btnSend = btnSend;

		planetHolder.btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (validationFrom(planetHolder)) {
					
				} else {
					planetHolder.txtValidation.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	private Boolean validationFrom(PlanetHolder planetHolder) {
		final String emailAddress = planetHolder.txtEmailAddress.getText()
				.toString();
		if (!isValidPassword(emailAddress) || !isValidEmail(emailAddress)) {
			return false;
		}
		return true;
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

	/**
	 * validating email id
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

	/* *********************************
	 * We use the holder pattern It makes the view faster and avoid finding the
	 * component *********************************
	 */
	private static class PlanetHolder {

		public EditText txtEmailAddress;
		public TextView txtValidation;
		public Button btnSend;
	}
}
