/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.adapters;

import java.util.List;

import com.appiaries.puzzle.R;
import com.appiaries.puzzle.common.APIHelper;
import com.appiaries.puzzle.jsonmodels.TimeRanking;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class TimeRankingAdapter extends ArrayAdapter<TimeRanking> {
	private final Context context;
	private List<TimeRanking> dataSource;

	public TimeRankingAdapter(Context context, List<TimeRanking> values) {
		super(context, R.layout.jikan_row, values);

		this.context = context;
		this.dataSource = values;		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PlanetHolder holder = new PlanetHolder();
		if (convertView == null) {
			// set up the ViewHolder
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.jikan_row, parent,
					false);

			TextView txtRank = (TextView) convertView
					.findViewById(R.id.txtRank);
			TextView txtNickName = (TextView) convertView
					.findViewById(R.id.txtNickName);
			TextView txtTime = (TextView) convertView
					.findViewById(R.id.txtTime);

			holder.txtRank = txtRank;
			holder.txtNickName = txtNickName;
			holder.txtTime = txtTime;

			// store the object with the view.
			convertView.setTag(holder);
		} else {
			// we've just avoided calling findViewById() on resource everytime
			// just use the viewHolder
			holder = (PlanetHolder) convertView.getTag();
		}

		// get data object for current row		
		TimeRanking info = dataSource.get(position);
		int rank = position + 1;
		if (info != null) {			
			holder.txtRank.setText(rank + "");			
			holder.txtNickName.setText(info.getNickname());
			
			if (info.getUserId().equals(APIHelper.getStringInLocalStorage(
					context, "user_id")))
			{
				holder.txtNickName.setTypeface(Typeface.DEFAULT_BOLD);
			}
			else
			{
				holder.txtNickName.setTypeface(Typeface.DEFAULT);
			}
			
			holder.txtTime.setText(info.getScore() + " sec");
		}

		return convertView;
	}

	/* *********************************
	 * We use the holder pattern It makes the view faster and avoid finding the
	 * component *********************************
	 */
	private static class PlanetHolder {

		public TextView txtRank;
		public TextView txtNickName;
		public TextView txtTime;
	}
	
	/**
	 * refresAdapter
	 * @param dataitems
	 */
	public synchronized void refreshAdapter(List<TimeRanking> dataitems) {
		dataSource.clear();
	}
}
