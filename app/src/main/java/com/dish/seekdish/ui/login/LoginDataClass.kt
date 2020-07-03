package com.dish.seekdish.ui.login

data class LoginDataClass(
    val `data`: Data,
    val status: Int,
    val message:String
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

data class CheckUpdateModel(
    val Android_version: String,
    val Ios_version: String,
    val message: String,
    val status: Int
)