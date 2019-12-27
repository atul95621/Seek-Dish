package com.dish.seekdish.ui.navDrawer.settings.myAlerts


data class MyAlertDataClass(
    val `data`: ArrayList<Data_Alert>,
    val status: Int
)

data class Data_Alert(
    val city: String,
    val country: String,
    val id: Int,
    val name: String,
    val restaurant_image: String,
    val street: String
)