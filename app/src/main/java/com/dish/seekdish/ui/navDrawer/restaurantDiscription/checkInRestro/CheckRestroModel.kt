package com.dish.seekdish.ui.navDrawer.restaurantDiscription.checkInRestro



data class CheckinRestroModel(
    val `data`: Data,
    val status: Int
)

data class Data(
    val registered_meals: ArrayList<RegisteredMeal>,
    val todo_meals: ArrayList<TodoMeal>
)

data class RegisteredMeal(
    val meal_id: Int,
    val meal_image: String,
    val meal_name: String,
    val restro_id: Int
)

data class TodoMeal(
    val meal_id: Int,
    val meal_image: String,
    val meal_name: String,
    val restro_id: Int,
    val restro_name: String
)