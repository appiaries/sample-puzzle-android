/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.appiaries.puzzle.R;
import com.appiaries.puzzle.common.APIHelper;
import com.appiaries.puzzle.jsonmodels.FirstComeRanking;
import android.widget.ArrayAdapter;

public class FirstComeRankingAdapter extends ArrayAdapter<FirstComeRanking> {

	private final Context context;
	private List<FirstComeRanking> dataSource;

	public FirstComeRankingAdapter(Context context, List<FirstComeRanking> values) {
		super(context, R.layout.senchaku_row, values);

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

			convertView = inflater
					.inflate(R.layout.senchaku_row, parent, false);

			TextView txtRank = (TextView) convertView
					.findViewById(R.id.txtRank);
			TextView txtNickName = (TextView) convertView
					.findViewById(R.id.txtNickName);

			holder.txtRank = txtRank;
			holder.txtNickName = txtNickName;

			// store the object with the view.
			convertView.setTag(holder);
		} else {
			// we've just avoided calling findViewById() on resource everytime
			// just use the viewHolder
			holder = (PlanetHolder) convertView.getTag();
		}

		// get data object for current row
		if(dataSource.size() > 0){
			FirstComeRanking info = dataSource.get(position);
			if (info != null) {
				holder.txtRank.setText((position + 1) + "");
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
			}
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
	}

	// refresh Adapter Method calling in Homepage Activity

	public synchronized void refreshAdapter(List<FirstComeRanking> dataitems) {
		dataSource.clear();
	}

}
