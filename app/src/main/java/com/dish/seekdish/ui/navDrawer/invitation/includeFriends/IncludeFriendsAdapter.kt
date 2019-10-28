package com.dish.seekdish.ui.navDrawer.invitation.includeFriends

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.friends.FriendsInclFragment
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.selected.SelectedInclFragment

class IncludeFriendsAdapter(fm: FragmentManager, private val mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return FriendsInclFragment()
            }
            1 -> {
                return SelectedInclFragment()
            }


            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}