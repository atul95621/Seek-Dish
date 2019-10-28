package com.dish.seekdish.ui.navDrawer.myFavourite.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.ListFragment;
import com.dish.seekdish.ui.navDrawer.myFavourite.fragment.FavouriteMapFragment;
import com.dish.seekdish.ui.navDrawer.myFavourite.fragment.ListFavouriteFragment;


public class MyFavouriteFragAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public MyFavouriteFragAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ListFavouriteFragment listFavouriteFragment = new ListFavouriteFragment();
                return listFavouriteFragment;
            case 1:
                FavouriteMapFragment favouriteMapFragment = new FavouriteMapFragment();
                return favouriteMapFragment;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}