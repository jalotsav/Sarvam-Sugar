/*
 * Copyright 2016 Jalotsav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.jalotsav.sarvamsugar.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jalotsav.sarvamsugar.Login;

/**
 * Created by JALOTSAV Dev. on 11/8/16.
 */
public class UserSessionManager {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static String PREFER_NAME;

    // All Shared Preferences Keys
    // Is User Login or not (make variable public to access from outside)
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // Username
    public static final String KEY_USERNAME = "username";

    // UserId
    public static final String KEY_USERID = "userId";

    // UserType
    public static final String KEY_USERTYPE = "userType";

    // Constructor
    public UserSessionManager(Context context) {

        this._context = context;
        PREFER_NAME = _context.getPackageName() + "_shrdprfrnc";
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    // Create login session
    // Get-Set UserName to SharedPreferences
    public String getUserName() {

        String userName = pref.getString(KEY_USERNAME, "");
        return userName;
    }

    public void setUserName(String userName) {

        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USERNAME, userName);
        editor.commit();
    }

    // Get-Set UserId to SharedPreferences
    public String getUserId() {

        String userId = pref.getString(KEY_USERID, "");
        return userId;
    }

    public void setUserId(String userId) {

        editor.putString(KEY_USERID, userId);
        editor.commit();
    }
    // Get-Set UserType to SharedPreferences
    public String getUserType() {

        String userType = pref.getString(KEY_USERTYPE, "");
        return userType;
    }

    public void setUserType(String userType) {

        editor.putString(KEY_USERTYPE, userType);
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to MainActivity
            Intent i = new Intent(_context, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            _context.startActivity(i);

            return false;
        }
        return true;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(i);
    }

    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
