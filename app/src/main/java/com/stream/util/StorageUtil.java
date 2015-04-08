package com.stream.util;

import android.content.SharedPreferences;

import java.util.Set;

public final class StorageUtil {

    private static final SharedPreferences sharedPreferences = ActivityUtil.getMainActivity().getSharedPreferences("SeeSetup", 0);
    private static final SharedPreferences.Editor editor = sharedPreferences.edit();

    public enum SharedPreferenceKeys{

        ChannelCreated("channel_created"), Registered("registered");

        private String key;

        SharedPreferenceKeys(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }

    }

    public static boolean setBooleanValue(String key, boolean value){
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBooleanValue(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public static boolean setStringValue(String key, String value){
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getStringValue(String key){
        return sharedPreferences.getString(key, null);
    }

    public static boolean setStringValues(String key, Set<String> values){
        editor.putStringSet(key, values);
        return editor.commit();
    }

    public static Set<String> getStringValues(String key){
        return sharedPreferences.getStringSet(key, null);
    }
}
