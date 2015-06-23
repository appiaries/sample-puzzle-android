//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.adapters;

import java.util.List;

import com.appiaries.puzzle.R;
import com.appiaries.puzzle.common.PreferenceHelper;
import com.appiaries.puzzle.models.TimeRanking;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class TimeRankingAdapter extends ArrayAdapter<TimeRanking> {

    private static class ViewHolder {
        public TextView rank;
        public TextView nickname;
        public TextView time;
    }

    ViewHolder mViewHolder = new ViewHolder();

    private final Context mContext;
    private List<TimeRanking> mDataSource;
    private String mScoreSuffix;

    public TimeRankingAdapter(Context context, List<TimeRanking> values) {
        super(context, R.layout.list_raw_time_ranking, values);
        mContext = context;
        mDataSource = values;
        mScoreSuffix = context.getResources().getString(R.string.time_ranking__score_suffix);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // set up the ViewHolder
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_raw_time_ranking, parent, false);

            mViewHolder.rank     = (TextView) convertView.findViewById(R.id.text_rank);
            mViewHolder.nickname = (TextView) convertView.findViewById(R.id.text_nickname);
            mViewHolder.time     = (TextView) convertView.findViewById(R.id.text_time);

            // store the object with the view.
            convertView.setTag(mViewHolder);
        } else {
            // we've just avoided calling findViewById() on resource every time
            // just use the viewHolder
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        // get data object for current row
        TimeRanking ranking = mDataSource.get(position);
        int rank = position + 1;
        if (ranking != null) {
            mViewHolder.rank.setText(String.valueOf(rank));
            mViewHolder.nickname.setText(ranking.getNickname());
            if (ranking.getPlayerID().equals(PreferenceHelper.loadPlayerId(mContext))) {
                mViewHolder.nickname.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                mViewHolder.nickname.setTypeface(Typeface.DEFAULT);
            }
            mViewHolder.time.setText(ranking.getScore() + mScoreSuffix);
        }

        return convertView;
    }

    public synchronized void refreshAdapter(List<TimeRanking> dataitems) {
        mDataSource.clear();
    }

}
