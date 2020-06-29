package com.dish.seekdish.ui.navDrawer.myFriends.dataModel

data class FriendFragDataClass (

    var friendImageURL: String? = null, var friendName: String? = null

)
data class FriendDataModel(
    val `data`: Data,
    val status: Int,
    val message: String
)

data class Data(
    val followers: ArrayList<Follower>,
    val followings: ArrayList<Following>,
    val friends: ArrayList<Friend>
)

data class Follower(
    val phone: String,
    val user_id: Int,
    val user_image: String,
    val username: String,
    val already_friend:Int,
    val already_follower:Int
)

data class Friend(
    val phone: String,
    val user_id: Int,
    val user_image: String,
    val username: String,
    var friendSelected:Boolean= false,
    val already_friend:Int,
    val already_follower:Int
)

data class Following(
    val phone: String,
    val user_id: Int,
    val user_image: String,
    val username: String,
    val already_friend:Int,
    val already_follower:Int
)