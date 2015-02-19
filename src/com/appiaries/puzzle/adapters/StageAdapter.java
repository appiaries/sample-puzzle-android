/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appiaries.puzzle.jsonmodels.*;

public class StageAdapter extends ArrayAdapter<Stage>{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<Stage> dataSource;

    public StageAdapter(Context context, int textViewResourceId,
    		List<Stage> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.dataSource = values;
    }

    public int getCount(){
       return dataSource.size();
    }

    public Stage getItem(int position){
       return dataSource.get(position);
    }

    public long getItemId(int position){
       return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(dataSource.get(position).getStageName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(dataSource.get(position).getStageName());

        return label;
    }
}
