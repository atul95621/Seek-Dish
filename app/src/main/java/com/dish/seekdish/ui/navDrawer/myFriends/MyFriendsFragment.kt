package com.dish.seekdish.ui.navDrawer.myFriends

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.myFriends.adapter.MyFriendsAdapter
import com.dish.seekdish.ui.navDrawer.myFriends.contactFetch.ContactFetchActivity
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FollowerFragment
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FollowingFragment
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FriendsFragment
import com.google.android.material.tabs.TabLayout


class MyFriendsFragment(var userId: String) : BaseFragment() {
    lateinit var tabLayout: TabLayout
//    internal lateinit var viewPager: ViewPager
//    internal lateinit var adapter: MyFriendsAdapter
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_friends, container, false)

//        Log.e("uerssss","::"+userId)

        homeActivity = activity as HomeActivity

        // setting up tabLayout
        this.tabLayout = view.findViewById(R.id.tabLayout)

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.friendsss)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.followers)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.following)))

        //setting initialfragment...
        replaceFragment(FriendsFragment(userId))


//        //change font
//        changeTabsFont();

      /*  viewPager = view.findViewById(R.id.pager) as ViewPager
        adapter = MyFriendsAdapter(activity!!.supportFragmentManager, tabLayout.tabCount,userId)


        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))*/


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

        homeActivity.tvAdd.setOnClickListener()
        {
            val intent = Intent(context, ContactFetchActivity::class.java)
            startActivity(intent)
        }


        return view
    }
    fun setCurrentTabFragment(tabPosition: Int) {
        when (tabPosition) {
            0 -> replaceFragment(FriendsFragment(userId))
            1 -> replaceFragment(FollowerFragment(userId))
            2 -> replaceFragment(FollowingFragment(userId))
        }
    }
    fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction
        transaction = childFragmentManager.beginTransaction()
        transaction.replace(com.dish.seekdish.R.id.content_friends_fragment, fragment)
        transaction.commit()
    }

}
