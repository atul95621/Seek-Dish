package com.dish.seekdish.ui.navDrawer.notifications

data class NotifyModel(
    val `data`: ArrayList<Data_Notify>,
    val status: Int
)

data class Data_Notify(
    val date_and_time: String,
    val image: String,
    val meal_id: String,
    val notification_message: String,
    val notification_type: String,
    val restaurant_id: String,
    val invitation_status:String,
    val follower_name:String,
    val username:String,
    val user_id: Int
)