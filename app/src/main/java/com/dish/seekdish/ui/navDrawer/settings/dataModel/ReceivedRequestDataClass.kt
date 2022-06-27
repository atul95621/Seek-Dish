package com.dish.seekdish.ui.navDrawer.settings.dataModel


data class ReceivedRequestDataClass(
    val `data`: ArrayList<Data_Req>,
    val status: Int,
    val message: String
)

data class Data_Req(
    val phone: String,
    val user_id: Int,
    val user_image: String,
    val username: String
)

data class CancelReModel(
//    val `data`: Cancel_Req,
    val status: Int,
    val message: String
)

data class Cancel_Req(
    val message: String
)



data class RemoveUserModel(
    val status: Int,
    val message: String,
    val data: DataRemove
)

data class DataRemove(
    val message: String
)

