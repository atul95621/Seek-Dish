package com.dish.seekdish.ui.navDrawer.invitation.invitedDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.ChildData
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.GroupData
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.RestroDetailAdapter
import com.dish.seekdish.ui.navDrawer.settings.myAlerts.InvitationModel
import kotlinx.android.synthetic.main.fragment_ingredient.*
import java.util.ArrayList
import java.util.LinkedHashMap

class InvitationDeatilsFragment(var invitationModel: InvitationModel) : Fragment() {

    private val team = LinkedHashMap<String, GroupData>()
    private val deptList = ArrayList<GroupData>()

    private var listAdapter: RestroDetailAdapter? = null
    private var simpleExpandableListView: ExpandableListView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_invited_deatils, container, false)

        // add data for displaying in expandable list view
        loadData()

        //get reference of the ExpandableListView
        simpleExpandableListView =
            view.findViewById(R.id.simpleIniviteDetailExpandableListView) as ExpandableListView
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
           /* Toast.makeText(
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

        var email = invitationModel.data.details_tab_arr.email
        addProduct("Email", email)
        var Name = invitationModel.data.details_tab_arr.name
        addProduct("Speed", Name)
        var phone = invitationModel.data.details_tab_arr.phone
        addProduct("Phone", phone)
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
