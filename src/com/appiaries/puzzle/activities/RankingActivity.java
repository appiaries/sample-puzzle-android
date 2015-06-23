//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.activities;

import com.appiaries.baas.sdk.AB;
import com.appiaries.baas.sdk.ABException;
import com.appiaries.baas.sdk.ABQuery;
import com.appiaries.baas.sdk.ABResult;
import com.appiaries.baas.sdk.ABStatus;
import com.appiaries.baas.sdk.ResultCallback;
import com.appiaries.puzzle.R;
import com.appiaries.puzzle.adapters.StageAdapter;
import com.appiaries.puzzle.adapters.TimeRankingAdapter;
import com.appiaries.puzzle.adapters.FirstComeRankingAdapter;
import com.appiaries.puzzle.models.FirstComeRanking;
import com.appiaries.puzzle.models.FirstComeRankingSequence;
import com.appiaries.puzzle.models.Stage;
import com.appiaries.puzzle.models.TimeRanking;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.TabHost.TabContentFactory;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
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

import java.util.List;

public class RankingActivity extends BaseTabActivity implements OnTabChangeListener {

    private static class ViewHolder {
        public LinearLayout resultSearch;
        public Spinner spinnerStages;
        public ListView listView0;
        public ListView listView1;
        public TextView tabTitle0;
        public TextView tabTitle1;
    }

    ViewHolder mViewHolder;

    TabHost mTabHost;
    FirstComeRankingAdapter mFirstComeAdapter;
    TimeRankingAdapter mTimeAdapter;
    private StageAdapter mStageAdapter = null;
    String mSelectedStageId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();

        // --------------------------------
        //  Find All Stages
        // --------------------------------
        final ProgressDialog progress = createAndShowProgressDialog(R.string.progress__loading);
        ABQuery query = Stage.query().orderBy(Stage.Field.ORDER, ABQuery.SortDirection.ASC);
        AB.DBService.findWithQuery(query, new ResultCallback<List<Stage>>() {
            @Override
            public void done(ABResult<List<Stage>> result, ABException e) {
                progress.dismiss();
                if (e == null) {
                    int code = result.getCode();
                    if (code == ABStatus.OK) {
                        List<Stage> stages = result.getData();
                        mStageAdapter = new StageAdapter(RankingActivity.this, android.R.layout.simple_spinner_item, stages);
                        mViewHolder.spinnerStages.setAdapter(mStageAdapter);
                        mViewHolder.spinnerStages.setSelection(0);
                    } else {
                        showUnexpectedStatusCodeError(RankingActivity.this, code);
                    }
                } else {
                    showError(RankingActivity.this, e);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.reset_menu:
                clearFirstComeRanking();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        if (mTabHost.getCurrentTab() == 0) {
            mViewHolder.tabTitle0.setText(R.string.ranking__tab0);

            String title;
            if (mTimeAdapter != null && mTimeAdapter.getCount() > 0) {
                title = String.format(getString(R.string.ranking__tab0_with_total), mTimeAdapter.getCount());
            } else {
                title = getString(R.string.ranking__tab0);
            }
            mViewHolder.tabTitle0.setText(title);

        } else if (mTabHost.getCurrentTab() == 1) {
            mViewHolder.tabTitle1.setText(R.string.ranking__tab1);

            String title;
            if (mFirstComeAdapter != null && mFirstComeAdapter.getCount() > 0) {
                title = String.format(getString(R.string.ranking__tab1_with_total), mFirstComeAdapter.getCount());
            } else {
                title = getString(R.string.ranking__tab1);
            }
            mViewHolder.tabTitle1.setText(title);
        }
    }

    private void setupView() {
        setContentView(R.layout.activity_ranking);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mViewHolder = new ViewHolder();
        mViewHolder.spinnerStages = (Spinner) findViewById(R.id.spinner_stages);
        mViewHolder.spinnerStages.setOnItemSelectedListener(mItemSelectedListener);
        mViewHolder.resultSearch = (LinearLayout) findViewById(R.id.layout_search_result);
        mViewHolder.listView0 = (ListView) findViewById(R.id.list1);
        mViewHolder.listView1 = (ListView) findViewById(R.id.list2);


        mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("First").setIndicator("").setContent(new TabContentFactory() {
            public View createTabContent(String arg0) {
                return mViewHolder.listView0;
            }
        }));
        mTabHost.addTab(mTabHost.newTabSpec("Second").setIndicator("").setContent(new TabContentFactory() {
            public View createTabContent(String arg0) {
                return mViewHolder.listView1;
            }
        }));
        mTabHost.getTabWidget().getChildAt(1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.listView1.setAdapter(mFirstComeAdapter);
                mTabHost.setCurrentTab(1);
            }
        });

        mViewHolder.tabTitle0 = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        mViewHolder.tabTitle0.setText(R.string.ranking__tab0);

        mViewHolder.tabTitle1 = (TextView) mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        mViewHolder.tabTitle1.setText(R.string.ranking__tab1);

        mTabHost.setOnTabChangedListener(this);
        mTabHost.getTabWidget().setCurrentTab(0);
    }

