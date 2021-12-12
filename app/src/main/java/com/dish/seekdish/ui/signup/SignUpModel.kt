package com.dish.seekdish.ui.signup

data class SignUpModel(
    val `data`: Data,
    val status: Int,
    val message: String
    )

data class Data(
    val bio: String,
    val country: String,
    val email: String,
    val fcm_token: String,
    val first_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val phone: String,
    val photo: String,
    val username: String,
    val message:String,
    val radius:Int,
    val latitude:String,
    val longitude:String
)