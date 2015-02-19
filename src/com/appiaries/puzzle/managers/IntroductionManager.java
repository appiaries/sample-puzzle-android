/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.managers;

import java.util.ArrayList;
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
import com.appiaries.puzzle.jsonmodels.Introduction;

public class IntroductionManager {

	private final static IntroductionManager instance = new IntroductionManager();

	public static IntroductionManager getInstance() {
		return instance;
	}
	
	/**
	 * 
	 * @param ctx is Context
	 * @return List<Stage>
	 * @throws JSONException
	 */
	public List<Introduction> getIntroductionList() throws JSONException {		

		APISQueryCondition appQueryCondition = new APISQueryCondition();

		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISJsonData.selectJsonData(
					Constants.COLLECTION_INTRODUCTION, appQueryCondition);
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

		List<Introduction> InformationList = new ArrayList<Introduction>();

		for (int i = 0; i < jsonObjs.length(); i++) {

			JSONObject obj = jsonObjs.getJSONObject(i);

			InformationList.add(new Introduction(obj.getString("_id"), 
					obj.getString("content"),
					obj.getString("order"), 
					obj.getString("_cby"), 
					obj.getString("_uby"), 
					obj.getLong("_cts"), 
					obj.getLong("_uts")));					
		}

		return InformationList;
	}
}
