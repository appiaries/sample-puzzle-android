/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.appiaries.puzzle.R;
import com.appiaries.puzzle.jsonmodels.Introduction;
import com.appiaries.puzzle.managers.IntroductionManager;

public class IntroductionActivity extends Activity {

	List<Introduction> introductionList = new ArrayList<Introduction>();
	Introduction currentIntroduction = new Introduction();
	int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introduction);
		CreateView();
	}

	private void CreateView() {

		final PlanetHolder planetHolder = new PlanetHolder();

		Button btnSkip = (Button) findViewById(R.id.btn_skip);
		Button btnNext = (Button) findViewById(R.id.btn_next);
		Button btnPre = (Button) findViewById(R.id.btn_pre);

		planetHolder.btnSkip = btnSkip;
		planetHolder.btnNext = btnNext;
		planetHolder.btnPre = btnPre;

		new SelectJsonDataAsyncTask().execute();

		planetHolder.btnSkip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(IntroductionActivity.this,
						LoginActivity.class);
				startActivity(i);
			}
		});

		planetHolder.btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (introductionList != null && introductionList.size() > 0) {
					if (index < introductionList.size() - 1) {
						index++;
						TextView textStage = (TextView) findViewById(R.id.text_ContentInformation);
						currentIntroduction = introductionList.get(index);
						textStage.setText(currentIntroduction.getContent());
			
						planetHolder.btnPre.setVisibility(View.VISIBLE);
						if (index == introductionList.size() - 1) {
							planetHolder.btnNext.setVisibility(View.INVISIBLE);
						}
					}
				}
			}
		});

		planetHolder.btnPre.setVisibility(View.INVISIBLE);
		planetHolder.btnPre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (introductionList != null && introductionList.size() > 0) {
					if (index > 0) {
						index--;
						TextView textStage = (TextView) findViewById(R.id.text_ContentInformation);
						currentIntroduction = introductionList.get(index);
						textStage.setText(currentIntroduction.getContent());
						
						planetHolder.btnNext.setVisibility(View.VISIBLE);
						if (index == 0) {
							planetHolder.btnPre.setVisibility(View.INVISIBLE);
						}
					}
				}
			}
		});
	}

	private class SelectJsonDataAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... r) {
			try {
				introductionList = IntroductionManager.getInstance()
						.getIntroductionList();
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {		
			TextView textStage = (TextView) findViewById(R.id.text_ContentInformation);
			currentIntroduction = introductionList.get(index);
			textStage.setText(currentIntroduction.getContent());
		}

		@Override
		protected void onPreExecute() {		

		}
	}

	/* *********************************
	 * We use the holder pattern It makes the view faster and avoid finding the
	 * component *********************************
	 */
	private static class PlanetHolder {
		public Button btnSkip;
		public Button btnNext;
		public Button btnPre;
	}
}