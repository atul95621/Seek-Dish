package com.dish.seekdish.ui.navDrawer.restaurants

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.myFavourite.adapter.MyFavouriteFragAdapter
import com.dish.seekdish.ui.navDrawer.restaurants.adapter.RestaurantAdapter
import com.dish.seekdish.ui.navDrawer.restaurants.fragments.ProximityFragment
import com.dish.seekdish.ui.navDrawer.restaurants.fragments.RestroMapFragment
import com.dish.seekdish.ui.navDrawer.restaurants.fragments.TimeRestroFragment
import com.google.android.material.tabs.TabLayout

class RestaurantsFragment : Fragment() {

    lateinit var tabLayout: TabLayout
//    internal lateinit var viewPager: ViewPager
    //    internal lateinit var adapter: RestaurantAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restaurants, container, false)

        this.tabLayout = view.findViewById(R.id.tabLayoutRestroFrag)

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.proximity)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.time)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.map)))

        //setting initials
        replaceFragment(ProximityFragment())


//        //change font
//        changeTabsFont();

        /*  viewPager = view.findViewById(R.id.viewPagerRestroFrag) as ViewPager
          adapter = RestaurantAdapter(activity!!.supportFragmentManager, tabLayout.tabCount)


          viewPager.adapter = adapter
          viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
  */

//        tabLayout.setTabTextColors(
//                ContextCompat.getColor(this, R.color.black),
//                ContextCompat.getColor(this, R.color.black)


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager.currentItem = tab.position
                setCurrentTabFragment(tab.getPosition());

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        return view
    }


    fun setCurrentTabFragment(tabPosition: Int) {
        when (tabPosition) {
            0 -> replaceFragment(ProximityFragment())
            1 -> replaceFragment(TimeRestroFragment())
            2 -> replaceFragment(RestroMapFragment())
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction
        transaction = childFragmentManager.beginTransaction()
        transaction.replace(com.dish.seekdish.R.id.content_restro_fragment, fragment)
        transaction.commit()

    }


}
