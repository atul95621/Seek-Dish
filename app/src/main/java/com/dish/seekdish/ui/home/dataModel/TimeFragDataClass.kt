package com.dish.seekdish.ui.home.dataModel



data class TimeFragDataClass(
    val `data`: ArrayList<Data_time>,
    val status: Int
)

data class Data_time(
    val budget: Int,
    val description: String,
    val distance: Double,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val meal_image: String,
    val name: String,
    val no_of_reviews: Int,
    val preperation_time: Int,
    val rating: Int
)