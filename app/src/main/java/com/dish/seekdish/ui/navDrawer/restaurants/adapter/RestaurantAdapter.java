package com.dish.seekdish.ui.navDrawer.restaurants.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dish.seekdish.ui.navDrawer.myFavourite.fragment.FavouriteMapFragment;
import com.dish.seekdish.ui.navDrawer.myFavourite.fragment.ListFavouriteFragment;
import com.dish.seekdish.ui.navDrawer.restaurants.fragments.ProximityFragment;
import com.dish.seekdish.ui.navDrawer.restaurants.fragments.RestroMapFragment;
import com.dish.seekdish.ui.navDrawer.restaurants.fragments.TimeRestroFragment;
import com.google.android.gms.maps.MapFragment;


public class RestaurantAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public RestaurantAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProximityFragment proximityFragment = new ProximityFragment();
                return proximityFragment;
            case 1:
                TimeRestroFragment timeRestroFragment = new TimeRestroFragment();
                return timeRestroFragment;

            case 2:
                RestroMapFragment restroMapFragment = new RestroMapFragment();
                return restroMapFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}