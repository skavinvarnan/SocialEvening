package com.kavin.socialevening.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kavin.socialevening.MainApplication;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 30/10/15
 * Author     : Kavin Varnan
 */
public final class SharedPreferenceUtils {

    private static SharedPreferences sharedPreferences;

    /**
     * Initialize the SharedPreference object with the default preference xml of the application
     */
    private static void initializeSharedPreference() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainApplication.getGlobalContext());
    }

    /**
     * Update the SharedPreference with a String value
     * @param key key of the preference. Note: Only use Key from Constants.SP class
     * @param value value in String
     * @return Returns true if the new values were successfully written to persistent storage.
     */
    public static boolean updateSharedPreference(String key, String value) {
        if (sharedPreferences == null) {
            initializeSharedPreference();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * Update the SharedPreference with a Integer value
     * @param key key of the preference. Note: Only use Key from Constants.SP class
     * @param value value in Integer
     * @return Returns true if the new values were successfully written to persistent storage.
     */
    public static boolean updateSharedPreference(String key, int value) {
        if (sharedPreferences == null) {
            initializeSharedPreference();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * Update the SharedPreference with a boolean value
     * @param key key of the preference. Note: Only use Key from Constants.SP class
     * @param value value in boolean
     * @return Returns true if the new values were successfully written to persistent storage.
     */
    public static boolean updateSharedPreference(String key, boolean value) {
        if (sharedPreferences == null) {
            initializeSharedPreference();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * Get the String value of the respected SharedPreference key
     * @param key key of the preference. Note: Only use Key from Constants.SP class
     * @return Returns the String value
     */
    public static String getSharedPreferenceValue(String key) {
        if (sharedPreferences == null) {
            initializeSharedPreference();
        }

        if (key != null) {
            return sharedPreferences.getString(key, null);
        } else {
            throw new NullPointerException("SharedPreference key cannot be null");
        }
    }

    /**
     * Get the Integer value of the respected SharedPreference key
     * @param key key of the preference. Note: Only use Key from Constants.SP class
     * @return Returns the Integer value
     */
    public static int getSharedPreferenceValueInt(String key) {
        if (sharedPreferences == null) {
            initializeSharedPreference();
        }

        if (key != null) {
            return sharedPreferences.getInt(key, 0);
        } else {
            throw new NullPointerException("SharedPreference key cannot be null");
        }
    }

    /**
     * Get the boolean value of the respected SharedPreference key
     * @param key key of the preference. Note: Only use Key from Constants.SP class
     * @return Returns the boolean value
     */
    public static boolean getSharedPreferenceBoolean(String key) {
        if (sharedPreferences == null) {
            initializeSharedPreference();
        }
        if (key != null) {
            return sharedPreferences.getBoolean(key, false);
        } else {
            throw new NullPointerException("SharedPreference key cannot be null");
        }
    }


    public static SharedPreferences getSharedPreference() {
        if (sharedPreferences == null) {
            initializeSharedPreference();
        }
        return sharedPreferences;
    }
}
