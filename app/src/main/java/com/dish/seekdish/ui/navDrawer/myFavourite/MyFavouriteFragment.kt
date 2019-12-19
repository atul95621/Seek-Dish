package com.dish.seekdish.ui.navDrawer.myFavourite

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.adapter.HomeAdapter
import com.dish.seekdish.ui.navDrawer.myFavourite.adapter.MyFavouriteFragAdapter
import com.google.android.material.tabs.TabLayout


class MyFavouriteFragment : Fragment() {
    lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var adapter: MyFavouriteFragAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_my_favourite, container, false)

        // setting up tabLayout
        this.tabLayout = view.findViewById(R.id.tabLayoutFavouriteFrag)

        tabLayout.addTab(tabLayout.newTab().setText("List"))
        tabLayout.addTab(tabLayout.newTab().setText("Map"))

//        //change font
//        changeTabsFont();

        viewPager = view.findViewById(R.id.viewPagerMyFavouriteFrag) as ViewPager
        adapter = MyFavouriteFragAdapter(activity!!.supportFragmentManager, tabLayout.tabCount)


        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))


//        tabLayout.setTabTextColors(
//                ContextCompat.getColor(this, R.color.black),
//                ContextCompat.getColor(this, R.color.black)


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        return view
    }

}