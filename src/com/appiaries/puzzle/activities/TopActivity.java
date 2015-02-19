/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.activities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import com.appiaries.puzzle.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appiaries.puzzle.common.APIHelper;
import com.appiaries.puzzle.jsonmodels.Stage;
import com.appiaries.puzzle.managers.StageManager;

/**
 * 
 * @author ntduc
 * 
 */
public class TopActivity extends Activity {

	private List<Stage> stageList = new ArrayList<Stage>();
	private int index = 0;	
	private PlanetHolder planetHolder;
	ProgressDialog progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);

		CreateView();
	}

	private void CreateView() {

		planetHolder = new PlanetHolder();
		RelativeLayout btnGroupStage = (RelativeLayout) findViewById(R.id.btnGroupStage);
		LinearLayout btnGroupSubmit = (LinearLayout) findViewById(R.id.btnGroupSubmit);
		TextView txtStage = (TextView) findViewById(R.id.text_stage);
		// TextView txtImageId = (TextView) findViewById(R.id.txtImageId);
		Button btnStart = (Button) findViewById(R.id.btn_start);
		Button btnRanking = (Button) findViewById(R.id.btn_ranking);
		Button btnNext = (Button) findViewById(R.id.btn_next);
		Button btnPre = (Button) findViewById(R.id.btn_pre);

		// planetHolder.txtImageId = txtImageId;
		planetHolder.btnGroupStage = btnGroupStage;
		planetHolder.btnGroupSubmit = btnGroupSubmit;
		planetHolder.txtStage = txtStage;
		planetHolder.txtStage.setTextColor(Color.parseColor("#000000"));
		planetHolder.btnStart = btnStart;
		planetHolder.btnRanking = btnRanking;
		planetHolder.btnNext = btnNext;
		planetHolder.btnPre = btnPre;
		
		getActionBar().setTitle("アプリタイトル");

		// get stage list and download image to local storage
		new getStageListFromServer().execute();

		planetHolder.btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Stage stageObj = stageList.get(index);
				APIHelper.setStringToLocalStorage(getApplicationContext(),
						"stage_id", stageObj.getId().toString());
				APIHelper.setStringToLocalStorage(getApplicationContext(),
						"stage_name", stageObj.getStageName().toString());
				Intent intent = new Intent(TopActivity.this, PlayActivity.class);
				intent.putExtra("stage", stageObj);
				startActivity(intent);
			}
		});

		planetHolder.btnRanking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(TopActivity.this, RankingActivity.class);
				startActivity(i);
			}
		});

		planetHolder.btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (index < stageList.size() - 1) {
					index++;
					planetHolder.txtStage.setText(stageList.get(index)
							.getStageName());					

					planetHolder.btnPre.setVisibility(View.VISIBLE);
					if (index == stageList.size() - 1) {
						planetHolder.btnNext.setVisibility(View.INVISIBLE);
					}
				}

			}
		});

		planetHolder.btnPre.setVisibility(View.INVISIBLE);
		planetHolder.btnPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (index > 0) {
					index--;
					planetHolder.txtStage.setText(stageList.get(index)
							.getStageName());				

					planetHolder.btnNext.setVisibility(View.VISIBLE);
					if (index == 0) {
						planetHolder.btnPre.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
	}

	/* *********************************
	 * We use the holder pattern It makes the view faster and avoid finding the
	 * component *********************************
	 */
	private static class PlanetHolder {
		public RelativeLayout btnGroupStage;
		public LinearLayout btnGroupSubmit;
		public TextView txtStage;
		public Button btnStart;
		public Button btnRanking;
		public Button btnNext;
		public Button btnPre;
	}

	// Asyntask to get stage list from server
	private class getStageListFromServer extends
			AsyncTask<Void, Void, List<Stage>> {

		@Override
		protected List<Stage> doInBackground(Void... r) {
			try {
				return StageManager.getInstance().getStageList();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Stage> result) {
			if (result != null) {
				stageList = result;
				TextView textStage = (TextView) findViewById(R.id.text_stage);
				textStage.setText(stageList.get(index).getStageName());

				planetHolder.btnGroupStage.setVisibility(View.VISIBLE);
				planetHolder.btnGroupSubmit.setVisibility(View.VISIBLE);

				// saving all image from sever to local storage
				new saveImageListFromServer().execute(stageList);
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar = new ProgressDialog(TopActivity.this);
			progressBar.setMessage("Loading....");
			progressBar.show();
			progressBar.setCancelable(false);
		}

	}

	private class saveImageListFromServer extends
			AsyncTask<List<Stage>, Void, Void> {

		@Override
		protected Void doInBackground(List<Stage>... params) {

			for (Stage s : params[0]) {
				try {
					// building string image
					String strImageId = s.getImageId();
					String strImageName = strImageId + ".jpg";
					String strImageURL = APIHelper
							.getImageFileUrlWithObjectId(strImageId);

					File fileEvents = new File(getApplicationContext()
							.getFilesDir().getAbsolutePath()
							+ "/"
							+ strImageName);

					if (!fileEvents.exists()) {						
						URL url = new URL(strImageURL);
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						connection.setDoInput(true);
						connection.connect();

						InputStream is = connection.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(is);
						ByteArrayBuffer baf = new ByteArrayBuffer(50);
						int current = 0;
						while ((current = bis.read()) != -1) {
							baf.append((byte) current);
						}

						FileOutputStream fos = openFileOutput(strImageName,
								Context.MODE_PRIVATE);

						fos.write(baf.toByteArray());
						fos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {		
			super.onPostExecute(result);
			progressBar.dismiss();
		}
	}

	/* *********************************
	 * If button back in the Top screen, the exit application
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something on back.
			finish();
			moveTaskToBack(true);
		}

		return super.onKeyDown(keyCode, event);
	}

}
