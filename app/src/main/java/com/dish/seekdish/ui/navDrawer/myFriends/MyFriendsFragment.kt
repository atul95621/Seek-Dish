package com.dish.seekdish.ui.navDrawer.myFriends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.myFriends.adapter.MyFriendsAdapter
import com.google.android.material.tabs.TabLayout


class MyFriendsFragment : BaseFragment() {
    lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var adapter: MyFriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_friends, container, false)


        // setting up tabLayout
        this.tabLayout = view.findViewById(R.id.tabLayout)

        tabLayout.addTab(tabLayout.newTab().setText("Friends"))
        tabLayout.addTab(tabLayout.newTab().setText("Followers"))
        tabLayout.addTab(tabLayout.newTab().setText("Following"))

//        //change font
//        changeTabsFont();

        viewPager = view.findViewById(R.id.pager) as ViewPager
        adapter = MyFriendsAdapter(activity!!.supportFragmentManager, tabLayout.tabCount
        )


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
