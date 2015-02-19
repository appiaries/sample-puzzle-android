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

import com.appiaries.APISFileData;
import com.appiaries.APISJsonData;
import com.appiaries.APISQueryCondition;
import com.appiaries.APISResult;
import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.common.TextHelper;
import com.appiaries.puzzle.jsonmodels.Image;

/**
 * 
 * author ntduc
 * 
 */
public class ImageManager {

	/**
	 * create ImageManager
	 */
	private final static ImageManager instance = new ImageManager();

	/**
	 * init
	 */
	private ImageManager() {

	}

	/**
	 * 
	 * @return ImageManager
	 */
	public static ImageManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param ctx
	 *            is Context
	 * @return List<Image>
	 * @throws JSONException
	 */
	public List<Image> getInformationList() throws JSONException {

		APISQueryCondition appQueryCondition = new APISQueryCondition();

		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISJsonData.selectJsonData(
					Constants.COLLECTION_IMAGE, appQueryCondition);
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

		List<Image> ImageList = new ArrayList<Image>();

		for (int i = 0; i < jsonObjs.length(); i++) {

			JSONObject obj = jsonObjs.getJSONObject(i);

			ImageList.add(new Image(obj.getString("_id"), obj
					.getString("_type"), obj.getString("_filename"), obj
					.getInt("_length"), obj.getLong("_cts"), obj
					.getString("_cby"), obj.getLong("_uts"), obj
					.getString("_uby")));
		}

		return ImageList;
	}

	/**
	 * 
	 * @param ctx
	 *            is Context
	 * @return List<Image>
	 * @throws JSONException
	 */
	public Image getInformationImage(String imageId) throws JSONException {

		APISQueryCondition appQueryCondition = new APISQueryCondition();
		appQueryCondition.equal("_id", imageId);
		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISFileData.selectData(
					Constants.COLLECTION_IMAGE, appQueryCondition);
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

			return new Image(obj.getString("_id"), obj.getString("_type"),
					obj.getString("_filename"), obj.getInt("_length"),
					obj.getLong("_cts"), obj.getString("_cby"),
					obj.getLong("_uts"), obj.getString("_uby"));
		}

		return null;
	}
}
