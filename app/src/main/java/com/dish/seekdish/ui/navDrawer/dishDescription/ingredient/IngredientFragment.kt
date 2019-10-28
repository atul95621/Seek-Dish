package com.dish.seekdish.ui.navDrawer.dishDescription.ingredient

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import java.util.ArrayList
import java.util.LinkedHashMap


class IngredientFragment : Fragment() {

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
            Toast.makeText(
                context, " Team And Player :: " + headerInfo.name
                        + "/" + detailInfo.name, Toast.LENGTH_LONG
            ).show()
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

        addProduct("Main Ingredients", "Sheep")
        addProduct("Main Ingredients", "Beef")
        addProduct("Main Ingredients", "Salmon")

        addProduct("Secondary Ingredients", "Carrot")
        addProduct("Secondary Ingredients", "Pepper")

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
