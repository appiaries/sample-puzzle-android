/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.activities;

import java.util.List;

import org.json.JSONException;

import com.appiaries.APISException;
import com.appiaries.puzzle.R;
import com.appiaries.puzzle.adapters.StageAdapter;
import com.appiaries.puzzle.adapters.TimeRankingAdapter;
import com.appiaries.puzzle.adapters.FirstComeRankingAdapter;
import com.appiaries.puzzle.jsonmodels.FirstComeRanking;
import com.appiaries.puzzle.jsonmodels.Stage;
import com.appiaries.puzzle.jsonmodels.TimeRanking;
import com.appiaries.puzzle.managers.FirstComeRankingManager;
import com.appiaries.puzzle.managers.FirstComeRankingSeqManager;
import com.appiaries.puzzle.managers.StageManager;
import com.appiaries.puzzle.managers.TimeRankingManager;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.widget.TabHost.TabContentFactory;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * 
 * @author ntduc
 * 
 */
@SuppressWarnings("deprecation")
public class RankingActivity extends TabActivity implements OnTabChangeListener {
	TabHost tabHost;
	PlanetHolder planetHolder;
	FirstComeRankingAdapter firstComeAdapter;
	TimeRankingAdapter timeAdapter;
	ProgressDialog progressBar;
	private StageAdapter stageAdapter = null;
	String selectedStageId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		createView();
		createTab();
	}

	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	//		MenuInflater inflater = getMenuInflater();
	//		inflater.inflate(R.menu.ranking_menu, menu);
	//		return super.onCreateOptionsMenu(menu);
	//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.reset_menu:
			resetData();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void createView() {
		planetHolder = new PlanetHolder();

		// Find components in View
		planetHolder.spinnerStages = (Spinner) findViewById(R.id.spinner_stages);
		LinearLayout resultSearch = (LinearLayout) findViewById(R.id.resultSearch);
		ListView listView1 = (ListView) findViewById(R.id.list1);
		ListView listView2 = (ListView) findViewById(R.id.list2);

		planetHolder.resultSearch = resultSearch;
		planetHolder.listView1 = listView1;
		planetHolder.listView2 = listView2;

		new LoadCollectionStagesAsynTask().execute();
	}

	/* *********************************
	 * We use the holder pattern It makes the view faster and avoid finding the
	 * component *********************************
	 */
	private static class PlanetHolder {
		public LinearLayout resultSearch;
		public Spinner spinnerStages;
		public ListView listView1;
		public ListView listView2;
	}

	private void createTab() {
		// Get TabHost Refference
		tabHost = getTabHost();
		// Set TabChangeListener called when tab changed
		tabHost.setOnTabChangedListener(this);
		// add views to tab host
		tabHost.addTab(tabHost.newTabSpec("First").setIndicator("")
				.setContent(new TabContentFactory() {
					public View createTabContent(String arg0) {
						return planetHolder.listView1;
					}
				}));
		tabHost.addTab(tabHost.newTabSpec("Second").setIndicator("")
				.setContent(new TabContentFactory() {
					public View createTabContent(String arg0) {
						return planetHolder.listView2;
					}
				}));
		tabHost.getTabWidget().getChildAt(1)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						planetHolder.listView2.setAdapter(firstComeAdapter);
						tabHost.setCurrentTab(1);
					}
				});

		TextView tv = (TextView) tabHost.getTabWidget().getChildAt(0)
				.findViewById(android.R.id.title);
		tv.setText(R.string.tab_Time);

		TextView tv2 = (TextView) tabHost.getTabWidget().getChildAt(1)
				.findViewById(android.R.id.title);
		tv2.setText(R.string.tab_FirstCome);

		tabHost.getTabWidget().setCurrentTab(0);
		tv.setText("時間");
	}

	@Override
	public void onTabChanged(String tabId) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			if (i == 0) {
				((TextView) tabHost.getTabWidget().getChildAt(i)
						.findViewById(android.R.id.title))
						.setText(R.string.tab_Time);
			} else if (i == 1) {
				((TextView) tabHost.getTabWidget().getChildAt(i)
						.findViewById(android.R.id.title))
						.setText(R.string.tab_FirstCome);
			}
		}

		if (tabHost.getCurrentTab() == 0) {

			String title = "時間";

			if (timeAdapter != null && timeAdapter.getCount() > 0) {
				title = String.format("時間 (全%d人中)", timeAdapter.getCount());
			}

			((TextView) tabHost.getTabWidget().getChildAt(0)
					.findViewById(android.R.id.title)).setText(title);
		} else if (tabHost.getCurrentTab() == 1) {

			String title = "先着";

			if (firstComeAdapter != null && firstComeAdapter.getCount() > 0) {
				title = String
						.format("先着 (全%d人中)", firstComeAdapter.getCount());
			}

			((TextView) tabHost.getTabWidget().getChildAt(1)
					.findViewById(android.R.id.title)).setText(title);

		}

	}

	private class LoadCollectionStagesAsynTask extends
			AsyncTask<Void, Void, List<Stage>> {

		@Override
		protected List<Stage> doInBackground(Void... params) {
			try {
				List<Stage> list = StageManager.getInstance().getStageList();
				return list;
			} catch (JSONException e) {
				Log.d("Error get List Stages", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Stage> result) {
			if (result != null) {
				stageAdapter = new StageAdapter(RankingActivity.this,
						android.R.layout.simple_spinner_item, result);

				planetHolder.spinnerStages.setAdapter(stageAdapter);
				planetHolder.spinnerStages.setSelection(0);

				planetHolder.spinnerStages
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								progressBar = new ProgressDialog(
										RankingActivity.this);
								progressBar
										.setMessage("Loading score list....");
								progressBar.show();

							 selectedStageId = stageAdapter.getItem(
										position).getId();

								Log.d("Selected Stage ID:", selectedStageId);

								new LoadTimeRankingAsyncTask()
										.execute(selectedStageId);

								new LoadFirstComeRankingAsyncTask()
										.execute(selectedStageId);

								planetHolder.resultSearch
										.setVisibility(View.VISIBLE);
							}

							public void onNothingSelected(AdapterView<?> parent) {

							}
						});
			}
		}

		@Override
		protected void onPreExecute() {
		}

	}

	private class ResetRankingCollectionGamePlayerAsynTask extends
			AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int responseCode = 0;
			try {
				int responseCodeS = FirstComeRankingSeqManager.getInstance()
						.deleteAll();
				if (responseCodeS == 200 || responseCodeS == 201
						|| responseCodeS == 204) {
					responseCode = FirstComeRankingManager.getInstance()
							.deleteAll();
				}

			} catch (APISException apisEx) {				
				Log.d("RainkingActivity : ", apisEx.getMessage());
			} catch(JSONException jsonEx){
				jsonEx.printStackTrace();
			}

			return responseCode;
		}

		@Override
		protected void onPostExecute(Integer responseCode) {
			// Update two list view
			if (responseCode == 200 || responseCode == 201
					|| responseCode == 204) {
				planetHolder.spinnerStages.setSelection(0);
				new LoadFirstComeRankingAsyncTask().execute(selectedStageId);
			}
			
		}

		@Override
		protected void onPreExecute() {
		}

	}

	private class LoadTimeRankingAsyncTask extends
			AsyncTask<String, Void, List<TimeRanking>> {

		@Override
		protected List<TimeRanking> doInBackground(String... arg0) {
			try {
				return TimeRankingManager.getInstance().getTimeRankingList(
						arg0[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		//	return new ArrayList<TimeRanking>();
			return null;
		}

		@Override
		protected void onPostExecute(List<TimeRanking> results) {
			progressBar.dismiss();
			if (results.size() > 0) {

				Log.d("Results: ",
						Integer.toString(results != null ? results.size() : 0));

				timeAdapter = new TimeRankingAdapter(getApplicationContext(),
						results);

				planetHolder.listView1.setAdapter(timeAdapter);

				if (tabHost.getCurrentTab() == 0) {
					// Update Tab title
					String title = String.format("時間 (全%d人中)", results.size());

					((TextView) tabHost.getTabWidget().getChildAt(0)
							.findViewById(android.R.id.title)).setText(title);
				}
			} else {

				((TextView) tabHost.getTabWidget().getChildAt(0)
						.findViewById(android.R.id.title)).setText("時間");

				if (timeAdapter != null) {
					timeAdapter.refreshAdapter(results);
					if (tabHost.getCurrentTab() == 0) {
						planetHolder.listView1.setAdapter(timeAdapter);
					}
				}
			}
		}
	}

	private class LoadFirstComeRankingAsyncTask extends
			AsyncTask<String, Void, List<FirstComeRanking>> {
		@Override
		protected List<FirstComeRanking> doInBackground(String... arg0) {
			try {
				return FirstComeRankingManager.getInstance()
						.getInformationList(arg0[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<FirstComeRanking> results) {
			progressBar.dismiss();
			if (results.size() > 0) {

				Log.d("Results: ",
						Integer.toString(results != null ? results.size() : 0));

				firstComeAdapter = new FirstComeRankingAdapter(
						getApplicationContext(), results);
				if (tabHost.getCurrentTab() == 1) {
					planetHolder.listView2.setAdapter(firstComeAdapter);
				}

				if (tabHost.getCurrentTab() == 1) {
					// Update Tab title
					String title = String.format("先着 (全%d人中)", results.size());

					((TextView) tabHost.getTabWidget().getChildAt(1)
							.findViewById(android.R.id.title)).setText(title);
				}

			} else {

				((TextView) tabHost.getTabWidget().getChildAt(1)
						.findViewById(android.R.id.title)).setText("先着");

				if (firstComeAdapter != null) {
					firstComeAdapter.refreshAdapter(results);
					if (tabHost.getCurrentTab() == 1) {
						planetHolder.listView2.setAdapter(firstComeAdapter);
					}
				}
			}
		}
	}

	public void resetData() {
		Builder builder = new AlertDialog.Builder(RankingActivity.this);

		// customize and set title and message content
		TextView customTitle = new TextView(RankingActivity.this);

		customTitle.setText("確認");
		customTitle.setPadding(10, 10, 10, 10);
		customTitle.setGravity(Gravity.CENTER);
		customTitle.setTextSize(20);

		builder.setCustomTitle(customTitle);

		TextView customMessage = new TextView(RankingActivity.this);

		customMessage.setPadding(10, 40, 10, 40);
		customMessage.setText("先着ランキングをリセットしますか。");
		customMessage.setGravity(Gravity.CENTER_HORIZONTAL);
		customMessage.setTextSize(20);

		builder.setView(customMessage);

		// handle cancel button click
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				new ResetRankingCollectionGamePlayerAsynTask().execute();
			}
		});

		builder.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.show();
	}
}
