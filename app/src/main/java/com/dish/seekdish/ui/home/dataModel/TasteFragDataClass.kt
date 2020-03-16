package com.dish.seekdish.ui.home.dataModel

// data classcreated replacing the getter and setter  model class.
/*
data class TasteFragDataClass(
    var foodImageUrl: String? = null,
    var dishName: String? = null,
    var distance: String? = null,
    var startRating: String? = null,
    var startReview: String? = null,
    var euroRating: String? = null
)
*/

data class TasteFragDataClass(
    val `data`: ArrayList<Data_Taste>,
    val status: Int,
    val message: String
)

data class Data_Taste(
    val budget: Int,
    val description: String,
    val distance: Double,
    val latitude: String,
    val longitude: String,
    val meal_image: String,
    val name: String,
    val no_of_reviews: Int,
    val rating: Int,
    val meal_id: Int,
    val restro_id: Int

)

data class Location(
    val `data`: Data,
    val status: Int,
    val message: String
) {
    data class Data(
        val latitude: String,
        val longitude: String,
        val user_id: String
    )
}
