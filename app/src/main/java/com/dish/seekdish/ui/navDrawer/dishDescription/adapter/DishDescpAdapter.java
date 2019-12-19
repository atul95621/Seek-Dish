package com.dish.seekdish.ui.navDrawer.dishDescription.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.dish.seekdish.ui.navDrawer.dishDescription.ingredient.IngredientFragment;
import com.dish.seekdish.ui.navDrawer.dishDescription.model.DishDescpModel;
import com.dish.seekdish.ui.navDrawer.dishDescription.opinion.OpinionFragment;
import com.dish.seekdish.ui.navDrawer.dishDescription.similar.SimilarFragment;

public class DishDescpAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
      public   DishDescpModel objDishModel;

    public DishDescpAdapter(FragmentManager fm, int NumOfTabs, DishDescpModel objDishModel) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.objDishModel=objDishModel;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                IngredientFragment ingredientFragment = new IngredientFragment(objDishModel);
                return ingredientFragment;
            case 1:
                OpinionFragment opinionFragment = new OpinionFragment(objDishModel);
                return opinionFragment;

            case 2:
                SimilarFragment similarFragment = new SimilarFragment(objDishModel);
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
