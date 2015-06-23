//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.appiaries.puzzle.R;
import com.appiaries.puzzle.common.PreferenceHelper;
import com.appiaries.puzzle.models.FirstComeRanking;

public class FirstComeRankingAdapter extends ArrayAdapter<FirstComeRanking> {

    private static class ViewHolder {
        public TextView rank;
        public TextView nickname;
    }

    private ViewHolder mViewHolder = new ViewHolder();

    private final Context mContext;
    private List<FirstComeRanking> mDataSource;

    public FirstComeRankingAdapter(Context context, List<FirstComeRanking> values) {
        super(context, R.layout.list_raw_first_come_ranking, values);
        mContext = context;
        mDataSource = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // set up the ViewHolder
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_raw_first_come_ranking, parent, false);

            mViewHolder.rank     = (TextView) convertView.findViewById(R.id.text_rank);
            mViewHolder.nickname = (TextView) convertView.findViewById(R.id.text_nickname);

            // store the object with the view.
            convertView.setTag(mViewHolder);
        } else {
            // we've just avoided calling findViewById() on resource every time
            // just use the viewHolder
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        // get data object for current row
        if (mDataSource.size() > 0) {
            FirstComeRanking ranking = mDataSource.get(position);
            if (ranking != null) {
                mViewHolder.rank.setText(String.valueOf(position + 1));
                mViewHolder.nickname.setText(ranking.getNickname());
                if (ranking.getPlayerID().equals(PreferenceHelper.loadPlayerId(mContext))) {
                    mViewHolder.nickname.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    mViewHolder.nickname.setTypeface(Typeface.DEFAULT);
                }
            }
        }

        return convertView;
    }

    public synchronized void refreshAdapter(List<FirstComeRanking> dataitems) {
        mDataSource.clear();
    }

}
