package com.dish.seekdish.ui.navDrawer.restaurants.mapWindow

data class InfoWindowModel(
    var imageUrl: String? = null,
    var starRating: String? = null,
    var restroTitle: String? = null,
    var address: String,
    var restro_id: Int
)