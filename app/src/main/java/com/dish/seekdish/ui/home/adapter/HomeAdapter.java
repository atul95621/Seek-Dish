package com.dish.seekdish.ui.home.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.dish.seekdish.ui.home.fragments.HomeMapFragment;
import com.dish.seekdish.ui.home.fragments.TasteFragment;
import com.dish.seekdish.ui.home.fragments.TimeFragment;
import com.dish.seekdish.ui.navDrawer.myFriends.MyFriendsFragment;
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FriendsFragment;

public class HomeAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public HomeAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TasteFragment tasteFragment = new TasteFragment();
                return tasteFragment;
            case 1:
                TimeFragment timeFragment = new TimeFragment();
                return timeFragment;

            case 2:
                HomeMapFragment mapFragment = new HomeMapFragment();
                return mapFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}