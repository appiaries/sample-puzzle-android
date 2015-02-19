/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.managers;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.appiaries.APISException;
import com.appiaries.APISResult;
import com.appiaries.APISSequence;
import com.appiaries.puzzle.common.Constants;
import com.appiaries.puzzle.common.TextHelper;

/**
 * 
 * author ntduc
 * 
 */
public class FirstComeRankingSeqManager {

	/**
	 * Create FirstComeRankingSeqManager
	 */
	private final static FirstComeRankingSeqManager instance = new FirstComeRankingSeqManager();

	/**
	 * init
	 */
	private FirstComeRankingSeqManager() {

	}

	/**
	 * 
	 * @return FirstComeRankingSeqManager
	 */
	public static FirstComeRankingSeqManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param ctx
	 *            is Context
	 * @return List<FirstComeRankingSeq>
	 * @throws JSONException
	 * @throws APISException
	 */
	public Long getSequenceValue() throws JSONException, APISException {

		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		responseObject = APISSequence
				.getSequenceValue(Constants.COLLECTION_FIRST_COME_RANKING_SEQ);
		if (responseObject.getResponseData() != null) {
			jsonString = TextHelper.convertToJSON(responseObject
					.getResponseData());
		}
		Log.d("AppInfoManager json return string: ", jsonString);

		Log.d("JSON Response: ", jsonString);

		JSONObject jsonObj = new JSONObject(jsonString);

		return Long.valueOf(jsonObj.getString("seq"));
	}

	/**
	 * Delete all
	 * @throws JSONException 
	 */
	public int deleteAll() throws APISException, JSONException {
		
		Long value = getSequenceValue();
		return addSequence(-value);
	}

	/**
	 * 
	 * @param collectionID
	 * @param objectName
	 * @param data
	 * @return
	 * @throws APISException
	 */
	public int addSequence(Long lvalue) throws APISException, JSONException {
		APISResult responseObject;
		String jsonString = Constants.BLANK_STRING;

		responseObject = APISSequence.addSequence(
				Constants.COLLECTION_FIRST_COME_RANKING_SEQ, lvalue);
		if (responseObject.getResponseData() != null) {
			jsonString = TextHelper.convertToJSON(responseObject
					.getResponseData());
		}

		Log.d("JSON Response: ", jsonString);

		JSONObject jsonObj = new JSONObject(jsonString);

		return responseObject.getResponseCode();

	}

}
