package com.dish.seekdish.ui.navDrawer.restaurantDiscription.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescpModel
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.RestroDetailsFragment

import com.dish.seekdish.ui.navDrawer.restaurantDiscription.meals.MealsFragment
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.similar.RestroSimilarFragment

class RestroDescrpAdapter(
    fm: FragmentManager,
    private val mNumOfTabs: Int,
    var response: RestroDescpModel
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return MealsFragment(response)
            }
            1 -> {
                return RestroSimilarFragment(response)
            }

            2 -> {
                return RestroDetailsFragment(response)
            }

            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}
