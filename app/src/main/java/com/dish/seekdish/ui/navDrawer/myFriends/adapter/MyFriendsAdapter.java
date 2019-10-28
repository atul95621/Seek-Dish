package com.dish.seekdish.ui.navDrawer.myFriends.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FollowerFragment;
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FollowingFragment;
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FriendsFragment;


public class MyFriendsAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public MyFriendsAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            case 1:
                FollowerFragment followerFragment = new FollowerFragment();
                return followerFragment;

            case 2:
                FollowingFragment followingFragment = new FollowingFragment();
                return followingFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}