package com.dish.seekdish.ui.navDrawer.restaurantDiscription

import java.io.Serializable


data class RestroDescpModel(
    val `data`: Data,
    val status: Int,
    val message: String
)

data class Data(
    val restaurant: Restaurant
)

data class Restaurant(
    val city: String,
    val country: String,
    val phone: String,
    var pdf_url:String,
    val country_code: Int,
    val description: String,
    val distance: Double,
    val id: Int,
    val zipcode: String,
    val latitude: String,
    val longitude: String,
    val meals: List<Meal>,
    val name: String,
    val facebook: String,
    val twitter: String,
    val no_of_reviews: Int,
    val rating: String,
    val restaurant_detail: RestaurantDetail,
    val restaurant_image: List<String>,
    val service_speed: String?,
    val similar_restaurants: ArrayList<SimilarRestaurant>,
    val street: String
) : Serializable

data class Meal(
    val budget: Int,
    val distance: Double,
    val latitude: String,
    val longitude: String,
    val meal_avg_rating: String,
    val meal_id: Int,
    val meal_image: String,
    val meal_name: String,
    val no_of_reviews: Int,
    val restro_id: Int,
    val restro_name: String,
    val meal_price: String,
    val meal_symbol: String,
    val meal_type: String
) : Serializable

data class RestaurantDetail(
    val additional_services: List<String>,
    val detail: List<Detail>,
    val restaurant_ambiance: List<String>,
    val restaurant_ambiance_complementary: List<String>,
    val specialities: List<String>
)

data class Detail(
    val email: String?,
    val guests: String?,
    val id: Int?,
    val name: String?,
    val origin: String?,
    val phone: String?,
    val schedule: String?,
    val website: String?
) : Serializable

data class SimilarRestaurant(
    val city: String,
    val country: String,
    val description: String,
    val email: String,
    val guests: String,
    val id: Int,
    val zipcode: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val no_of_reviews: Int,
    val origin: String,
    val phone: String,
    val rating: String,
    val restaurant_image: String,
    val schedule: String,
    val service_speed: String,
    val street: String,
    val website: String,
    val distance: String
) : Serializable
