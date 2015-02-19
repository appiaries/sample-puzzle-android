/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.activities;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import com.appiaries.puzzle.R;
import com.appiaries.puzzle.common.APIHelper;
import com.appiaries.puzzle.jsonmodels.TimeRanking;
import com.appiaries.puzzle.jsonmodels.FirstComeRanking;
import com.appiaries.puzzle.managers.FirstComeRankingManager;
import com.appiaries.puzzle.managers.TimeRankingManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

/**
 * 
 * @author ntduc
 * 
 */
public class GameResultActivity extends Activity {

	private String userId = null;
	private String stageId = null;
	private String stageName = "";
	private Long newScore;
	
	
	PlanetHolder planetHolder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameresult);
		
		userId = APIHelper.getStringInLocalStorage(getApplicationContext(), "user_id");
		stageId = APIHelper.getStringInLocalStorage(getApplicationContext(), "stage_id");
		stageName  = APIHelper.getStringInLocalStorage(getApplicationContext(), "stage_name"); 
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		newScore = bundle.getLong("score");
		
		createView();
	}

	private void createView() {

		planetHolder = new PlanetHolder();
		
		LinearLayout formResultGame = (LinearLayout)findViewById(R.id.formResultGame);
		TextView txtStage = (TextView) findViewById(R.id.txtStage);
		TextView txtResultScore = (TextView) findViewById(R.id.txtResultScore);
		TextView txtTimeRanking = (TextView) findViewById(R.id.txtTimeRanking);
		TextView txtTotalCountTimeRanking = (TextView) findViewById(R.id.txtTotalCountTimeRanking);
		TextView txtFirsComeRanking = (TextView) findViewById(R.id.txtFirsComeRanking);
		TextView txtTotalCountFirsComeRanking = (TextView) findViewById(R.id.txtTotalCountFirsComeRanking);
		
		Button btnHome = (Button) findViewById(R.id.btn_home);
		Button btnRanking = (Button) findViewById(R.id.btn_ranking);

		planetHolder.formResultGame = formResultGame;
		planetHolder.txtStage = txtStage;
		planetHolder.txtStage.setText(stageName);
		
		planetHolder.txtResultScore = txtResultScore;
		planetHolder.txtResultScore.setText(String.valueOf(newScore));
		
		planetHolder.txtTimeRanking = txtTimeRanking;
		planetHolder.txtTotalCountTimeRanking = txtTotalCountTimeRanking;
		planetHolder.txtFirsComeRanking = txtFirsComeRanking;
		planetHolder.txtTotalCountFirsComeRanking = txtTotalCountFirsComeRanking;
		planetHolder.btnHome = btnHome;
		planetHolder.btnRanking = btnRanking;		
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("stageId", stageId);
		data.put("userId", userId);
		
		new SelectJsonDataAsynTask().execute(data);
		
		planetHolder.btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				backToTopScreen();
				
				
			}
		});

		planetHolder.btnRanking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(GameResultActivity.this,
					RankingActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		backToTopScreen();
	}
	
	private void backToTopScreen(){
		Intent i = new Intent(GameResultActivity.this,
				TopActivity.class);
		
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	
			
		startActivity(i);
		
		finish();
	}

	private class SelectJsonDataAsynTask extends AsyncTask<HashMap<String, String>, Void, HashMap<String, String>> {
	
		@Override
		protected HashMap<String, String> doInBackground(HashMap<String, String>... agr0) {
			HashMap<String, String> resultData = new HashMap<String, String>();
			try {
				HashMap<String, String> data = agr0[0];
				String strStageId = data.get("stageId");
				String strUserId = data.get("userId");
				
				//get all timeRanking list
				List<TimeRanking> listTimeRanking = TimeRankingManager.getInstance()
						.getInformationList();
				
				int timeRank = 0;
				for (TimeRanking timeRanking : listTimeRanking) {
					timeRank ++;
					if (timeRanking.getStageId().equals(strStageId) && timeRanking.getUserId().equals(strUserId)){
						break;
					}					
				}
				
				List<FirstComeRanking> listFirstComeRanking = FirstComeRankingManager.getInstance()
						.getInformationList();
				int score = 0;
				int firstComeRank = 0;
				for (FirstComeRanking firstComeRanking : listFirstComeRanking) {
					firstComeRank ++;
					if (firstComeRanking.getStageId().equals(strStageId) && firstComeRanking.getUserId().equals(strUserId)){
						score = firstComeRanking.getScore();
						break;
					}					
				}
				
				resultData.put("score",String.valueOf(score));
				resultData.put("timeRank",String.valueOf(timeRank));
				resultData.put("totalCountTimeRanking",String.valueOf(listTimeRanking.size()));
				resultData.put("firstComeRank",String.valueOf(firstComeRank));
				resultData.put("totalCountFirstComeRanking",String.valueOf(listFirstComeRanking.size()));
								
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			return resultData;
		}

		@Override
		protected void onPostExecute(HashMap<String, String> result) {						
			planetHolder.txtTimeRanking.setText(result.get("timeRank"));
			planetHolder.txtTotalCountTimeRanking.setText(result.get("totalCountTimeRanking"));
			planetHolder.txtFirsComeRanking.setText(result.get("firstComeRank"));
			planetHolder.txtTotalCountFirsComeRanking.setText(result.get("totalCountFirstComeRanking"));
			planetHolder.formResultGame.setVisibility(View.VISIBLE);
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
		public LinearLayout formResultGame;
		public TextView txtStage;
		public TextView txtResultScore;
		public TextView txtTimeRanking;
		public TextView txtTotalCountTimeRanking;
		public TextView txtFirsComeRanking;
		public TextView txtTotalCountFirsComeRanking;
		public Button btnHome;
		public Button btnRanking;
	}
}
