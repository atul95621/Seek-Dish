package com.dish.seekdish.ui.navDrawer.restaurantDiscription.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescpModel
import java.util.ArrayList
import java.util.LinkedHashMap


class RestroDetailsFragment(var response: RestroDescpModel) : Fragment() {
    private val team = LinkedHashMap<String, GroupData>()
    private val deptList = ArrayList<GroupData>()

    private var listAdapter: RestroDetailAdapter? = null
    private var simpleExpandableListView: ExpandableListView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restro_details, container, false)
        // add data for displaying in expandable list view
        loadData()

        //get reference of the ExpandableListView
        simpleExpandableListView =
            view.findViewById(R.id.simpleRestroDetailExpandableListView) as ExpandableListView
        // create the adapter by passing your ArrayList data
        listAdapter = RestroDetailAdapter(context, deptList)
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
            /*Toast.makeText(
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

        var email = response.data.restaurant.restaurant_detail.detail[0].email
        addProduct("Email", email)
        var website = response.data.restaurant.restaurant_detail.detail[0].website
        addProduct("Website", website)
        var guets = response.data.restaurant.restaurant_detail.detail[0].guests
        addProduct("Guests", guets)
        var origin = response.data.restaurant.restaurant_detail.detail[0].origin
        addProduct("Origin", origin)
        var phone = response.data.restaurant.restaurant_detail.detail[0].phone
        addProduct("Phone", phone)
        var schedule = response.data.restaurant.restaurant_detail.detail[0].schedule
        addProduct("Schedule", schedule)
        for (items in response.data.restaurant.restaurant_detail.additional_services) {
            addProduct("Additioanal Services", items)
        }
        for (items in response.data.restaurant.restaurant_detail.restaurant_ambiance) {
            addProduct("Restaurant Ambiance", items)
        }
        for (items in response.data.restaurant.restaurant_detail.restaurant_ambiance_complementary) {
            addProduct("Complementary", items)
        }
        for (items in response.data.restaurant.restaurant_detail.additional_services) {
            addProduct("Services", items)
        }

    }


    // here we maintain team and player names
    private fun addProduct(teamName: String, playerName: String): Int {

        var groupPosition = 0

        //check the hash map if the group already exists
        var headerInfo: GroupData? = team[teamName]
        //add the group if doesn't exists
        if (headerInfo == null) {
            headerInfo = GroupData()
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
        val detailInfo = ChildData()
        detailInfo.name = playerName
        productList.add(detailInfo)
        headerInfo.playerName = productList

        // find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo)
        return groupPosition
    }


}
