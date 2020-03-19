package com.dish.seekdish.ui.navDrawer.settings.myAlerts


data class MyAlertDataClass(
    val `data`: ArrayList<Data_Alert>,
    val status: Int,
    val message: String
)

data class Data_Alert(
    val city: String,
    val country: String,
    val id: Int,
    val name: String,
    val restaurant_image: String,
    val street: String,
    val zipcode:Int
)

//-----------------------------------

data class InvitationModel(
    val `data`: Data,
    val status: Int,
    val message:String
)

data class Data(
    val details_tab_arr: DetailsTabArr,
    val id: Int,
    val invited_tab_array: ArrayList<InvitedTabArray>,
    val map_tab: MapTab,
    val name: String,
    val restaurant_image: String,
    val setting_invitation: SettingInvitation,
    val street: String
)

data class DetailsTabArr(
    val email: String,
    val name: String,
    val phone: String
)

data class InvitedTabArray(
    val invitation_status: String,
    val user_image: String,
    val username: String
)

data class MapTab(
    val latitude: String,
    val longitude: String
)

data class SettingInvitation(
    val allow_invitation: Int,
    val validity_of_invitation: Int
)