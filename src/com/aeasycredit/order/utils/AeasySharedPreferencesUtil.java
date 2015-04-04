package com.aeasycredit.order.utils;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class AeasySharedPreferencesUtil {
    public static final String SHARED_FILE_NAME = "aeasy";
    public static final String UUID = "uuid";
    public static final String USER_CODE = "usercode";
    
    public static void saveUuid(Context context, String uuid) {
        try {
            SharedPreferences sp = context.getSharedPreferences(SHARED_FILE_NAME, 0);
            Editor editor = sp.edit();
            editor.putString(UUID, uuid);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getUuid(Context context) {
        String uuid = null;
        try {
            SharedPreferences sp = context.getSharedPreferences(SHARED_FILE_NAME, 0);
            uuid = sp.getString(UUID, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uuid;
    }
    
    public static void saveUserCode(Context context, String usercode) {
        try {
            SharedPreferences sp = context.getSharedPreferences(SHARED_FILE_NAME, 0);
            Editor editor = sp.edit();
            editor.putString(USER_CODE, usercode);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getUserCode(Context context) {
        String uuid = null;
        try {
            SharedPreferences sp = context.getSharedPreferences(SHARED_FILE_NAME, 0);
            uuid = sp.getString(USER_CODE, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uuid;
    }
}
