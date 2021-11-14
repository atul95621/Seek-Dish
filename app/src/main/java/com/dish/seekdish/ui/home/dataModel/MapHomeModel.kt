package com.dish.seekdish.ui.home.dataModel

data class MapHomeModel(
    val `data`: ArrayList<Data_Home_Map>,
    val status: Int,
    val message: String
)

data class Data_Home_Map(
    val budget: Int,
    val meal_id: Int,
    val latitude: String,
    val longitude: String,
    val meal_image: String,
    val name: String,
    val no_of_reviews: Int,
    val rating: Int,
    val restro_id: Int,
    val meal_price: String? = "null",
    val meal_symbol: String
)