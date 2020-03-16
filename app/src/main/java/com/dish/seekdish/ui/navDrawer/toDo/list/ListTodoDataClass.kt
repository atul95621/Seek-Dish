package com.dish.seekdish.ui.navDrawer.toDo.list

/*
data class ListTodoDataClass(
    var foodImageUrl: String? = null, var dishName: String? = null,var distance: String? = null, var startRating: String? = null, var startReview: String? = null,var euroRating: String? = null
)
*/
data class ListTodoDataClass(
    val `data`: ArrayList<Data_todo>,
    val status: Int,
    val message: String
)

data class Data_todo(
    val budget_rating: Int,
    val latitude: String,
    val longitude: String,
    val meal_avg_rating: String,
    val meal_id: Int,
    val meal_image: String,
    val name: String,
    val no_of_reviews: Int,
    val restro_id: Int,
    val restro_name: String,
    val distance: Double
)


data class DeleteTodoList(
    val `data`: Data_Delete_TOdo,
    val status: Int,
    val message: String
)

data class Data_Delete_TOdo(
    val message: String
)