package com.dish.seekdish.ui.navDrawer.invitation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dish.seekdish.ui.home.fragments.HomeMapFragment

import com.dish.seekdish.ui.navDrawer.invitation.invited.InvitedFragment
import com.dish.seekdish.ui.navDrawer.invitation.invitedDetails.InvitationDeatilsFragment
import com.dish.seekdish.ui.navDrawer.invitation.map.InvitationMapFragment
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.RestroDetailsFragment

class InvitationAdapter(fm: FragmentManager, private val mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return InvitedFragment()
            }
            1 -> {
                return InvitationMapFragment()
            }

           /* 2 -> {
//                return InvitationDeatilsFragment()
                return RestroDetailsFragment()
            }
*/
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}
