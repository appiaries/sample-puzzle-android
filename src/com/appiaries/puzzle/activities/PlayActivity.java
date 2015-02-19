/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.appiaries.puzzle.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appiaries.puzzle.common.APIHelper;
import com.appiaries.puzzle.common.UIHelper;
import com.appiaries.puzzle.games.*;
import com.appiaries.puzzle.games.CountDownTextView.CountDownTimerListener;
import com.appiaries.puzzle.games.TileBoardView.TileBoardViewListener;
import com.appiaries.puzzle.jsonmodels.FirstComeRanking;
import com.appiaries.puzzle.jsonmodels.Stage;
import com.appiaries.puzzle.jsonmodels.TimeRanking;
import com.appiaries.puzzle.managers.FirstComeRankingManager;
import com.appiaries.puzzle.managers.FirstComeRankingSeqManager;
import com.appiaries.puzzle.managers.TimeRankingManager;

public class PlayActivity extends Activity implements CountDownTimerListener,
		TileBoardViewListener {
	
	private CountDownTextView countDownView;
	private String stageId = null;
	private String imageId = null;
	private final java.util.List<String> mActList = new java.util.ArrayList<String>();

	ProgressDialog progressBar;
	private boolean bStartCountDownFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_play);

		Intent intent = getIntent();
		Stage stageObj = (Stage) intent.getSerializableExtra("stage");
		imageId = stageObj.getImageId();
		stageId = APIHelper.getStringInLocalStorage(getApplicationContext(),
				"stage_id");
		getActionBar().setTitle(stageObj.getStageName());

		long countDownTime = stageObj.getTimeLimit();
		int numberOfWidthPieces = stageObj.getNumberOfHorizontalPieces();
		int numberOfHeightPieces = stageObj.getNumberOfVerticalPieces();

		// Get Timer Layout
		final RelativeLayout timerLayout = (RelativeLayout) findViewById(R.id.timer_layout);

		// Initial CountDownTextView
		countDownView = new CountDownTextView(getApplicationContext(),
				PlayActivity.this, countDownTime);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);

		// Add CountDown View into Main Layout
		timerLayout.addView(countDownView, params);

		DisplayMetrics dimension = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dimension);

		int screenWidth = dimension.widthPixels;

		// Sample Picture
		Bitmap picture = loadBitmap(imageId + ".jpg");

		final TileBoardView gameBoard = (TileBoardView) findViewById(R.id.gameboard);

		gameBoard.setBackgroundColor(Color.parseColor("#000000"));

		gameBoard.setTileBoardViewListener(PlayActivity.this);

		gameBoard.playWithImage(picture, numberOfWidthPieces,
				numberOfHeightPieces, screenWidth);

		gameBoard.shuffle();

		// Wait 3 seconds before the game begins
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// Start Count Down
				countDownView.startCountDown();
				bStartCountDownFlag = true;
			}
		}, 3000);
	}

	@Override
	public void onTimeUp() {
		DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		};

		UIHelper.showNotifyDialog(PlayActivity.this, "残念！時間切れです",
				dialogListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.play_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.sample_menu:
			if(bStartCountDownFlag){
				Intent intent = new Intent(this, SampleActivity.class);
				intent.putExtra("imageId", imageId);
				intent.putExtra("timeLimit", countDownView.getElaspedTime());
				startActivity(intent);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 
	 * @param stageId
	 * @param userId
	 * @param code
	 * @param time
	 */
	private void submitResultPlayer(String stageId, String userId,
			String nickName, int score) {

		HashMap<String, Object> dataObj = new HashMap<String, Object>();
		dataObj.put("stage_id", stageId);
		dataObj.put("user_id", userId);
		dataObj.put("nickname", nickName);
		dataObj.put("score", score);

		new SendResultGameRegisterAsynTask().execute(dataObj);
	}

	private class SendResultGameRegisterAsynTask extends
			AsyncTask<HashMap<String, Object>, Void, Void> {

		@SuppressWarnings("unused")
		@Override
		protected Void doInBackground(HashMap<String, Object>... params) {			
			HashMap<String, Object> data = params[0];
			String stageId = String.valueOf(data.get("stage_id"));
			String userId = APIHelper.getStringInLocalStorage(
					getApplicationContext(), "user_id");
			int score = (int) data.get("score");

			try {

				// check exist for insert to firstComeRanking collection
				if (FirstComeRankingManager.getInstance().getInformationList() != null) {
					Boolean insertFlag = true;
					List<FirstComeRanking> firstComeRankingList = FirstComeRankingManager
							.getInstance().getInformationList();
					for (FirstComeRanking firstComeObj : firstComeRankingList) {
						if (firstComeObj.getStageId().equals(stageId)
								&& firstComeObj.getUserId().equals(userId)) {
							insertFlag = false;
						}
					}

					// insert new record to firstComeRanking collection using
					// new sequence
					if (insertFlag) {
						int seqNo = FirstComeRankingSeqManager.getInstance()
								.addSequence(1L);
						data.put("rank", seqNo);

						FirstComeRankingManager.getInstance().registerJsonData(
								data);
					}
				}

				// check record exist in timeRanking collection if exist, update
				// new best score
				TimeRanking timeRanking = TimeRankingManager.getInstance()
						.getInformationList(stageId, userId);
				if (timeRanking.getId() != null) {
					if (score < timeRanking.getScore()) {
						TimeRankingManager.getInstance().updateJsonData(
								timeRanking.getId(), data);
					}
				} else {
					// if not exist, insert new record
					TimeRankingManager.getInstance().registJsonData(data);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar.dismiss();
			Intent i = new Intent(PlayActivity.this, GameResultActivity.class);
			i.putExtra("stageId", stageId);
			i.putExtra("score", countDownView.getSpentTime());
			startActivity(i);
			finish();
		}

		@Override
		protected void onPreExecute() {
			progressBar = new ProgressDialog(PlayActivity.this);
			progressBar.setMessage("結果を送信中です...");
			progressBar.show();
			progressBar.setCancelable(false);
		}

	}

	@Override
	public void onFinish() {		
		countDownView.stopCountDown();
		final int score = (int) countDownView.getSpentTime();
		Runnable mRunnable;
		Handler mHandler = new Handler();
		mRunnable = new Runnable() {
			@Override
			public void run() {
				submitResultPlayer(stageId, APIHelper.getStringInLocalStorage(
						getApplicationContext(), "user_id"),
						APIHelper.getStringInLocalStorage(
								getApplicationContext(), "nickname"), score);
			}
		};
		mHandler.postDelayed(mRunnable, 2000);

	}

	private Bitmap loadBitmap(String strImageName) {
		try {
			File f = getApplicationContext().getFileStreamPath(strImageName);

			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 1024;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Bitmap loadBitmap2(String strImageName) {
		try {
			Options o = new Options();
			o.inJustDecodeBounds = true;
			File filePath = getApplicationContext().getFileStreamPath(
					strImageName);
			FileInputStream fi = new FileInputStream(filePath);
			BitmapFactory.decodeStream(fi, null, o);

			int targetWidth = 600;
			int targetHeight = 600;

			if (o.outWidth > o.outHeight && targetWidth < targetHeight) {
				int i = targetWidth;
				targetWidth = targetHeight;
				targetHeight = i;
			}

			if (targetWidth < o.outWidth || targetHeight < o.outHeight) {
				double widthRatio = (double) targetWidth / (double) o.outWidth;
				double heightRatio = (double) targetHeight
						/ (double) o.outHeight;
				double ratio = Math.max(widthRatio, heightRatio);

				o.inSampleSize = (int) Math.pow(2,
						(int) Math.round(Math.log(ratio) / Math.log(0.5)));
			} else {
				o.inSampleSize = 1;
			}

			o.inScaled = false;
			o.inJustDecodeBounds = false;
			fi = new FileInputStream(filePath);
			Bitmap bitmap = BitmapFactory.decodeStream(fi, null, o);
			if (bitmap == null) {
				Toast.makeText(this, "Couldn't load image", Toast.LENGTH_LONG)
						.show();
			}

			int rotate = 0;

			if (rotate != 0) {
				Matrix matrix = new Matrix();
				matrix.postRotate(rotate);

				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), matrix, true);
			}

			return bitmap;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
