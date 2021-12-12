package com.dish.seekdish.ui.navDrawer.restaurantDiscription.details

import android.content.Intent
import android.net.Uri
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
    var pdfURL: String = ""
    var websiteURL: String = ""

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
            /*        Toast.makeText(
                        context, " Child name :: "
                                + "/" + detailInfo.name, Toast.LENGTH_LONG
                    ).show()*/

            // opening pdf file in chrome if available
            if (detailInfo.name == context?.resources?.getString(R.string.menu_click)) {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(pdfURL)
                )
                startActivity(browserIntent)
            }

            // opening in chrome if there is website link
            if (headerInfo.name == context?.resources?.getString(R.string.website)) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL))
                startActivity(browserIntent)
            }
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

        pdfURL = response.data.restaurant.pdf_url.toString()
        if (pdfURL.isNullOrEmpty() == false) {
            addProduct(getString(R.string.menu), getString(R.string.menu_click))
        }
        var email = response.data.restaurant.restaurant_detail.detail[0].email ?: "null"
        addProduct(getString(R.string.email), email)
        var webs = response.data.restaurant.restaurant_detail.detail[0].website
        if (!webs.isNullOrEmpty()) {
            var website = response.data.restaurant.restaurant_detail.detail[0].website ?: "null"
            websiteURL = website
            addProduct(getString(R.string.website), website)
        }
        var serviceSpeed = response.data.restaurant.service_speed
        addProduct(getString(R.string.service_sped), serviceSpeed.toString())
        var guets = response.data.restaurant.restaurant_detail.detail[0].guests ?: "null"
        addProduct(getString(R.string.guest), guets)
        var origin = response.data.restaurant.restaurant_detail.detail[0].origin ?: "null"
        addProduct(getString(R.string.origin), origin)
        var phone = response.data.restaurant.restaurant_detail.detail[0].phone ?: "null"
        addProduct(getString(R.string.phone), phone)
        var schedule = response.data.restaurant.restaurant_detail.detail[0].schedule ?: "null"
        addProduct(getString(R.string.schedule), schedule)
        for (items in response.data.restaurant.restaurant_detail.specialities) {
            addProduct(getString(R.string.specialities), items)
        }
        for (items in response.data.restaurant.restaurant_detail.additional_services) {
            addProduct(getString(R.string.servic), items)
        }
        for (items in response.data.restaurant.restaurant_detail.restaurant_ambiance) {
            addProduct(getString(R.string.restro_amb), items)
        }
        for (items in response.data.restaurant.restaurant_detail.restaurant_ambiance_complementary) {
            addProduct(getString(R.string.comple), items)
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
