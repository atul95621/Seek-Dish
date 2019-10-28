package com.dish.seekdish.ui.navDrawer.settings.dataModel

data class LikeDataClass(
    val `data`: ArrayList<Data_Liked>,
    val status: Int
)

data class Data_Liked(
    val id: Int,
    val liked: Int,
    val name: String,
    var checkForLike: Boolean = false
)


data class DisLikeDataClass(
    val `data`: ArrayList<Data_Disliked>,
    val status: Int
)

data class Data_Disliked(
    val id: Int,
    val liked: Int,
    val name: String,
    var checkForDisLike: Boolean= false
)

data class LikedIngredientsSaved(
    val `data`: DataResult,
    val status: Int
)

data class DataResult(
    val message: String
)