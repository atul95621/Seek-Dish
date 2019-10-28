package com.dish.seekdish.ui.login

data class LoginDataClass(
    val `data`: Data,
    val status: Int
)

data class Data(
    val bio: String,
    val country_code: Int,
    val email: String,
    val fcm_token: String,
    val first_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val phone: String,
    val photo: String,
    val username: String
)