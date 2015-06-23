//
// Copyright (c) 2015 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.common;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("unused")
public class PreferenceHelper {

    /* Local Preferences Storage */
    public static final String PREFS_NAME = "PUZZPrefsFile";

    public static final String PREF_KEY_STORE_TOKEN = "PUZZAccessToken";
    public static final String PREF_KEY_PLAYER_ID   = "PUZZUserID";
    public static final String PREF_KEY_NICKNAME    = "PUZZNickname";

    public static final String LOGIN_FLAG = "PUZZLoginFlag";
    public static final String ACCESS_TOKEN_KEY = "PUZZAccessToken";
    public static final String STORE_TOKEN_KEY = "PUZZStoreToken";
    public static final String TOKEN_EXPIRE_KEY = "PUZZTokenExpire";

    public static final String PREF_KEY_STAGE_ID = "PUZZStageID";
    public static final String PREF_KEY_STAGE_NAME = "PUZZStageName";

    public static String loadUserToken(Context context) {
        SharedPreferences pref = getPreference(context);
        return pref.getString(PreferenceHelper.PREF_KEY_STORE_TOKEN, "");
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences pref = getPreference(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PreferenceHelper.PREF_KEY_STORE_TOKEN, token);
        editor.apply();
    }

    public static boolean loadIsAlreadyRegistered(Context context) {
        SharedPreferences pref = getPreference(context);
        String accessToken = pref.getString(PreferenceHelper.PREF_KEY_STORE_TOKEN, null);
        return (accessToken != null);
    }

    public static String loadNickname(Context context) {
        SharedPreferences pref = getPreference(context);
        return pref.getString(PreferenceHelper.PREF_KEY_NICKNAME, null);
    }

    public static void saveNickname(Context context, String nickname) {
        SharedPreferences pref = getPreference(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PreferenceHelper.PREF_KEY_NICKNAME, nickname);
        editor.apply();
    }

    public static String loadPlayerId(Context context) {
        SharedPreferences pref = getPreference(context);
        return pref.getString(PreferenceHelper.PREF_KEY_PLAYER_ID, null);
    }

    public static void savePlayerId(Context context, String playerId) {
        SharedPreferences pref = getPreference(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PreferenceHelper.PREF_KEY_PLAYER_ID, playerId);
        editor.apply();
    }

    public static String loadStageId(Context context) {
        SharedPreferences pref = getPreference(context);
        return pref.getString(PreferenceHelper.PREF_KEY_STAGE_ID, null);
    }

    public static void saveStageId(Context context, String stageId) {
        SharedPreferences pref = getPreference(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PreferenceHelper.PREF_KEY_STAGE_ID, stageId);
        editor.apply();
    }

    public static String loadStageName(Context context) {
        SharedPreferences pref = getPreference(context);
        return pref.getString(PreferenceHelper.PREF_KEY_STAGE_NAME, null);
    }

    public static void saveStageName(Context context, String stageName) {
        SharedPreferences pref = getPreference(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PreferenceHelper.PREF_KEY_STAGE_NAME, stageName);
        editor.apply();
    }

    public static String loadString(Context context, String key) {
        SharedPreferences pref = getPreference(context);
        return pref.getString(key, "");
    }

    public static void saveString(Context context, String key, String value){
        SharedPreferences pref = getPreference(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(PreferenceHelper.PREFS_NAME, 0);
    }

}
