//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.activities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.appiaries.baas.sdk.AB;
import com.appiaries.baas.sdk.ABException;
import com.appiaries.baas.sdk.ABQuery;
import com.appiaries.baas.sdk.ABResult;
import com.appiaries.baas.sdk.ABStatus;
import com.appiaries.baas.sdk.ResultCallback;
import com.appiaries.puzzle.R;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.common.PreferenceHelper;
import com.appiaries.puzzle.models.Image;
import com.appiaries.puzzle.models.Stage;

public class TopActivity extends BaseActivity {

    private static class ViewHolder {
        public RelativeLayout layoutGroupStage;
        public LinearLayout layoutGroupSubmit;
        public TextView stageName;
        public Button startButton;
        public Button rankingButton;
        public Button nextButton;
        public Button previousButton;
    }

    private ViewHolder mViewHolder;

    private List<Stage> mStageList = new ArrayList<>();
    private int mSelectedIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();

        // --------------------------------
        //  Find All Stages
        // --------------------------------
        ABQuery query = Stage.query().orderBy(Stage.Field.ORDER, ABQuery.SortDirection.ASC);
        AB.DBService.findWithQuery(query, new ResultCallback<List<Stage>>() {
            @Override
            public void done(ABResult<List<Stage>> result, ABException e) {
                if (e == null) {
                    int code = result.getCode();
                    if (code == ABStatus.OK) {
                        mStageList = result.getData();
                        mViewHolder.stageName.setText(mStageList.get(mSelectedIndex).getName());
                        mViewHolder.layoutGroupStage.setVisibility(View.VISIBLE);
                        mViewHolder.layoutGroupSubmit.setVisibility(View.VISIBLE);

                        for (final Stage stage : mStageList) {
                            Image image = new Image();
                            image.setID(stage.getImageID());
                            // --------------------------------
                            //  Fetch Image
                            // --------------------------------
                            AB.FileService.fetch(image, new ResultCallback<Image>() {
                                @Override
                                public void done(ABResult<Image> fetchResult, ABException e) {
                                    if (e == null) {
                                        final Image fetchedImage = fetchResult.getData();
                                        String filename = fetchedImage.getID() + "-" + fetchedImage.getName();
                                        final File localFile = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + filename);
                                        if (!localFile.exists()) {
                                            // --------------------------------
                                            //  Download Image
                                            // --------------------------------
                                            fetchedImage.download(new ResultCallback<Void>() {
                                                @Override
                                                public void done(ABResult<Void> downloadResult, ABException e) {
                                                    if (e == null) {
                                                        //FIXME: bytes でとるのは限界がある
                                                        byte[] binary = downloadResult.getRawData();
                                                        BufferedOutputStream bos = null;
                                                        try {
                                                            bos = new BufferedOutputStream(new FileOutputStream(localFile));
                                                            bos.write(binary);
                                                            bos.close();
                                                        } catch (Exception ex) {
                                                            showError(TopActivity.this, new ABException(ex));
                                                        } finally {
                                                            if (bos != null) {
                                                                try {
                                                                    bos.close();
                                                                } catch (IOException ignored) { }
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        showUnexpectedStatusCodeError(TopActivity.this, code);
                    }
                } else {
                    showError(TopActivity.this, e);
                }
            }
        });
    }

    // If button back in the Top screen, the exit application
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setupView() {
        setContentView(R.layout.activity_top);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }

        mViewHolder = new ViewHolder();
        mViewHolder.layoutGroupStage  = (RelativeLayout) findViewById(R.id.layout_group_stage);
        mViewHolder.layoutGroupSubmit = (LinearLayout) findViewById(R.id.layout_group_submit);
        mViewHolder.stageName         = (TextView) findViewById(R.id.text_stage);
        mViewHolder.stageName.setTextColor(Color.BLACK);
        mViewHolder.startButton       = (Button) findViewById(R.id.button_start);
        mViewHolder.rankingButton     = (Button) findViewById(R.id.button_ranking);
        mViewHolder.nextButton        = (Button) findViewById(R.id.button_next);
        mViewHolder.previousButton    = (Button) findViewById(R.id.button_previous);
        mViewHolder.startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Stage stage = mStageList.get(mSelectedIndex);
                PreferenceHelper.saveStageId(getApplicationContext(), stage.getID());
                PreferenceHelper.saveStageName(getApplicationContext(), stage.getName());

                Intent intent = new Intent(TopActivity.this, PlayActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_STAGE, stage);
                startActivity(intent);
            }
        });
        mViewHolder.rankingButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(TopActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });
        mViewHolder.nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSelectedIndex < mStageList.size() - 1) {
                    mSelectedIndex++;
                    mViewHolder.stageName.setText(mStageList.get(mSelectedIndex).getName());
                    mViewHolder.previousButton.setVisibility(View.VISIBLE);
                    if (mSelectedIndex == mStageList.size() - 1) {
                        mViewHolder.nextButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        mViewHolder.previousButton.setVisibility(View.INVISIBLE);
        mViewHolder.previousButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSelectedIndex > 0) {
                    mSelectedIndex--;
                    mViewHolder.stageName.setText(mStageList.get(mSelectedIndex).getName());
                    mViewHolder.nextButton.setVisibility(View.VISIBLE);
                    if (mSelectedIndex == 0) {
                        mViewHolder.previousButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

}
