package com.dish.seekdish.ui.navDrawer.dishDescription.opinion

data class OpinionDataClass(
    var opinionTitle: String? = null,
    var opinionDate: String? = null,
    var imageUrl: String? = null,
    var comment: String? = null,
    var anononymous: Int? = null
)

data class CommentDetailModel(
    val message: String,
    val response: Response,
    val status: Int
)

data class Response(
    val `data`: Data,
    val follower: Int,
    val friend: Int,
    val `private`: Int,
    val rating: String
)

data class Data(
    val ambiance_rating: Int,
    val anonymous: Int,
    val cleanness_rating: Int,
    val comment: String,
    val created_at: String,
    val decore_rating: Int,
    val deleted: Int,
    val id: Int,
    val image1: String,
    val image2: String,
    val meal: Int,
    val meal_image: String,
    val meal_name: String,
    val odor_rating: Int,
    val presentation_rating: Int,
    val published_on: String,
    val restaurant: Int,
    val reviewed_on: String,
    val service_rating: Int,
    val taste_rating: Int,
    val texture_rating: Int,
    val updated_at: String,
    val user: Int,
    val viewed: Int,
    val username: String,
    val budget: Int
)