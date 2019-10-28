package com.dish.seekdish.ui.navDrawer.restaurantDiscription.details;

import com.dish.seekdish.ui.navDrawer.dishDescription.ingredient.ChildInfo;

import java.util.ArrayList;

public class GroupData {

    private String teamName;
    public ArrayList<ChildData> list = new ArrayList<ChildData>();

    public String getName() {
        return teamName;
    }

    public void setName(String teamName) {
        this.teamName = teamName;
    }

    public ArrayList<ChildData> getPlayerName() {
        return list;
    }

    public void setPlayerName(ArrayList<ChildData> playerName) {
        this.list = playerName;
    }
}
