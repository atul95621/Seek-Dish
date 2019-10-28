package com.dish.seekdish.ui.navDrawer.dishDescription.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.dish.seekdish.ui.navDrawer.dishDescription.ingredient.IngredientFragment;
import com.dish.seekdish.ui.navDrawer.dishDescription.opinion.OpinionFragment;
import com.dish.seekdish.ui.navDrawer.dishDescription.similar.SimilarFragment;

public class DishDescpAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public DishDescpAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                IngredientFragment ingredientFragment = new IngredientFragment();
                return ingredientFragment;
            case 1:
                OpinionFragment opinionFragment = new OpinionFragment();
                return opinionFragment;

            case 2:
                SimilarFragment similarFragment = new SimilarFragment();
                return similarFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
