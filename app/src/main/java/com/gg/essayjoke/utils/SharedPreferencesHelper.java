package com.gg.essayjoke.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String SHARED_PATH = "shared";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public static SharedPreferencesHelper getInstance() {
        return SharedPreferencesHelperHolder.mInstance;
    }

    private static class SharedPreferencesHelperHolder {
        private static SharedPreferencesHelper mInstance = new SharedPreferencesHelper();
    }

    private SharedPreferencesHelper() {
    }


    public SharedPreferencesHelper init(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
        editor = sp.edit();
        return this;
    }

    public long getLongValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getLong(key, 0);
        }
        return 0;
    }

    public String getStringValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getString(key, null);
        }
        return null;
    }

    public int getIntValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public int getIntValueByDefault(String key) {
        if (key != null && !key.equals("")) {
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public boolean getBooleanValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getBoolean(key, false);
        }
        return true;
    }

    public float getFloatValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getFloat(key, 0);
        }
        return 0;
    }

    public void putStringValue(String key, String value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public void putIntValue(String key, int value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public void putBooleanValue(String key, boolean value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public void putLongValue(String key, long value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    public void putFloatValue(String key, Float value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putFloat(key, value);
            editor.apply();
        }
    }

    public void remove(String key) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public void clear() {
        if (editor == null)
            editor = sp.edit();
        editor.clear().apply();
    }
} 