package com.dish.seekdish.ui.navDrawer.restaurants.dataClass



data class RestroMapModel(
    val `data`: ArrayList<Data_Restro_Map>,
    val status: Int,
    val message: String
)

data class Data_Restro_Map(
    val description: String,
    val distance: Double,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val name: String,
    val no_of_reviews: Int,
    val rating: String,
    val restaurant_image: String,
    val street:String
)