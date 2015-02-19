/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.managers;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import com.appiaries.APISException;
import com.appiaries.APISResult;
import com.appiaries.APISUser;
import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.common.TextHelper;
import com.appiaries.puzzle.jsonmodels.Player;

/**
 * 
 * @creator ntduc
 * 
 */
public class PlayerManager {

	/**
	 * Create PlayerManager
	 */
	private final static PlayerManager instance = new PlayerManager();

	/**
	 * init
	 */
	private PlayerManager() {

	}

	/**
	 * 
	 * @return PlayerManager
	 */
	public static PlayerManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param ctx
	 *            is Context
	 * @return List<Player>
	 * @throws JSONException
	 */
	public Player getInformationList() throws JSONException {

		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		try {

			responseObject = APISUser.getOwnInformation();
			if (responseObject.getResponseData() != null) {
				jsonString = TextHelper.convertToJSON(responseObject
						.getResponseData());
			}
			Log.d("PlayerManager json return string: ", jsonString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("JSON Response: ", jsonString);

		JSONObject obj = new JSONObject(jsonString);

		Player player = new Player();

		player.setLoginId(obj.getString("login_id"));
		player.setNickname(obj.getString("nickname"));

		return player;
	}

	public String doLogin(String strLoginId, String strPassword,
			boolean bAutoLogin, Context ctx) throws JSONException {

		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;
		try {

			responseObject = APISUser.login(strLoginId, strPassword, true);
			if (responseObject.getResponseCode() == 201)
				jsonString = TextHelper.convertToJSON(responseObject
						.getResponseData());
		} catch (APISException e) {
			e.printStackTrace();
		}
		Log.d("JSON Response: ", jsonString);

		JSONObject jsonObj = new JSONObject(jsonString);

		String userId = jsonObj.getString("user_id");

		// Log.d("JSON Response: ", jsonString);
		return userId;
	}

	public APISResult registerUser(String strLoginId, String strPassword,
			String strEmail, Map<String, Object> data) throws JSONException,
			APISException {
		APISResult responseObject = null;
		responseObject = APISUser.registUser(strLoginId, strPassword, strEmail,
				data);
		return responseObject;
	}
}
