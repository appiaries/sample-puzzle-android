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
import com.appiaries.puzzle.jsonmodels.FirstComeRanking;

/**
 * 
 * author ntduc
 * 
 */
public class FirstComeRankingManager {

	/**
	 * create FirstComeRankingManager
	 */
	private final static FirstComeRankingManager instance = new FirstComeRankingManager();

	/**
	 * init
	 */
	private FirstComeRankingManager() {

	}

	/**
	 * 
	 * @return FirstComeRankingManager
	 */
	public static FirstComeRankingManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param ctx
	 *            is Context
	 * @return List<FirstComeRanking>
	 * @throws JSONException
	 */
	public List<FirstComeRanking> getInformationList() throws JSONException {

		APISQueryCondition appQueryCondition = new APISQueryCondition();

		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISJsonData.selectJsonData(
					Constants.COLLECTION_FIRST_COME_RANKING, appQueryCondition);
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

		List<FirstComeRanking> FirstComeRankingList = new ArrayList<FirstComeRanking>();

		for (int i = 0; i < jsonObjs.length(); i++) {

			JSONObject obj = jsonObjs.getJSONObject(i);

			FirstComeRankingList.add(new FirstComeRanking(obj.getString("_id"),
					obj.getString("stage_id"), obj.getString("user_id"), obj
							.getString("nickname"), obj.getInt("rank"), obj
							.getInt("score"), obj.getLong("_cts"), obj
							.getString("_cby"), obj.getLong("_uts"), obj
							.getString("_uby")));
		}
		Collections.sort(FirstComeRankingList);
		return FirstComeRankingList;
	}

	/**
	 * 
	 * @param stageId
	 * @return
	 * @throws JSONException
	 */
	public List<FirstComeRanking> getInformationList(String stageId)
			throws JSONException {

		APISQueryCondition appQueryCondition = new APISQueryCondition();
		appQueryCondition.equal("stage_id", stageId);
		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISJsonData.selectJsonData(
					Constants.COLLECTION_FIRST_COME_RANKING, appQueryCondition);
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

		List<FirstComeRanking> FirstComeRankingList = new ArrayList<FirstComeRanking>();

		for (int i = 0; i < jsonObjs.length(); i++) {

			JSONObject obj = jsonObjs.getJSONObject(i);

			FirstComeRankingList.add(new FirstComeRanking(obj.getString("_id"),
					obj.getString("stage_id"), obj.getString("user_id"), obj
							.getString("nickname"), obj.getInt("rank"), obj
							.getInt("score"), obj.getLong("_cts"), obj
							.getString("_cby"), obj.getLong("_uts"), obj
							.getString("_uby")));
		}
		Collections.sort(FirstComeRankingList);
		return FirstComeRankingList;
	}

	/**
	 * 
	 * @param stageId
	 * @param user_id
	 * @return
	 * @throws JSONException
	 */
	public FirstComeRanking getInformationList(String stageId, String userId)
			throws JSONException {

		APISQueryCondition appQueryCondition = new APISQueryCondition();
		appQueryCondition.equal("stage_id", stageId);
		appQueryCondition.equal("user_id", userId);
		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISJsonData.selectJsonData(
					Constants.COLLECTION_FIRST_COME_RANKING, appQueryCondition);
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

		for (int i = 0; i < jsonObjs.length(); i++) {

			JSONObject obj = jsonObjs.getJSONObject(i);

			return new FirstComeRanking(obj.getString("_id"),
					obj.getString("stage_id"), obj.getString("user_id"),
					obj.getString("nickname"), obj.getInt("rank"),
					obj.getInt("score"), obj.getLong("_cts"),
					obj.getString("_cby"), obj.getLong("_uts"),
					obj.getString("_uby"));
		}

		return new FirstComeRanking();
	}

	/**
	 * Delete all
	 */
	public int deleteAll() {
		APISQueryCondition appQueryCondition = new APISQueryCondition();

		int responseCode = 0;
		APISResult responseObject;

		try {

			responseObject = APISJsonData.deleteJsonData(
					Constants.COLLECTION_FIRST_COME_RANKING, appQueryCondition);
			if (responseObject != null) {
				responseCode = responseObject.getResponseCode();
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseCode;
	}

	public int registerJsonData(Map<String, Object> jsonDataToRegist) throws APISException {
		APISResult responseObject = null;

		HashMap<String, Object> registData = new HashMap<String, Object>();

		registData.put("user_id", jsonDataToRegist.get("user_id"));
		registData.put("stage_id", jsonDataToRegist.get("stage_id"));
		registData.put("nickname", jsonDataToRegist.get("nickname"));
		registData.put("rank", jsonDataToRegist.get("rank"));
		registData.put("score", jsonDataToRegist.get("score"));

		responseObject = APISJsonData.registJsonData(
				Constants.COLLECTION_FIRST_COME_RANKING, null, registData);

		return responseObject.getResponseCode();
	}

}
