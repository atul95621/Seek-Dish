package com.dish.seekdish.ui.navDrawer.myFriends

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.myFriends.contactFetch.ContactFetchActivity
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FollowerFragment
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FollowingFragment
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FriendsFragment
import com.dish.seekdish.util.BaseFragment
import com.dish.seekdish.util.SessionManager
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

        homeActivity = activity as HomeActivity
        sessionManager = SessionManager(homeActivity)

        // setting up tabLayout
        this.tabLayout = view.findViewById(R.id.tabLayout)

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.friendsss)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.followers)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.following)))

        //setting initialfragment...
        replaceFragment(FriendsFragment(userId))

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

        // hiding filter when friends are to shown
        homeActivity.imgFilters.visibility = View.GONE
        if (!userId.equals(sessionManager.getValue(SessionManager.USER_ID))) {
            homeActivity.hideHamburgerIcon()
        }
//        homeActivity.imgFilters.visibility = View.GONE

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
