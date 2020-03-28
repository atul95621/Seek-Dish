package com.dish.seekdish.ui.navDrawer.activities.model


data class ProfileDataClass(
    val `data`: Data,
    val status: Int,
    val message: String
)

data class Data(
    val address_line1: String,
    val address_line2: String,
    val bio: String,
    val birth_date: String,
    val body_fat: String,
    val city: String,
    val city_id: String,
    val country: String,
    val country_id: String,
    val email: String,
    val facebook_id: String,
    val first_name: String,
    val gender: String,
    val height: String,
    val last_name: String,
    val phone: String,
    val photo: String,
    val twitter_id: String,
    val username: String,
    val weight: String,
    val zip_code: String,
    val new_info_data: NewInfoData
)
data class NewInfoData(
    val checkin: Int,
    val dislike: Int,
    val followers: Int,
    val friends: Int,
    val like: Int,
    val profile_percentage: String
)


data class ContactModel(
    val `data`: List<Data_Contact>,
    val status: Int,
    val message:String
)

data class Data_Contact(
    val callingcode: Int?,
    val email: String?,
    val facebook_id: String?,
    val phone: String?,
    val skype_id: String?,
    val twitter_id: String?
)