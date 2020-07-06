package com.dish.seekdish.ui.navDrawer.restaurants.dataClass



data class TimeRestroDataClass(
    val `data`: ArrayList<Data_Time_Restro>,
    val status: Int,
    val message: String
)

data class Data_Time_Restro(
    val city: String,
    val country: String,
    val description: String,
    val distance: Double,
    val id: Int,
    val zipcode:String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val no_of_reviews: Int,
    val rating: String,
    val restaurant_image: String,
    val service_speed: Int,
    val street: String
)