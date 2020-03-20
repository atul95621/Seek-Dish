package com.dish.seekdish.ui.navDrawer.dishDescription.ingredient

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.model.Data
import com.dish.seekdish.ui.navDrawer.dishDescription.model.DishDescpModel
import com.dish.seekdish.ui.navDrawer.dishDescription.model.Meals
import com.dish.seekdish.util.BaseFragment
import java.util.ArrayList
import java.util.LinkedHashMap


class IngredientFragment(var objDishModel: DishDescpModel) : BaseFragment() {

    // objDishModel is model instance ...

    private val team = LinkedHashMap<String, GroupInfo>()
    private val deptList = ArrayList<GroupInfo>()

    private var listAdapter: CustomAdapter? = null
    private var simpleExpandableListView: ExpandableListView? = null

    lateinit var context: DishDescriptionActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingredient, container, false)

        context = activity as DishDescriptionActivity


        // add data for displaying in expandable list view
        loadData()

        //get reference of the ExpandableListView
        simpleExpandableListView = view.findViewById(R.id.simpleExpandableListView) as ExpandableListView
        // create the adapter by passing your ArrayList data
        listAdapter = CustomAdapter(context, deptList)
        // attach the adapter to the expandable list view
        simpleExpandableListView!!.setAdapter(listAdapter)

        val count = listAdapter!!.getGroupCount()
        for (i in 0 until count)
            simpleExpandableListView!!.expandGroup(i)

        // setOnChildClickListener listener for child row click
        simpleExpandableListView!!.setOnChildClickListener(ExpandableListView.OnChildClickListener { parent, v, groupPosition, childPosition, id ->
            //get the group header
            val headerInfo = deptList[groupPosition]
            //get the child info
            val detailInfo = headerInfo.playerName[childPosition]
            //display it or do something with it
         /*   Toast.makeText(
                context, " Team And Player :: " + headerInfo.name
                        + "/" + detailInfo.name, Toast.LENGTH_LONG
            ).show()*/
            false
        })
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView!!.setOnGroupClickListener(ExpandableListView.OnGroupClickListener { parent, v, groupPosition, id ->
            //get the group header
            /* val headerInfo = deptList[groupPosition]
             //display it or do something with it
             Toast.makeText(
                 context, " Team Name :: " + headerInfo.name,
                 Toast.LENGTH_LONG
             ).show()*/

            true
        })

        return view
    }


    // load some initial data into out list
    private fun loadData() {
        // objDishModel is model instance ...


        var intolerance = objDishModel.data.Ingredients.intolerance_compatibilities
        var element_ao = objDishModel.data.Ingredients.Elements_Ao
        var fat_appellation = objDishModel.data.Ingredients.Fat_Appellation_Of_Controlled_Origin_Aop
        var fats = objDishModel.data.Ingredients.Fats
        var second_ingre = objDishModel.data.Ingredients.Secondary_Ingredients
        var main_ingre = objDishModel.data.Ingredients.Main_Ingredients
        var alcohol = objDishModel.data.Ingredients.alcohol
        var type_cooking = objDishModel.data.Ingredients.Type_of_Cooking
        var tags = objDishModel.data.Ingredients.meal_tags
        var rest_tags = objDishModel.data.Ingredients.restaurant_tags
        var seasoning = objDishModel.data.Ingredients.seasoning


        for (items in main_ingre) {
            addProduct(getString(R.string.main_ingre), items)
        }
        for (items in second_ingre) {
            addProduct(getString(R.string.secondary_ingre), items.toString())
        }
        for (items in element_ao) {
            addProduct(getString(R.string.element_Ao), items.toString())
        }
        for (items in type_cooking) {
            addProduct(getString(R.string.type_of), items.toString())
        }
        for (items in fats) {
            addProduct(getString(R.string.fats), items.toString())
        }
        for (items in fat_appellation) {
            addProduct(getString(R.string.fat_app), items.toString())
        }
        for (items in alcohol) {
            addProduct(getString(R.string.alcohol), items.toString())
        }
        for (items in seasoning) {
            addProduct(getString(R.string.season), items.toString())
        }
        /*for (items in intolerance) {
            addProduct(getString(R.string.intol_capa), items.toString())
        }*/
        for (items in tags) {
            addProduct(getString(R.string.meal), items.toString())
        }
        for (items in rest_tags) {
            addProduct(getString(R.string.restro_tags), items.toString())
        }

    }


    // here we maintain team and player names
    private fun addProduct(teamName: String, playerName: String): Int {

        var groupPosition = 0

        //check the hash map if the group already exists
        var headerInfo: GroupInfo? = team[teamName]
        //add the group if doesn't exists
        if (headerInfo == null) {
            headerInfo = GroupInfo()
            headerInfo.name = teamName
            team[teamName] = headerInfo
            deptList.add(headerInfo)
        }

        // get the children for the group
        val productList = headerInfo.playerName
        // size of the children list
        var listSize = productList.size
        // add to the counter
        listSize++

        // create a new child and add that to the group
        val detailInfo = ChildInfo()
        detailInfo.name = playerName
        productList.add(detailInfo)
        headerInfo.playerName = productList

        // find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo)
        return groupPosition
    }


}