    private OnItemSelectedListener mItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            mSelectedStageId = mStageAdapter.getItem(position).getID();

            // --------------------------------
            //  Find TimeRanking (TOP 10)
            // --------------------------------
            ABQuery allTimeRankQuery = TimeRanking.query()
                    .where(TimeRanking.Field.STAGE_ID).equalsTo(mSelectedStageId)
                    .orderBy(TimeRanking.Field.SCORE, ABQuery.SortDirection.ASC)
                    .limit(10);

            final ProgressDialog progress = createAndShowProgressDialog(R.string.progress__loading);
            AB.DBService.findWithQuery(allTimeRankQuery, new ResultCallback<List<TimeRanking>>() {

                @Override
                public void done(ABResult<List<TimeRanking>> allTimeRankResult, ABException e) {
                    if (e == null) {
                        int allTimeRankResultCode = allTimeRankResult.getCode();
                        if (allTimeRankResultCode == ABStatus.OK) {

                            List<TimeRanking> timeRankingList = allTimeRankResult.getData();
                            if (timeRankingList.size() > 0) {

                                mTimeAdapter = new TimeRankingAdapter(getApplicationContext(), timeRankingList);
                                mViewHolder.listView0.setAdapter(mTimeAdapter);

                                if (mTabHost.getCurrentTab() == 0) {
                                    // Update Tab title
                                    String title = String.format(getString(R.string.ranking__tab0_with_total), timeRankingList.size());
                                    mViewHolder.tabTitle0.setText(title);
                                }

                            } else {

                                mViewHolder.tabTitle0.setText(R.string.ranking__tab0);

                                if (mTimeAdapter != null) {
                                    mTimeAdapter.refreshAdapter(timeRankingList);
                                    if (mTabHost.getCurrentTab() == 0) {
                                        mViewHolder.listView0.setAdapter(mTimeAdapter);
                                    }
                                }
                            }

                            // --------------------------------
                            //  Find FirstComeRanking (TOP 10)
                            // --------------------------------
                            ABQuery allFirstComeRankQuery = FirstComeRanking.query()
                                    .where(FirstComeRanking.Field.STAGE_ID).equalsTo(mSelectedStageId)
                                    .orderBy(FirstComeRanking.Field.CREATED, ABQuery.SortDirection.ASC)
                                    .limit(10);

                            AB.DBService.findWithQuery(allFirstComeRankQuery, new ResultCallback<List<FirstComeRanking>>() {
                                @Override
                                public void done(ABResult<List<FirstComeRanking>> allFirstRankResult, ABException e) {
                                    progress.dismiss();
                                    if (e == null) {
                                        int allFirstRankResultCode = allFirstRankResult.getCode();
                                        if (allFirstRankResultCode == ABStatus.OK) {

                                            List<FirstComeRanking> firstComeRankingList = allFirstRankResult.getData();
                                            if (firstComeRankingList.size() > 0) {

                                                mFirstComeAdapter = new FirstComeRankingAdapter(getApplicationContext(), firstComeRankingList);
                                                if (mTabHost.getCurrentTab() == 1) {
                                                    mViewHolder.listView1.setAdapter(mFirstComeAdapter);
                                                }

                                                if (mTabHost.getCurrentTab() == 1) {
                                                    // Update tab title
                                                    String title = String.format(getString(R.string.ranking__tab1_with_total), firstComeRankingList.size());
                                                    mViewHolder.tabTitle1.setText(title);
                                                }

                                            } else {

                                                mViewHolder.tabTitle1.setText(R.string.ranking__tab1);

                                                if (mFirstComeAdapter != null) {
                                                    mFirstComeAdapter.refreshAdapter(firstComeRankingList);
                                                    if (mTabHost.getCurrentTab() == 1) {
                                                        mViewHolder.listView1.setAdapter(mFirstComeAdapter);
                                                    }
                                                }
                                            }


                                        } else {
                                            showUnexpectedStatusCodeError(RankingActivity.this, allFirstRankResultCode);
                                        }
                                    } else {
                                        showError(RankingActivity.this, e);
                                    }
                                }
                            });
                        } else {
                            progress.dismiss();
                            showUnexpectedStatusCodeError(RankingActivity.this, allTimeRankResultCode);
                        }
                    } else {
                        progress.dismiss();
                        showError(RankingActivity.this, e);
                    }
                }
            });

