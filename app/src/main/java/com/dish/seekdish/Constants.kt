package com.dish.seekdish

import androidx.fragment.app.Fragment
import com.dish.seekdish.ui.home.fragments.HomeFragment
import com.dish.seekdish.ui.navDrawer.restaurants.RestaurantsFragment
import com.dish.seekdish.ui.navDrawer.restaurants.fragments.ProximityFragment

class Constants {
    companion object {
        // taste api constants
        var Longitude = "3.2158"
        var Latitude = "43.3442"
        var deviceType = "android"

        // extra prams
        var type = "type"
        var noOfItems = "1000"
        var noOfMapItemsMeals = "500"
        var noOfMapItemsRestro = "100"
        var noOfMeals = "30"
        var device_token = "token"
        var homePage = "1"

        var name = "Sunny"
        var mealId = "1"
        var restaurant_id = "3"
        var radius = "4" // 5km default distance

        fun refreshFragment(frag: String): Fragment {
            when (frag) {
                "HomeFragment" -> return HomeFragment()
                "RestaurantFragment" -> return RestaurantsFragment()
                else -> {
                    // Note the block
                    return HomeFragment()
                }
            }
        }

    }
}