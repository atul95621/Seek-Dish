package com.dish.seekdish.ui.navDrawer.restaurantDiscription.meals

data class MealsDataClass(
    var foodImageUrl: String? = null,
    var dishName: String? = null,
    var distance: String? = null,
    var startRating: String? = null,
    var startReview: String? = null,
    var euroRating: String? = null,
   var mealId: String,
    var restroId: String
)