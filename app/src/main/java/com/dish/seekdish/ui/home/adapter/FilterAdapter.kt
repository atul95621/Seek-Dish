package com.dish.seekdish.ui.home.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.ChildData
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.GroupData
import com.dish.seekdish.util.Global

import java.util.ArrayList

class FilterAdapter(private val context: Context, private val teamName: ArrayList<GroupData>) :
    BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        val productList = teamName[groupPosition].playerName
        return productList[childPosition]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        view: View?, parent: ViewGroup
    ): View {
        var view = view


        val detailInfo = getChild(groupPosition, childPosition) as ChildData
        if (view == null) {
            val infalInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = infalInflater.inflate(R.layout.item_layout_filter_child, null)
        }
        val childItem = view!!.findViewById<View>(R.id.childDetailItem) as TextView
        val childDetailItemId = view!!.findViewById<View>(R.id.childDetailItemId) as TextView
        val linChildLayout = view.findViewById<View>(R.id.linFilterChild) as LinearLayout
        val filterCheckBox = view.findViewById<View>(R.id.filterCheckBox) as CheckBox
        val linCheckbox = view.findViewById<View>(R.id.linCheckbox) as LinearLayout


        childItem.text = detailInfo.name.trim { it <= ' ' }
        Log.e("item", "" + childItem.text)

        childDetailItemId.text = detailInfo.itemId.trim { it <= ' ' }
        Log.e("itemId", "" + childDetailItemId.text)


        if (detailInfo.selected == true) {
            filterCheckBox.isChecked = true
        } else {
            filterCheckBox.isChecked = false
        }



        linChildLayout.setOnClickListener {
            //                Log.e("filterchildPosition","    "+detailInfo.getName()+" status is now "+filterCheckBox.isChecked());

            if (filterCheckBox.isChecked) {
                // flag just to chnage the custom checkbox
                filterCheckBox.isChecked = false


                Log.e(
                    "filterchildPositionif",
                    "    " + detailInfo.name + " status is now " + filterCheckBox.isChecked + "in " + detailInfo.groupName + " group"
                )

                    // to tell model that it has been not been selected
                    detailInfo.selected = false

                // method to add the selected ids to static variable
                deleteSelected(detailInfo.itemId, detailInfo.groupName)

            } else {
                // flag just to chnage the custom checkbox
                filterCheckBox.isChecked = true
                Log.e(
                    "filterchildPositionelse",
                    "    " + detailInfo.name + " status is now " + filterCheckBox.isChecked + "in " + detailInfo.groupName + " group"
                )
                // to tell model that it has been selected
                detailInfo.selected = true

                // method to add the selected ids to static variable
                addSelected(detailInfo.itemId, detailInfo.groupName)
            }

            notifyDataSetChanged()
        }

        /* filterCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
             if (filterCheckBox.isChecked) {
                 // your code to checked checkbox
                 Log.e("filterchildPositionif", "    " + detailInfo.name + " status is now " + filterCheckBox.isChecked)
             } else {
                 // your code to  no checked checkbox
                 Log.e(
                     "filterchildPositionelse",
                     "    " + detailInfo.name + " status is now " + filterCheckBox.isChecked
                 )
             }
         }*/

        return view
    }

    private fun deleteSelected(itemId: String?, groupName: String?) {
        if (groupName.equals("Budget")) {

            for (i in 0 until Global.budgetSet.size) {
                if (Global.budgetSet.contains(itemId)) {
                    Global.budgetSet.remove(itemId)
                }
            }
            Log.e("ListDeleteBudget", TextUtils.join(",", Global.budgetSet))


        } else if (groupName.equals("Service Speed")) {

            for (i in 0 until Global.serviceSet.size) {
                if (Global.serviceSet.contains(itemId)) {
                    Global.serviceSet.remove(itemId)
                }
            }
            Log.e("ListDeleteService", TextUtils.join(",", Global.serviceSet))


        } else if (groupName.equals("Meal type")) {

            for (i in 0 until Global.mealSet.size) {
                if (Global.mealSet.contains(itemId)) {
                    Global.mealSet.remove(itemId)
                }
            }

        } else if (groupName.equals("Compatibility intolerance")) {

            for (i in 0 until Global.compatIntSet.size) {
                if (Global.compatIntSet.contains(itemId)) {
                    Global.compatIntSet.remove(itemId)
                }
            }


        } else if (groupName.equals("Restaurant speciality")) {

            for (i in 0 until Global.restroSpeclSet.size) {
                if (Global.restroSpeclSet.contains(itemId)) {
                    Global.restroSpeclSet.remove(itemId)
                }
            }

        } else if (groupName.equals("Restaurant ambiance")) {

            for (i in 0 until Global.restroAmbiSet.size) {
                if (Global.restroAmbiSet.contains(itemId)) {
                    Global.restroAmbiSet.remove(itemId)
                }
            }

        } else if (groupName.equals("Complementary ambiance")) {
            for (i in 0 until Global.compAmbianceSet.size) {
                if (Global.compAmbianceSet.contains(itemId)) {
                    Global.compAmbianceSet.remove(itemId)
                }
            }

        } else if (groupName.equals("Additional Services")) {

            for (i in 0 until Global.additonalSet.size) {
                if (Global.additonalSet.contains(itemId)) {
                    Global.additonalSet.remove(itemId)
                }
            }

        } else if (groupName.equals("Seasonality")) {

            for (i in 0 until Global.seasonlitySet.size) {
                if (Global.seasonlitySet.contains(itemId)) {
                    Global.seasonlitySet.remove(itemId)
                }
            }
        }
    }

    private fun addSelected(itemId: String?, groupName: String?) {

        if (groupName.equals("Budget")) {

            Global.budgetSet.add(itemId)
            Log.e("Listaddedbudget", TextUtils.join(",", Global.budgetSet))

        } else if (groupName.equals("Service Speed")) {

            Global.serviceSet.add(itemId)
            Log.e("ListaddedService", TextUtils.join(",", Global.serviceSet))


        } else if (groupName.equals("Meal type")) {

            Global.mealSet.add(itemId)

        } else if (groupName.equals("Compatibility intolerance")) {

            Global.compatIntSet.add(itemId)

        } else if (groupName.equals("Restaurant speciality")) {

            Global.restroSpeclSet.add(itemId)

        } else if (groupName.equals("Restaurant ambiance")) {

            Global.restroAmbiSet.add(itemId)

        } else if (groupName.equals("Complementary ambiance")) {
            Global.compAmbianceSet.add(itemId)

        } else if (groupName.equals("Additional Services")) {

            Global.additonalSet.add(itemId)

        } else if (groupName.equals("Seasonality")) {

            Global.seasonlitySet.add(itemId)
        }

    }

    override fun getChildrenCount(groupPosition: Int): Int {

        val productList = teamName[groupPosition].playerName
        Log.e("listsize", productList.size.toString())

        return productList.size


        /*  // tigdam so as the items cant be reapeated 2 times...
          var divideHalfSize = productList.size / 2
          var subsHalfSize = productList.size.minus(divideHalfSize)
          return subsHalfSize
  */

    }

    override fun getGroup(groupPosition: Int): Any {
        return teamName[groupPosition]
    }

    override fun getGroupCount(): Int {
        return teamName.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int, isLastChild: Boolean, view: View?,
        parent: ViewGroup
    ): View {
        var view = view

        val headerInfo = getGroup(groupPosition) as GroupData
        if (view == null) {
            val inf = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inf.inflate(R.layout.item_layout_filter_group, null)
        }

        val heading = view!!.findViewById<View>(R.id.heading) as TextView

        heading.text = headerInfo.name.trim { it <= ' ' }

        return view
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }


}
