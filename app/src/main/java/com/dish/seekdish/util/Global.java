package com.dish.seekdish.util;

import java.util.ArrayList;
import java.util.HashSet;

public class Global {

    public static final String BASE_URL = "http://seekdish.com/seekdish_android/public/api/v1/";


    //required for FCM
    public static final String CHANNEL_ID = "my_channel_seekdish";
    public static final String CHANNEL_NAME = "SEEKDISH";
    public static final String CHANNEL_DESCRIPTION = "seekdish";

    public static HashSet<String> likedItemsSet= new HashSet<>();
    public static HashSet<String> dislikedItemsSet= new HashSet<>();

    public static HashSet<String> budgetSet= new HashSet<String>();
    public static HashSet<String> serviceSet= new HashSet<String>();
    public static HashSet<String> mealSet= new HashSet<String>();
    public static HashSet<String> compatIntSet= new HashSet<String>();
    public static HashSet<String> restroSpeclSet= new HashSet<String>();
    public static HashSet<String> restroAmbiSet= new HashSet<String>();
    public static HashSet<String> compAmbianceSet= new HashSet<String>();
    public static HashSet<String> additonalSet= new HashSet<String>();
    public static HashSet<String> seasonlitySet= new HashSet<String>();




}
