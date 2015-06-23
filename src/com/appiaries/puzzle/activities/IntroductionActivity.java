//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.appiaries.baas.sdk.AB;
import com.appiaries.baas.sdk.ABException;
import com.appiaries.baas.sdk.ABQuery;
import com.appiaries.baas.sdk.ABResult;
import com.appiaries.baas.sdk.ResultCallback;
import com.appiaries.puzzle.R;
import com.appiaries.puzzle.models.Introduction;

public class IntroductionActivity extends BaseActivity {
    private static final String TAG = IntroductionActivity.class.getSimpleName();

    private static class ViewHolder {
        public TextView content;
        public Button skipButton;
        public Button nextButton;
        public Button previousButton;
    }

    private ViewHolder mViewHolder;

    List<Introduction> mIntroductionList = new ArrayList<>();
    Introduction mIntroduction = new Introduction(); // Current introduction
    int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();

        // --------------------------------
        //  Find All Introductions
        // --------------------------------
        ABQuery query = Introduction.query().orderBy(Introduction.Field.ORDER, ABQuery.SortDirection.ASC);
        AB.DBService.findWithQuery(query, new ResultCallback<List<Introduction>>() {
            @Override
            public void done(ABResult<List<Introduction>> result, ABException e) {
                if (e == null) {
                    mIntroductionList = result.getData();
                    if (mIntroductionList.size() > 0) {
                        mIntroduction = mIntroductionList.get(mCurrentIndex);
                        mViewHolder.content.setText(mIntroduction.getContent());
                    }
                } else {
                    showError(IntroductionActivity.this, e);
                }
            }
        });
    }

    private void setupView() {
        setContentView(R.layout.activity_introduction);

        mViewHolder = new ViewHolder();
        mViewHolder.content        = (TextView) findViewById(R.id.content);
        mViewHolder.skipButton     = (Button)   findViewById(R.id.button_skip);
        mViewHolder.nextButton     = (Button)   findViewById(R.id.button_next);
        mViewHolder.previousButton = (Button)   findViewById(R.id.button_previous);
        mViewHolder.skipButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        mViewHolder.nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mIntroductionList != null && mIntroductionList.size() > 0) {
                    if (mCurrentIndex < mIntroductionList.size() - 1) {
                        mCurrentIndex++;
                        mIntroduction = mIntroductionList.get(mCurrentIndex);
                        mViewHolder.content.setText(mIntroduction.getContent());
                        mViewHolder.previousButton.setVisibility(View.VISIBLE);
                        if (mCurrentIndex == mIntroductionList.size() - 1) {
                            mViewHolder.nextButton.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });
        mViewHolder.previousButton.setVisibility(View.INVISIBLE);
        mViewHolder.previousButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mIntroductionList != null && mIntroductionList.size() > 0) {
                    if (mCurrentIndex > 0) {
                        mCurrentIndex--;
                        mIntroduction = mIntroductionList.get(mCurrentIndex);
                        mViewHolder.content.setText(mIntroduction.getContent());
                        mViewHolder.nextButton.setVisibility(View.VISIBLE);
                        if (mCurrentIndex == 0) {
                            mViewHolder.previousButton.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });
    }

}
