package com.dish.seekdish.ui.navDrawer.restaurants.dataClass

/*
data class ProximityDataClass(
    var foodImageUrl: String? = null, var dishName: String? = null,var address: String? = null, var startRating: String? = null, var startReview: String? = null,var euroRating: String? = null
)
*/

data class ProximityDataClass(
    val `data`: ArrayList<Data_Proximity>,
    val status: Int,
    val message: String
)

data class Data_Proximity(
    val city: String,
    val country: String,
    val description: String,
    val distance: Double,
    val id: Int,
    val zipcode:Int,
    val latitude: String,
    val longitude: String,
    val name: String,
    val no_of_reviews: Int,
    val rating: String,
    val restaurant_image: String,
    val street: String
)