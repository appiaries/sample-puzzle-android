//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import com.appiaries.baas.sdk.AB;
import com.appiaries.baas.sdk.ABException;
import com.appiaries.baas.sdk.ABQuery;
import com.appiaries.baas.sdk.ABResult;
import com.appiaries.puzzle.R;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.common.PreferenceHelper;
import com.appiaries.puzzle.games.CountDownTextView;
import com.appiaries.puzzle.games.CountDownTextView.CountDownTimerListener;
import com.appiaries.puzzle.games.TileBoardView;
import com.appiaries.puzzle.games.TileBoardView.TileBoardViewListener;
import com.appiaries.puzzle.models.FirstComeRanking;
import com.appiaries.puzzle.models.FirstComeRankingSequence;
import com.appiaries.puzzle.models.Stage;
import com.appiaries.puzzle.models.TimeRanking;

public class PlayActivity extends BaseActivity implements CountDownTimerListener, TileBoardViewListener {
    private static final String TAG = PlayActivity.class.getSimpleName();

    private TileBoardView mGameBoard;
    private CountDownTextView mCountDownView;
    private Stage mStage;
    private String mStageId = null;
    private File mImageFile;
    private boolean mStartCountDownFlag = false;

    private static final int START_DELAY = 3000; // Wait 3 seconds before the game begins

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();

        mGameBoard.shuffle();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                // Start Count Down
                mCountDownView.startCountDown();
                mStartCountDownFlag = true;
            }
        }, START_DELAY);

//        mCountDownView.startCountDown();
//        mStartCountDownFlag = true;
    }

    @Override
    protected void onDestroy() {
        if (mCountDownView.isCountingDown()) {
            mCountDownView.stopCountDown();
        }
        super.onDestroy();
    }

    @Override
    public void onTimeUp() {
        createAndShowConfirmationDialog(
                R.string.play__timed_up_confirm_title,
                R.string.play__timed_up_confirm_message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.play_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sample_menu:
                if(mStartCountDownFlag){
                    Intent intent = new Intent(this, SampleActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_IMAGE_FILENAME, mImageFile.getName());
                    intent.putExtra(Constants.EXTRA_KEY_TIME_LIMIT, mCountDownView.getRemainingTime());
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFinish() {
        mCountDownView.stopCountDown();

        final int score = (int) mCountDownView.getElapsedTime();

        final ProgressDialog progress = createAndShowProgressDialog(R.string.play__progress_sending);

        Handler mHandler = new Handler();
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {

                String stageId = mStageId;
                Context context = getApplicationContext();
                String playerId = PreferenceHelper.loadPlayerId(context);
                String nickname = PreferenceHelper.loadNickname(context);

                try {
                    // --------------------------------
                    //  Find Own FirstComeRanking
                    // --------------------------------
                    ABQuery queryAllFirstComeRanking = FirstComeRanking.query()
                            .where(FirstComeRanking.Field.STAGE_ID).equalsTo(stageId)
                            .and(FirstComeRanking.Field.PLAYER_ID).equalsTo(playerId);
                    ABResult<List<FirstComeRanking>> resultAllFirstComeRanking = AB.DBService.findSynchronouslyWithQuery(queryAllFirstComeRanking);
                    List<FirstComeRanking> firstComeRankingList = resultAllFirstComeRanking.getData();
                    if (firstComeRankingList.size() == 0) {
                        // --------------------------------
                        //  Increment Sequence
                        // --------------------------------
                        FirstComeRankingSequence sequence = new FirstComeRankingSequence();
                        long rank = sequence.getNextValueSynchronously();
                        // --------------------------------
                        //  Create FirstComeRanking
                        // --------------------------------
                        FirstComeRanking ranking = new FirstComeRanking();
                        ranking.setStageID(stageId);
                        ranking.setPlayerID(playerId);
                        ranking.setNickname(nickname);
                        ranking.setScore(score);
                        ranking.setRank((int)rank);
                        ranking.saveSynchronously();
                    }

                    // --------------------------------
                    //  Find Own TimeRaking
                    // --------------------------------
                    ABQuery queryAllTimeRanking = TimeRanking.query()
                            .where(TimeRanking.Field.STAGE_ID).equalsTo(stageId)
                            .and(TimeRanking.Field.PLAYER_ID).equalsTo(playerId);
                    ABResult<List<TimeRanking>> resultAllTimeRanking = AB.DBService.findSynchronouslyWithQuery(queryAllTimeRanking);
                    List<TimeRanking> timeRankingList = resultAllTimeRanking.getData();
                    if (timeRankingList.size() > 0) {
                        TimeRanking ownTimeRanking = timeRankingList.get(0);
                        if (score < ownTimeRanking.getScore()) { //NOTE: スコアが小さい方がより高得点
                            // --------------------------------
                            //  Update TimeRanking
                            // --------------------------------
                            ownTimeRanking.setScore(score);
                            ownTimeRanking.saveSynchronously();
                        }
                    } else {
                        // --------------------------------
                        //  Create TimeRanking
                        // --------------------------------
                        TimeRanking ranking = new TimeRanking();
                        ranking.setStageID(stageId);
                        ranking.setPlayerID(playerId);
                        ranking.setNickname(nickname);
                        ranking.setScore(score);
                        ranking.saveSynchronously();
                    }

                    progress.dismiss();

                    Intent intent = new Intent(PlayActivity.this, PlayResultActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_STAGE_ID, stageId);
                    intent.putExtra(Constants.EXTRA_KEY_SCORE, mCountDownView.getElapsedTime());
                    startActivity(intent);
                    finish();

                } catch (ABException e) {
                    progress.dismiss();
                    showError(PlayActivity.this, e);
                    e.printStackTrace();
                }
            }
        };
        mHandler.postDelayed(mRunnable, 2000);
    }

    private void setupView() {
        setContentView(R.layout.activity_play);

        mStage = (Stage) getIntent().getSerializableExtra(Constants.EXTRA_KEY_STAGE);
        mStageId = PreferenceHelper.loadStageId(getApplicationContext());

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mStage.getName());
        }

        long countDownTime = mStage.getTimeLimit();
        int numberOfWidthPieces = mStage.getNumberOfHorizontalPieces();
        int numberOfHeightPieces = mStage.getNumberOfVerticalPieces();

        // Get Timer Layout
        final RelativeLayout timerLayout = (RelativeLayout) findViewById(R.id.timer_layout);

        // Initial CountDownTextView
        mCountDownView = new CountDownTextView(PlayActivity.this, PlayActivity.this, countDownTime);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        // Add CountDown View into Main Layout
        timerLayout.addView(mCountDownView, params);

        DisplayMetrics dimension = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimension);

        int screenWidth = dimension.widthPixels;

        // Search image file
        File dir = getApplication().getFilesDir();
        File[] foundFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.startsWith(mStage.getImageID() + "-");
            }
        });
        if (foundFiles.length > 0) {
            mImageFile = foundFiles[0];

            // Sample Picture
            Bitmap picture = loadBitmap(mImageFile);

            mGameBoard = (TileBoardView) findViewById(R.id.game_board);
            mGameBoard.setBackgroundColor(Color.parseColor("#000000"));
            mGameBoard.setTileBoardViewListener(PlayActivity.this);
            mGameBoard.playWithImage(picture, numberOfWidthPieces, numberOfHeightPieces, screenWidth);
        }

    }

    private Bitmap loadBitmap(File f) {
        try {
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
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
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
        } catch (IOException e) {
            showError(PlayActivity.this, new ABException(e));
        }
        return null;
    }

}
