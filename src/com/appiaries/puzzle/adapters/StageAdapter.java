//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appiaries.puzzle.models.Stage;

import java.util.List;

public class StageAdapter extends ArrayAdapter<Stage> {

    private Context mContext;
    private List<Stage> mDataSource;

    public StageAdapter(Context context, int textViewResourceId, List<Stage> values) {
        super(context, textViewResourceId, values);
        mContext = context;
        mDataSource = values;
    }

    public int getCount(){
       return mDataSource.size();
    }

    public Stage getItem(int position){
       return mDataSource.get(position);
    }

    public long getItemId(int position){
       return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(mContext);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(mDataSource.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(mContext);
        label.setTextColor(Color.BLACK);
        label.setText(mDataSource.get(position).getName());

        return label;
    }

}
