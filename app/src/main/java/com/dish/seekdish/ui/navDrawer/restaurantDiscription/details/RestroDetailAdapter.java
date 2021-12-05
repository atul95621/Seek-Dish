package com.dish.seekdish.ui.navDrawer.restaurantDiscription.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dish.seekdish.R;
import com.dish.seekdish.custom.GlideApp;
import com.dish.seekdish.util.SessionManager;


import java.util.ArrayList;

public class RestroDetailAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<GroupData> teamName;
    String pos = "";

    public RestroDetailAdapter(Context context, ArrayList<GroupData> deptList) {
        this.context = context;
        this.teamName = deptList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChildData> productList = teamName.get(groupPosition).getPlayerName();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        ChildData detailInfo = (ChildData) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.item_layout_restro_detail_child, null);
        }
        TextView childItem = (TextView) view.findViewById(R.id.childDetailItem);
        childItem.setText(detailInfo.getName().trim());

        // giving red color if text is for menu
        if (detailInfo.getName().equals(context.getResources().getString(R.string.menu_click))) {
            childItem.setTextColor(Color.parseColor("#EF0C1D"));
        } else {
            childItem.setTextColor(Color.parseColor("#7E7E7E"));
        }

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<ChildData> productList = teamName.get(groupPosition).getPlayerName();
        return productList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return teamName.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return teamName.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        GroupData headerInfo = (GroupData) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_layout_restro_detail_group, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        ImageView imgIcon = (ImageView) view.findViewById(R.id.imgIcon);


        heading.setText(headerInfo.getName().trim());

        if (headerInfo.getName().equals(context.getResources().getString(R.string.menu))) {
            imgIcon.setVisibility(View.GONE);
        } else {
            imgIcon.setVisibility(View.VISIBLE);
            imgIcon.setImageResource(R.drawable.ic_down);
        }

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
