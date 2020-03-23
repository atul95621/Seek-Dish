package com.dish.seekdish.ui.navDrawer.restaurantDiscription.details;

public class ChildData {

    public String playerName = "";
    String itemId;
    String isSelected;
    String groupName = "";

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public String getSelected() {
        return isSelected;
    }

    public void setSelected(String selected) {
        isSelected = selected;
    }

    public String getName() {
        return playerName;
    }

    public void setName(String playerName) {
        this.playerName = playerName;
    }

}
