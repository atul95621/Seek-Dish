package com.dish.seekdish.ui.navDrawer.invitation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import com.dish.seekdish.ui.navDrawer.invitation.invited.InvitedFragment
import com.dish.seekdish.ui.navDrawer.invitation.invitedDetails.InvitationDeatilsFragment
import com.dish.seekdish.ui.navDrawer.invitation.map.InvitationMapFragment
import com.dish.seekdish.ui.navDrawer.settings.myAlerts.InvitationModel

class InvitationAdapter(
    fm: FragmentManager,
    private val mNumOfTabs: Int,
   var invitationModel: InvitationModel
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return InvitedFragment(invitationModel)
            }
            1 -> {
                return InvitationMapFragment(invitationModel)
            }

            2 -> {
                return InvitationDeatilsFragment(invitationModel)
//                return RestroDetailsFragment()
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}
