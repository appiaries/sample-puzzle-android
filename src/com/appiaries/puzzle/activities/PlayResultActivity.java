//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.activities;

import java.util.List;

import com.appiaries.baas.sdk.AB;
import com.appiaries.baas.sdk.ABException;
import com.appiaries.baas.sdk.ABQuery;
import com.appiaries.baas.sdk.ABResult;
import com.appiaries.baas.sdk.ABStatus;
import com.appiaries.baas.sdk.ResultCallback;
import com.appiaries.puzzle.R;
import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.common.PreferenceHelper;
import com.appiaries.puzzle.models.FirstComeRanking;
import com.appiaries.puzzle.models.TimeRanking;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;

public class PlayResultActivity extends BaseActivity {

    private static class ViewHolder {
        public LinearLayout playResultLayout;
        public TextView stageName;
        public TextView ownScore;
        public TextView ownTimeRank;
        public TextView timeRankTotal;
        public TextView ownFirstComeRank;
        public TextView firstComeRankTotal;
        public Button homeButton;
        public Button rankingButton;
    }

    private ViewHolder mViewHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();

        Context context = getApplicationContext();
        final String playerId  = PreferenceHelper.loadPlayerId(context);
        final String stageId   = PreferenceHelper.loadStageId(context);
        final String stageName = PreferenceHelper.loadStageName(context);
        final long newScore    = getIntent().getLongExtra(Constants.EXTRA_KEY_SCORE, -1);

        mViewHolder.stageName.setText(stageName);
        mViewHolder.ownScore.setText(String.valueOf(newScore));

        // --------------------------------
        //  Find All TimeRanking
        // --------------------------------
        ABQuery timeRankQuery = TimeRanking.query()
                .where(TimeRanking.Field.STAGE_ID).equalsTo(stageId)
                .orderBy(TimeRanking.Field.SCORE, ABQuery.SortDirection.ASC);
        AB.DBService.findWithQuery(timeRankQuery, new ResultCallback<List<TimeRanking>>() {
            @Override
            public void done(ABResult<List<TimeRanking>> timeRankResult, ABException e) {
                if (e == null) {
                    int code = timeRankResult.getCode();
                    if (code == ABStatus.OK) {
                        List<TimeRanking> timeRankingList = timeRankResult.getData();
                        int ownTimeRank = -1;
                        for (int i = 0; i < timeRankingList.size(); i++) {
                            TimeRanking ranking = timeRankingList.get(i);
                            if (ranking.getPlayerID().equals(playerId)) {
                                ownTimeRank = i + 1;
                                break;
                            }
                        }

                        if (ownTimeRank >= 0) {
                            mViewHolder.ownTimeRank.setText(String.valueOf(ownTimeRank));
                        } else {
                            mViewHolder.ownTimeRank.setText("--");
                        }
                        mViewHolder.timeRankTotal.setText(String.valueOf(timeRankResult.getTotal()));

                        // --------------------------------
                        //  Find All FirstComeRanking
                        // --------------------------------
                        ABQuery firstComeRankQuery = FirstComeRanking.query()
                                .where(FirstComeRanking.Field.STAGE_ID).equalsTo(stageId)
                                .orderBy(FirstComeRanking.Field.CREATED, ABQuery.SortDirection.ASC);
                        AB.DBService.findWithQuery(firstComeRankQuery, new ResultCallback<List<FirstComeRanking>>() {
                            @Override
                            public void done(ABResult<List<FirstComeRanking>> firstComeRankResult, ABException e) {
                                if (e == null) {
                                    int code = firstComeRankResult.getCode();
                                    if (code == ABStatus.OK) {
                                        List<FirstComeRanking> firstComeRankingList = firstComeRankResult.getData();
                                        int ownFirstComeRank = -1;
                                        for (int i = 0; i < firstComeRankingList.size(); i++) {
                                            FirstComeRanking ranking = firstComeRankingList.get(i);
                                            if (ranking.getPlayerID().equals(playerId)) {
                                                ownFirstComeRank = i + 1;
                                                break;
                                            }
                                        }
                                        mViewHolder.firstComeRankTotal.setText(String.valueOf(firstComeRankResult.getTotal()));
                                        mViewHolder.ownFirstComeRank.setText(String.valueOf(ownFirstComeRank));

                                        mViewHolder.playResultLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        showUnexpectedStatusCodeError(PlayResultActivity.this, code);
                                    }
                                } else {
                                    showError(PlayResultActivity.this, e);
                                }
                            }
                        });

                    } else {
                        showUnexpectedStatusCodeError(PlayResultActivity.this, code);
                    }
                } else {
                    showError(PlayResultActivity.this, e);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        backToTopScreen();
    }

    private void setupView() {
        setContentView(R.layout.activity_play_result);

        mViewHolder = new ViewHolder();
        mViewHolder.playResultLayout = (LinearLayout) findViewById(R.id.layout_play_result);
        mViewHolder.stageName          = (TextView) findViewById(R.id.text_stage_name);
        mViewHolder.ownScore           = (TextView) findViewById(R.id.text_own_score);
        mViewHolder.ownTimeRank        = (TextView) findViewById(R.id.text_own_time_rank);
        mViewHolder.timeRankTotal      = (TextView) findViewById(R.id.text_time_rank_total);
        mViewHolder.ownFirstComeRank   = (TextView) findViewById(R.id.text_own_first_come_rank);
        mViewHolder.firstComeRankTotal = (TextView) findViewById(R.id.text_first_come_rank_total);
        mViewHolder.homeButton         = (Button)   findViewById(R.id.button_home);
        mViewHolder.rankingButton      = (Button)   findViewById(R.id.button_ranking);
        mViewHolder.homeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                backToTopScreen();
            }
        });
        mViewHolder.rankingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(PlayResultActivity.this, RankingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void backToTopScreen(){
        Intent intent = new Intent(PlayResultActivity.this, TopActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
