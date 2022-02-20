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
    static SharedPreferences sharedPreferencesLang;
    SharedPreferences.Editor editorLang;


    public static final String LANGUAGE_NAME = "LANGUAGE_NAME";
    public static final String IS_LANGUAGE_SELECTED = "IS_LANGUAGE_SELECTED";
    public static final String WALKTHROUGH = "walkthrough";
    public static final String LOGGEDIN = "loggedin";
    public static final String LOGGEDIN_THROUGH = "loggedinThrough";// 0- mannual ,,1 - fb, twitter

    public static final String REMEMBER = "remember";
    public static final String REMEMBER_EMAIL = "remember_email";
    public static final String REMEMBER_PASSWORD= "remember_password";


    // STORING THE MAIN COORDINATES=================
    public static final String LATITUDE = "lattitude";
    public static final String LONGITUDE = "longitude";
    // STORING THE MAIN COORDINATES=================

    // STORING THE CURRENT COORDINATES+++++++++++++++++++++++++++
    public static final String CURRENT_LATITUDE = "current_lattitude";
    public static final String CURRENT_LONGITUDE = "current_longitude";
    // STORING THE CURRENT COORDINATES+++++++++++++++++++++++++++

    public static final String LATITUDE_SELECTED = "lattitude_selected";
    public static final String LONGITUDE_SELECTED = "longitude_selected";
    public static final String PLACE_SELECTED = "place_selected";  // selected with aadress to show address only in textview


    public static final String LOCATION_SAVED = "location_saved";  // location saved on server
    public static final String IS_CURRENT_LOCATION_SELECTED = "current_location";  // key to select current location

    //------------------
//    public static final String LOCATION_SELECTED = "location_selected";   // selected a particular location,
    // 1- means he has selected particular location, 0 - means its current location wherever he will go it gets updated.
    /// did this so that if user move when selected current position teh location gets updated if selected then not
//-----------------------
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
    public static final String LANGUAGE_ID = "";
    public static final String RADIUS = "radius";
    public static final String LANGUAGE_HOME_ACTIVITY = "lang_home";
    public static final String LANGUAGE_CODE = "languageCode";
    public static final String IS_PRIVATE = "is_private";
    public static final String IS_GEOLOCATION = "is_geolocation";
    public static final String IS_NOTIFICATION = "is_notification";
    public static final String MEAL_MAP_QTY = "meal_map";
    public static final String RESTRO_MAP_QTY = "restro_map";


    // to keep the track of current screen to refresh after saving the filters
    public static final String CURRENT_SCREEN = "current_screen";


    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("seekdish_app", context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
// for lang
        sharedPreferencesLang = context.getSharedPreferences("seekdish_app_Lang", context.MODE_PRIVATE);
        editorLang = sharedPreferencesLang.edit();
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

    public void clearOtherSessionValue(String key) {
        editorLang.remove(key);
        editorLang.apply();
    }

    // for saving the language activity lang
    public void savesSessionLang(String key, String value) {
       /* sharedPreferencesLang = context.getSharedPreferences("seekdish_app_Lang", context.MODE_PRIVATE);
        editorLang = sharedPreferencesLang.edit();*/
        editorLang.putString(key, value);
        editorLang.commit();
    }

    public String getLangValue(String key) {
        String st_valuess = sharedPreferencesLang.getString(key, "");
        return st_valuess;
    }


}
