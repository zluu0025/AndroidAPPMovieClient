package com.example.movieclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SP {

    private static final String DBName = "movie";


    private static SP instance = null;
    private SharedPreferences sp;

    public SP(Context context) {
        sp = context.getSharedPreferences(DBName, Context.MODE_PRIVATE);
        instance = this;
    }

    public static SP getInstance(Context context) {
        if (instance == null) return new SP(context);
        return instance;
    }

    public SharedPreferences getSP() {
        return sp;
    }


    public String getString(String key) {
        return sp.getString("String-" + key, null);
    }


    public String getString(String key, String defValue) {
        return sp.getString("String-" + key, defValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        if (value==null){
            value="";
        }
        editor.putString("String-" + key, value);
        editor.commit();
    }


    public Boolean getBoolean(String key) {
        return sp.getBoolean("Boolean-" + key, false);
    }

    public Boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean("Boolean-" + key, defValue);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("Boolean-" + key, value);
        editor.commit();
    }

    public Float getFloat(String key) {
        return sp.getFloat("Float-" + key, 0f);
    }

    public void putFloat(String key, Float value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("Float-" + key, value);
        editor.commit();
    }

    public Integer getInt(String key, int defaultValue) {
        return sp.getInt("Int-" + key, defaultValue);
    }

    public Integer getInt(String key) {
        return getInt(key, 0);
    }

    public void putInt(String key, Integer value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("Int-" + key, value);
        editor.commit();
    }

    public Long getLong(String key) {
        return sp.getLong("Long-" + key, 0);
    }

    public void putLong(String key, Long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("Long-" + key, value);
        editor.commit();
    }






}