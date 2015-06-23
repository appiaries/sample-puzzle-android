//
// Copyright (c) 2015 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.common;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class Validator {

    private static Pattern sValidEmailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    public static boolean isValidEmail(String email) {
        return sValidEmailPattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return isValidPassword(password, false);
    }
    public static boolean isValidPassword(String password, boolean required) {
        if (required && !isNotEmpty(password)) return false;

        String pw = password.trim();
        int len = pw.length();
        return Constants.PASSWORD_MIN_LENGTH <= len && len <= Constants.PASSWORD_MAX_LENGTH;
    }

    public static boolean isNotEmpty(String string) {
        return !TextUtils.isEmpty(string);
    }

}
