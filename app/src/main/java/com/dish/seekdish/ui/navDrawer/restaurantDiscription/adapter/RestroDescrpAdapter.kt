package com.dish.seekdish.ui.navDrawer.restaurantDiscription.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.RestroDetailsFragment

import com.dish.seekdish.ui.navDrawer.restaurantDiscription.meals.MealsFragment
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.similar.RestroSimilarFragment

class RestroDescrpAdapter(fm: FragmentManager, private val mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return MealsFragment()
            }
            1 -> {
                return RestroSimilarFragment()
            }

            2 -> {
                return RestroDetailsFragment()
            }

            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}
