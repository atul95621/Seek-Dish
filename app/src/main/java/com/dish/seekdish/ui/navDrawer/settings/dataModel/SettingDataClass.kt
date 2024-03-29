package com.dish.seekdish.ui.navDrawer.settings.dataModel

data class SettingDataClass(
    val `data`: Data,
    val status: Int,
    val message: String
)

data class Data(
    val `private`: Int,
    val disliked_count: Int,
    val geolocation: Int,
    val liked_count: Int,
    val push_notification: Int,
    val radius: Int,
    var radius_center_location:String
)


data class SendUserGeneralSetting(
    val `data`: UserData,
    val status: Int,
    val message: String
)

data class UserData(
    val `private`: String,
    val geolocation: String,
    val push_notification: String,
    val radius: String,
    val user_id: String
)

data class LanguageData(
    val `data`: ArrayList<LangData>,
    val status: Int,
    val message:String
)

data class LangData(
    val id: Int,
    val name: String,
    val status: Int,
    val prefix: String

)

data class SaveLanguageModel(
    val `data`: Data_Lang,
    val status: Int,
    val message: String
)

data class Data_Lang(
    val message: String
)