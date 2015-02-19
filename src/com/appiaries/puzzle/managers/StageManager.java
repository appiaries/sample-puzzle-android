/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.appiaries.APISJsonData;
import com.appiaries.APISQueryCondition;
import com.appiaries.APISResult;
import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.common.TextHelper;
import com.appiaries.puzzle.jsonmodels.Stage;

/**
 * 
 * @creator ntduc
 * 
 */
public class StageManager {

	/**
	 * Create StageManager
	 */
	private final static StageManager instance = new StageManager();

	/**
	 * init
	 */
	private StageManager() {

	}

	/**
	 * 
	 * @return StageManager
	 */
	public static StageManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param ctx is Context
	 * @return List<Stage>
	 * @throws JSONException
	 */
	public List<Stage> getStageList() throws JSONException {		

		APISQueryCondition appQueryCondition = new APISQueryCondition();

		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISJsonData.selectJsonData(
					Constants.COLLECTION_STAGE, appQueryCondition);
			if (responseObject.getResponseData() != null) {
				jsonString = TextHelper.convertToJSON(responseObject
						.getResponseData());
			}
			Log.d("Stage json return string: ", jsonString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("JSON Response: ", jsonString);

		JSONObject jsonObj = new JSONObject(jsonString);

		String objsString = jsonObj.getString("_objs");

		JSONArray jsonObjs = new JSONArray(objsString);

		List<Stage> StageList = new ArrayList<Stage>();

		for (int i = 0; i < jsonObjs.length(); i++) {

			JSONObject obj = jsonObjs.getJSONObject(i);

			StageList.add(new Stage(obj.getString("_id"), 
					obj.getString("stage_id"),
					obj.getString("image_id"), 
					obj.getInt("number_of_horizontal_pieces"), 
					obj.getInt("number_of_vertical_pieces"), 
					obj.getInt("time_limit"), 
					obj.getLong("_cts"), 
					obj.getString("_cby"), 
					obj.getLong("_uts"), 
					obj.getString("_uby"),
					obj.getInt("order")));					
		}
		Collections.sort(StageList);
		return StageList;
	}
	
	/**
	 * 
	 * @param ctx is Context
	 * @return List<Stage>
	 * @throws JSONException
	 */
	public List<String> getNameStages() throws JSONException {		

		APISQueryCondition appQueryCondition = new APISQueryCondition();

		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISJsonData.selectJsonData(
					Constants.COLLECTION_STAGE, appQueryCondition);
			if (responseObject.getResponseData() != null) {
				jsonString = TextHelper.convertToJSON(responseObject
						.getResponseData());
			}
			Log.d("AppInfoManager json return string: ", jsonString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("JSON Response: ", jsonString);

		JSONObject jsonObj = new JSONObject(jsonString);

		String objsString = jsonObj.getString("_objs");

		JSONArray jsonObjs = new JSONArray(objsString);

		List<String> StageList = new ArrayList<String>();

		for (int i = 0; i < jsonObjs.length(); i++) {

			JSONObject obj = jsonObjs.getJSONObject(i);
			
			StageList.add(obj.getString("stage_id"));					
		}

		return StageList;
	}
}
