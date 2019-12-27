package com.dish.seekdish.ui.navDrawer.dishDescription.model

import java.io.Serializable


data class DishDescpModel(
    val `data`: Data,
    val status: Int
)

data class Data(
    val Ingredients: Ingredients,
    val meals: Meals,
    val similar_meals: List<SimilarMeal>,
    val user_meal_comments: List<UserMealComment>
)

data class Meals(
    val budget: String,
    val calories: Int,
    val distance: Double,
    val latitude: String,
    val longitude: String,
    val meal_avg_rating: String,
    val meal_id: Int,
    val meal_image: String,
    val meal_name: String,
    val meal_people: String,
    val meal_status: List<String>,
    val meal_type: String,
    val preperation_time: String,
    val restro_id: Int,
    val restro_name: String,
    val seasons: List<String>
) : Serializable

data class Ingredients(
    val Elements_Ao: List<String>,
    val Fat_Appellation_Of_Controlled_Origin_Aop: List<String>,
    val Fats: List<String>,
    val Main_Ingredients: ArrayList<String>,
    val Secondary_Ingredients: List<String>,
    val Type_of_Cooking: List<String>,
    val alcohol: List<String>,
    val intolerance_compatibilities: List<String>,
    val meal_tags: List<String>,
    val restaurant_tags: List<String>,
    val seasoning: List<String>
) : Serializable

data class SimilarMeal(
    val budget_rating: Int,
    val distance: Double,
    val latitude: String,
    val longitude: String,
    val meal_avg_rating: String,
    val meal_id: Int,
    val meal_image: String,
    val name: String,
    val no_of_reviews: Int,
    val restro_id: Int,
    val restro_name: String
)

data class UserMealComment(
    val `private`: Int,
    val ambiance_rating: Int,
    val anonymous: Int,
    val budget: Int,
    val cleanness_rating: Int,
    val comment: String,
    val rating_image1: String,
    val rating_image2: String,
    val decore_rating: Int,
    val follower: Int,
    val friend: Int,
    val meal_avg_rating: String,
    val meal_id: Int,
    val meal_image: String,
    val name: String,
    val odor_rating: Int,
    val presentation_rating: Int,
    val published_on: String,
    val restro_id: Int,
    val service_rating: Int,
    val taste_rating: Int,
    val texture_rating: Int,
    val user_id: Int,
    val username: String
)

data class AddTodoModel(
    val `data`: Data_TOdo,
    val status: Int
)

data class Data_TOdo(
    val message: String
)
