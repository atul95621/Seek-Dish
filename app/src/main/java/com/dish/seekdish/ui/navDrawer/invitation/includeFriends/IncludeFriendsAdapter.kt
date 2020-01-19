package com.dish.seekdish.ui.navDrawer.invitation.includeFriends

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dish.seekdish.ui.navDrawer.invitation.includeFriendDataModels.fragments.selected.SelectedInclFragment
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.friends.FriendsInclFragment

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