            mViewHolder.resultSearch.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            /* NOP */
        }
    };

    private void clearFirstComeRanking() {
        Builder builder = new AlertDialog.Builder(RankingActivity.this);
        // Custom title
        TextView customTitle = new TextView(RankingActivity.this);
        customTitle.setText(R.string.ranking__reset_confirm_title);
        customTitle.setPadding(10, 10, 10, 10);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextSize(20);
        builder.setCustomTitle(customTitle);
        //>> Custom message
        TextView customMessage = new TextView(RankingActivity.this);
        customMessage.setPadding(10, 40, 10, 40);
        customMessage.setText(R.string.ranking__reset_confirm_message);
        customMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        customMessage.setTextSize(20);
        builder.setView(customMessage);
        //>> Positive Button
        builder.setPositiveButton(
                R.string.ranking__reset_confirm_positive_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // --------------------------------
                        //  Reset FirstComeRankingSequence
                        // --------------------------------
                        final ProgressDialog progress1 = createAndShowProgressDialog(R.string.progress__processing);
                        FirstComeRankingSequence sequence = new FirstComeRankingSequence();
                        sequence.reset(new ResultCallback<FirstComeRankingSequence>() {
                            @Override
                            public void done(ABResult<FirstComeRankingSequence> result, ABException e) {
                                progress1.dismiss();
                                if (e == null) {
                                    int code = result.getCode();
                                    if (code == ABStatus.OK) {
                                        // --------------------------------
                                        //  Delete All FirstComeRanking
                                        // --------------------------------
                                        final ProgressDialog progress2 = createAndShowProgressDialog(R.string.progress__deleting);
                                        AB.DBService.deleteWithQuery(FirstComeRanking.query(), new ResultCallback<Void>() {
                                            @Override
                                            public void done(ABResult<Void> result, ABException e) {
                                                progress2.dismiss();
                                                if (e == null) {
                                                    int code = result.getCode();
                                                    if (code == ABStatus.NO_CONTENT) {
                                                        showMessage(RankingActivity.this, R.string.message_success__deleted);
                                                    } else {
                                                        showUnexpectedStatusCodeError(RankingActivity.this, code);
                                                    }
                                                } else {
                                                    showError(RankingActivity.this, e);
                                                }
                                            }
                                        });
                                    } else {
                                        showUnexpectedStatusCodeError(RankingActivity.this, code);
                                    }
                                } else {
                                    showError(RankingActivity.this, e);
                                }
                            }
                        });
                    }
                });
        //>> Negative Button
        builder.setNegativeButton(
                R.string.ranking__reset_confirm_negative_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /* NOP */
                    }
                });

        builder.show();
    }

}
