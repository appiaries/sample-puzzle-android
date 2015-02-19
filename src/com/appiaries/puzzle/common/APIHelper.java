/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import com.appiaries.puzzle.common.Constants;

/**
 * @author nmcuong
 * 
 */
public class APIHelper {

	/**
	 * Get Access Token
	 * 
	 * @param ctx
	 *            Context
	 * @return accessToken String
	 */
	public static String getAccessToken(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);
		String accessToken = pref.getString(Constants.ACCESS_TOKEN_KEY, "");

		return accessToken;
	}

	/**
	 * Save Access Token
	 * 
	 * @param ctx
	 *            Context
	 * @param accessToken
	 *            string
	 */
	public static void setAccessToken(Context ctx, String accessToken) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);
		Editor editor = pref.edit();

		editor.putString(Constants.ACCESS_TOKEN_KEY, accessToken);
		editor.commit();
	}

	public static void setStringToLocalStorage(Context ctx, String key, String value){
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);
		Editor editor = pref.edit();

		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getStringInLocalStorage(Context ctx, String key) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);
		return pref.getString(key, "");
	}
	/**
	 * Get Store Token
	 * 
	 * @param ctx
	 *            Context
	 * @return storeToken String
	 */
	public static String getStoreToken(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);
		String storeToken = pref.getString(Constants.STORE_TOKEN_KEY, "");

		return storeToken;
	}

	/**
	 * Save Store Token
	 * 
	 * @param ctx
	 *            Context
	 * @param storeToken
	 *            String
	 */
	public static void setStoreToken(Context ctx, String storeToken) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);
		Editor editor = pref.edit();

		editor.putString(Constants.STORE_TOKEN_KEY, storeToken);
		editor.commit();
	}
	/**
	 * Get Token Expire Date
	 * 
	 * @param ctx
	 *            Context
	 * @return date Date
	 */
	public static Date getTokenExpireDate(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);

		SimpleDateFormat formatter = new SimpleDateFormat(
				Constants.DATE_TIME_FORMAT);

		Date tokenExpireDate = null;

		try {
			tokenExpireDate = formatter.parse(pref.getString(
					Constants.TOKEN_EXPIRE_KEY, ""));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tokenExpireDate;
	}

	/**
	 * Save Token Expire Date
	 * 
	 * @param ctx
	 *            Context
	 * @param tokenExpireDate
	 *            Date
	 */
	public static void setTokenExpireDate(Context ctx, Date tokenExpireDate) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);
		Editor editor = pref.edit();

		SimpleDateFormat formatter = new SimpleDateFormat(
				Constants.DATE_TIME_FORMAT);
		editor.putString(Constants.TOKEN_EXPIRE_KEY,
				formatter.format(tokenExpireDate));
		editor.commit();
	}

	/**
	 * Get Login status
	 * 
	 * @param ctx
	 *            Context
	 * @return Login status
	 */
	public static boolean getLoginStatus(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);

		boolean isLoggedIn = Boolean.parseBoolean(pref.getString(
				Constants.LOGIN_FLAG, "false"));

		return isLoggedIn;
	}

	/**
	 * Save Login Status
	 * 
	 * @param ctx
	 *            Context
	 * @param status
	 */
	public static void setLoginStatus(Context ctx, boolean status) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);
		Editor editor = pref.edit();

		editor.putString(Constants.LOGIN_FLAG, Boolean.toString(status));
		editor.commit();
	}

	/**
	 * Generate OAuth Request URL with default scopes
	 * 
	 * @return OAuth URL
	 */
	public static String getOAuthUrl() {
		return APIHelper.getOAuthUrlWithScope("gender date_of_birth");
	}

	/**
	 * Get OAuth Request URL
	 * 
	 * @param scope
	 *            profile scope string
	 * @return OAuth URL
	 */
	public static String getOAuthUrlWithScope(String scope) {
		String url = "";

		UrlHelper urlHelper = new UrlHelper();
		urlHelper.addQuery("response_type", "token");
		urlHelper.addQuery("scope", scope);
		urlHelper.addQuery("display", "touch");
		// urlHelper.addQuery("redirect_uri", Constants.AUTH_CALLBACK_URL);

		url = String.format("%s?%s", Constants.AUTH_REQUEST_URL,
				urlHelper.toString());

		return url;
	}

	/**
	 * Build an Image File URL from a ObjectId, We can use this URL to display
	 * an image
	 * 
	 * @param objectId
	 * @return image file URL string
	 */
	public static String getImageFileUrlWithObjectId(String objectId) {
		return String.format("%s/%s/%s/%s/%s/_bin",
				Constants.DATASTORE_FILE_URL_BASE, Constants.APIS_DATASTORE_ID,
				Constants.APIS_APP_ID, Constants.IMAGE_COLLECTION_ID, objectId);
	}

	/**
	 * Get Collection Data by collectionId using DataStoreAPI
	 * 
	 * @param collectionId
	 *            collection to search
	 * @param conditions
	 *            search conditions
	 * @param parameters
	 *            search parameters
	 * @return JSON string
	 */
	public static String searchDataStoreAPIWithCollection(String storeToken,
			String accessToken, String collectionId, List<String> conditions,
			HashMap<String, String> parameters) {

		// Build the search parameters string
		String parameterString = "";
		if (parameters != null) {
			for (Entry<String, String> parameter : parameters.entrySet()) {
				if (parameterString.length() > 0) {
					parameterString += "&";
				}

				parameterString += String.format("%s=%s", parameter.getKey(),
						parameter.getValue());
			}
		}

		// Build the search conditions string
		String conditionString = "";
		if (conditions != null) {
			for (String condition : conditions) {
				conditionString += condition + ";";
			}
		}

		// Build the request URL from parameters and conditions
		String url = String.format("%s/%s/%s/%s/-;%s?%s",
				Constants.DATASTORE_JSON_URL_BASE, Constants.APIS_DATASTORE_ID,
				Constants.APIS_APP_ID, collectionId, conditionString,
				parameterString);

		// Make a request
		return GET(url, storeToken, accessToken);
	}

	/**
	 * Convert HTTPResponse Stream to String
	 * 
	 * @param inputStream
	 * @return string
	 * @throws IOException
	 */
	public static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

	

	/**
	 * API to update data store
	 * 
	 * @param storeToken
	 * @param accessToken
	 * @param collectionId
	 * @param userObjectId
	 * @param updateParam
	 */
	public static void updateDataStoreAPI(String storeToken,
			String accessToken, String collectionId, String userObjectId,
			HashMap<String, Object> updateParam) {

		// Build the request URL from parameters and conditions
		String url = String.format("%s/%s/%s/%s/%s?proc=patch",
				Constants.DATASTORE_JSON_URL_BASE, Constants.APIS_DATASTORE_ID,
				Constants.APIS_APP_ID, collectionId, userObjectId);

		// Make a request
		POST(url, storeToken, accessToken, updateParam);

	}

	/**
	 * API to create user's information
	 * 
	 * @param storeToken
	 * @param accessToken
	 * @param collectionId
	 * @param updateParam
	 * @return
	 */
	public static String createDataStoreAPI(String storeToken,
			String accessToken, String collectionId,
			HashMap<String, Object> updateParam) {

		// Build the request URL from parameters and conditions
		String url = String.format("%s/%s/%s/%s",
				Constants.DATASTORE_JSON_URL_BASE, Constants.APIS_DATASTORE_ID,
				Constants.APIS_APP_ID, collectionId);

		// Make a request
		return POST(url, storeToken, accessToken, updateParam);

	}

	/**
	 * POST method to execute creating, updating, editing
	 * 
	 * @param url
	 * @param storeToken
	 * @param accessToken
	 * @param parameters
	 * @return
	 */
	public static String POST(String url, String storeToken,
			String accessToken, HashMap<String, Object> parameters) {
		InputStream inputStream = null;
		String result = "";

		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(url);

			// Set Request Header Token
			if (!storeToken.equals("")) {
				httpPost.setHeader("X-APPIARIES-TOKEN", storeToken);
			}

			if (!accessToken.equals("")) {
				httpPost.setHeader("Authorization",
						String.format("Bearer %s", accessToken));
			}

			if (parameters != null) {
				JSONObject holder = new JSONObject(parameters);

				StringEntity se = new StringEntity(holder.toString(),
						HTTP.UTF_8);
				se.setContentType("application/json");

				httpPost.setEntity(se);
			}

			// make POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// receive response as input stream
			if (httpResponse.getEntity() != null)
				inputStream = httpResponse.getEntity().getContent();

			// convert input stream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	/**
	 * Make a GET Request by URL and return an response content
	 * 
	 * @param url
	 * @return response content
	 */
	public static String GET(String url, String storeToken, String accessToken) {

		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet(url);

			// Set Request Header Token
			if (!TextUtils.isEmpty(storeToken)){
				httpGet.addHeader("X-APPIARIES-TOKEN", storeToken);
			}

			// Set Request Header Token
			if (!TextUtils.isEmpty(accessToken)) {
				httpGet.addHeader("Authorization",
						String.format("Bearer %s", accessToken));
			}

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpGet);

			// receive response as input stream
			inputStream = httpResponse.getEntity().getContent();

			// convert input stream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}		
	/**
	 * Get Saved Registration ID from Local Storage
	 * 
	 * @param ctx
	 * @return Saved registration Id
	 */
	public static String getRegistrationId(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);
		String registrationId = pref.getString(Constants.REGISTRATION_ID, "");

		return registrationId;
	}

	/**
	 * Saving Registration Id (Got from Google Cloud Messaging)
	 * 
	 * @param ctx
	 * @param registrationId
	 */
	public static void setRegistrationId(Context ctx, String registrationId) {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.PREFS_NAME,
				0);
		Editor editor = pref.edit();

		editor.putString(Constants.REGISTRATION_ID, registrationId);
		editor.commit();
	}
	/**
	 * API to get information
	 * 
	 * @return JSON string
	 */
	public static String getProfileAPIWithCompletion(Context ctx) {

		// Build the request URL from parameters and conditions
		String url = String.format("%s/%s/%s", Constants.PROFILE_URL_BASE,
				Constants.APIS_DATASTORE_ID, Constants.APIS_APP_ID);

		// Make a request
		return GET(url, APIHelper.getStoreToken(ctx),
				APIHelper.getAccessToken(ctx));
	}
	
}
