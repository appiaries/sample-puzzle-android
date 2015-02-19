/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.appiaries.APISException;
import com.appiaries.APISJsonData;
import com.appiaries.APISQueryCondition;
import com.appiaries.APISResult;
import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.common.TextHelper;
import com.appiaries.puzzle.jsonmodels.TimeRanking;

/**
 * 
 * @creator ntduc
 * 
 */
public class TimeRankingManager {

	/**
	 * Create TimeRankingManager
	 */
	private final static TimeRankingManager instance = new TimeRankingManager();

	/**
	 * init
	 */
	private TimeRankingManager() {

	}

	/**
	 * 
	 * @return TimeRankingManager
	 */
	public static TimeRankingManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param ctx
	 *            is Context
	 * @return List<TimeRanking>
	 * @throws JSONException
	 */
	public List<TimeRanking> getInformationList() throws JSONException {

		APISQueryCondition appQueryCondition = new APISQueryCondition();

		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISJsonData.selectJsonData(
					Constants.COLLECTION_TIME_RANKING, appQueryCondition);
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

		List<TimeRanking> TimeRankingList = new ArrayList<TimeRanking>();

		for (int i = 0; i < jsonObjs.length(); i++) {

			JSONObject obj = jsonObjs.getJSONObject(i);

			TimeRankingList.add(new TimeRanking(obj.getString("_id"), obj
					.getString("stage_id"), obj.getString("user_id"), obj
					.getString("nickname"), obj.getInt("score"), obj
					.getLong("_cts"), obj.getString("_cby"), obj
					.getLong("_uts"), obj.getString("_uby")));
		}
		Collections.sort(TimeRankingList);
		return TimeRankingList;
	}

	/**
	 * 
	 * @param ctx
	 *            is Context
	 * @return List<TimeRanking>
	 * @throws JSONException
	 */
	public List<TimeRanking> getTimeRankingList(String stageId)
			throws JSONException {

		APISQueryCondition appQueryCondition = new APISQueryCondition();
		appQueryCondition.equal("stage_id", stageId);
		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISJsonData.selectJsonData(
					Constants.COLLECTION_TIME_RANKING, appQueryCondition);
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

		List<TimeRanking> TimeRankingList = new ArrayList<TimeRanking>();

		for (int i = 0; i < jsonObjs.length(); i++) {

			JSONObject obj = jsonObjs.getJSONObject(i);

			TimeRankingList.add(new TimeRanking(obj.getString("_id"), obj
					.getString("stage_id"), obj.getString("user_id"), obj
					.getString("nickname"), obj.getInt("score"), obj
					.getLong("_cts"), obj.getString("_cby"), obj
					.getLong("_uts"), obj.getString("_uby")));
		}
		Collections.sort(TimeRankingList);
		return TimeRankingList;
	}

	/**
	 * 
	 * @param ctx
	 *            is Context
	 * @return List<TimeRanking>
	 * @throws JSONException
	 */
	public TimeRanking getInformationList(String stageId, String userId)
			throws JSONException {

		APISQueryCondition appQueryCondition = new APISQueryCondition();
		appQueryCondition.equal("stage_id", stageId);
		appQueryCondition.equal("user_id", userId);
		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISJsonData.selectJsonData(
					Constants.COLLECTION_TIME_RANKING, appQueryCondition);
			if (responseObject.getResponseData() != null) {
				jsonString = TextHelper.convertToJSON(responseObject
						.getResponseData());
			}
			Log.d("Response json string: ", jsonString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("JSON Response: ", jsonString);

		JSONObject jsonObj = new JSONObject(jsonString);

		String objsString = jsonObj.getString("_objs");

		JSONArray jsonObjs = new JSONArray(objsString);

		for (int i = 0; i < jsonObjs.length(); i++) {

			JSONObject obj = jsonObjs.getJSONObject(i);

			return new TimeRanking(obj.getString("_id"),
					obj.getString("stage_id"), obj.getString("user_id"),
					obj.getString("nickname"), obj.getInt("score"),
					obj.getLong("_cts"), obj.getString("_cby"),
					obj.getLong("_uts"), obj.getString("_uby"));
		}

		return new TimeRanking();
	}

	/**
	 * Delete all
	 */
	public void deleteAll() {
		APISQueryCondition appQueryCondition = new APISQueryCondition();

		APISResult responseObject;

		try {

			responseObject = APISJsonData.deleteJsonData(
					Constants.COLLECTION_TIME_RANKING, appQueryCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int registJsonData(Map<String, Object> data)
			throws APISException {
		APISResult responseObject = null;

		HashMap<String, Object> registData = new HashMap<String, Object>();

		registData.put("user_id", data.get("user_id"));
		registData.put("stage_id", data.get("stage_id"));
		registData.put("nickname", data.get("nickname"));
		registData.put("score", data.get("score"));

		responseObject = APISJsonData.registJsonData(
				Constants.COLLECTION_TIME_RANKING, null, registData);

		return responseObject.getResponseCode();
	}

	public int updateJsonData(String objectId, Map<String, Object> data)
			throws APISException {

		HashMap<String, Object> registData = new HashMap<String, Object>();

		registData.put("user_id", data.get("user_id"));
		registData.put("stage_id", data.get("stage_id"));
		registData.put("nickname", data.get("nickname"));
		registData.put("score", data.get("score"));
		APISResult responseObject = APISJsonData.updateJsonData(
				Constants.COLLECTION_TIME_RANKING, objectId, registData);

		return responseObject.getResponseCode();
	}
}
