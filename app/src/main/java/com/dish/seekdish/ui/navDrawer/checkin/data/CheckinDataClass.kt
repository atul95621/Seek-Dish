package com.dish.seekdish.ui.navDrawer.checkin.data
import androidx.annotation.Keep

data class CheckinModel(
    val `data`: ArrayList<Data_Checkin>,
    val status: Int,
    val message:String
)

data class Data_Checkin(
    val date: String,
    val restro_id: Int,
    val restro_name: String,
    val user_image: String,
    val meal:Int,
    val meal_image:String,
    val meal_name:String
)