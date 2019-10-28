package com.dish.seekdish.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by SFS-009 on 3/19/2016.
 */
public class SessionManager {

    public static final String FCM_TOKEN = "fcm_token";
    static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String LANGUAGE_NAME= "LANGUAGE_NAME";
    public static final String IS_LANGUAGE_SELECTED = "IS_LANGUAGE_SELECTED";
    public static final String WALKTHROUGH = "walkthrough";
    public static final String LOGGEDIN = "loggedin";
    public static final String LATITUDE="lattitude";
    public static final String LONGITUDE="longitude";
    public static final String CURRENT_LATITUDE="current_lattitude";
    public static final String CURRENT_LONGITUDE="current_longitude";
    public static final String LATITUDE_SELECTED="lattitude_selected";
    public static final String LONGITUDE_SELECTED="longitude_selected";
    public static final String PLACE_SELECTED="place_selected";

    public static final String LOCATION_STATUS = "location";
    public static final String USERNAME = "name";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String GENDER = "gender";
    public static final String COUNTRY_CODE = "country_code";
    public static final String PHONE = "phone";
    public static final String PHOTO_URL = "photo_url";
    public static final String EMAIL = "email";
    public static final String USER_ID = "user_id";
    public static final String BIO = "bio";
    public static final String LANGUAGE_ID = "languageId";
    public static final String RADIUS = "radius";



    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("seekdish_app", context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setValues(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key) {
        String st_value = sharedPreferences.getString(key, "");
        return st_value;
    }

    public void clearValues() {
        editor.clear();
        editor.commit();
    }


    public void clearCetainValue(String key) {
        editor.remove(key);
        editor.apply();
    }

}
